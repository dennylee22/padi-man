#!/usr/bin/python3
import flask
import mimetypes
from flask import request, jsonify
import pathlib
from werkzeug.utils import secure_filename
import mysql.connector as mysql
from db import get_solusi, list_penyakit
import json
import requests
import os
from savedata import save_data
from flask_restful import Api, Resource

model_url = 'http://192.168.1.4:9000/model'
app = flask.Flask(__name__)
app.config["DEBUG"] = True
api = Api(app)

dict = {0:'Hawar Daun Bakteri/Kresek',1:'Bercak Daun Cokelat',2:'Leaf Smut'}
allowed_files = {'.png','.jpg','.jpeg'}
allowed_mime = {'image/png','image/jpeg','image/jpg'}

#API untuk upload gambar dari user
#URL buat endpoint http://34.126.165.65:9000/upload
#Bikin HTTP POST request ke URL-nya, pake variabel "file" buat gambarnya
class Upload(Resource):
	def post(self):
		if "file" in request.files:
			file = request.files['file']
			if pathlib.Path(file.filename).suffix in allowed_files :
				if file.mimetype in allowed_mime :
					filename = secure_filename(file.filename)
					file.save("/images/" + filename)
					files = {'file': (filename, open('/images/'+filename, 'rb'), file.mimetype)}
					os.remove('/images/' + filename)
					r = requests.post(model_url,files=files)
					penyakit = r.text[1:-2]
					#print(penyakit[1:-2])
					solusi = get_solusi(penyakit)
					result = {"penyakit":penyakit,"solusi":solusi}
					#Masukkin data ke BigQuery
					#lokasi=None karena belom bisa mengmbil lokasi user
					lokasi=None
					if penyakit != "tidak ada penyakit yang terdeteksi":
						feedback = save_data(penyakit, lokasi)
					return(result)
					#expected return:
					#{"penyakit": "Leaf Smut", "solusi": ["Menjaga kebersihan sawah dari sisa - sisa tanaman yang terinfeksi", "hindari penggunaan pupuk Nitrogen yang berlebihan", "Jika masih terinfeksi, tanam varietas padi yang memiliki ketahanan terhadap leaf smut."]}
				else:
					error = "Mimetype not allowed"
			else:
				error = "File is not an image"
		else:
			error = "File not found"
		if error:
			return({"error" : error})


api.add_resource(Upload,"/upload")

#API untuk fitur list semua penyakit
#URL buat endpoint http://34.126.165.65:9000/list ato http://34.126.165.65:9000/list?id=1
#bisa list solusi dari sebuah penyakit lewat request GET dan kasih id dari penyakit
class List(Resource):
	def get(self):
		if request.args.get("id"):
			id = request.args.get("id", type = int)
			res = get_solusi(dict[id])
			return({"penyakit":dict[id],"solusi":res})
			#expected return:
			#{
			#  "penyakit": "Leaf Smut", 
			#  "solusi": [
			#    "Menjaga kebersihan sawah dari sisa - sisa tanaman yang terinfeksi", 
			#    "hindari penggunaan pupuk Nitrogen yang berlebihan", 
			#    "Jika masih terinfeksi, tanam varietas padi yang memiliki ketahanan terhadap leaf smut."
			#  ]
			#}
		else:
			res = list_penyakit()
			return({"penyakit":res})
			#expected return
			#{
			#  "penyakit": [
			#    "Bercak Daun Cokelat", 
			#    "Hawar Daun Bakteri/Kresek", 
			#    "Leaf Smut"
			#  ]
			#}
api.add_resource(List,"/list")

app.run(host='0.0.0.0',port=9000)
