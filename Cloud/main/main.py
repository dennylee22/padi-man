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

model_url = 'http://192.168.1.4:9000/model'
app = flask.Flask(__name__)
app.config["DEBUG"] = True

dict = {0:'Hawar Daun Bakteri/Kresek',1:'Bercak Daun Cokelat',2:'Leaf Smut'}
allowed_files = {'.png','.jpg','.jpeg'}
allowed_mime = {'image/png','image/jpeg','image/jpg'}

#API untuk upload gambar dari user
#URL buat endpoint http://34.126.165.65:9000/upload
#Bikin HTTP POST request ke URL-nya, pake variabel "file" buat gambarnya
@app.route('/upload', methods=['POST'])
def upload():
	if request.method == "POST" or request.method == "post":
		if "file" in request.files:
			file = request.files['file']
			if pathlib.Path(file.filename).suffix in allowed_files :
				if file.mimetype in allowed_mime :
					filename = secure_filename(file.filename)
					file.save("/images/" + filename)
					files = {'file': (filename, open('/images/'+filename, 'rb'), file.mimetype)}
					os.remove('/images/' + filename)
					r = requests.post(model_url,files=files)
					penyakit = r.text
					solusi = get_solusi(penyakit)
					result = {"penyakit":penyakit,"solusi":solusi}
					#Masukkin data ke BigQuery
					#lokasi=None karena belom bisa mengmbil lokasi user
					lokasi=None
					if penyakit != "tidak ada penyakit yang terdeteksi":
						feedback = save_data(penyakit, lokasi)
					return json.dumps(result)
				#expected return:
				#{"penyakit": "Leaf Smut", "solusi": ["Menjaga kebersihan sawah dari sisa - sisa tanaman yang terinfeksi", "hindari penggunaan pupuk Nitrogen yang berlebihan", "Jika masih terinfeksi, tanam varietas padi yang memiliki ketahanan terhadap leaf smut."]}
				else:
					error = "Mimetype not allowed"
			else:
				error = "File is not an image"
		else:
			error = "File not found"
	else:
		error = "Method not allowed"
	if error:
		return(json.dumps({"error" : error}))

#API untuk fitur list semua penyakit
#URL buat endpoint http://34.126.165.65:9000/list ato http://34.126.165.65:9000/list?id=1
#bisa list solusi dari sebuah penyakit lewat request GET dan kasih id dari penyakit
@app.route('/list', methods=['GET'])
def list():
	if request.method == "GET" or request.method == "get":
		if request.args.get("id"):
			id = request.args.get("id", type = int)
			res = get_solusi(dict[id])
			return jsonify({"penyakit":dict[id],"solusi":res})
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
			return jsonify({"penyakit":res})
			#expected return
			#{
			#  "penyakit": [
			#    "Bercak Daun Cokelat", 
			#    "Hawar Daun Bakteri/Kresek", 
			#    "Leaf Smut"
			#  ]
			#}

	else:
		error = "Method not allowed"

	if error:
		return(json.dumps({"error" : error}))

app.run(host='0.0.0.0',port=9000)
