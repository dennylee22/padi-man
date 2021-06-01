# flaskapp.py
# This is a "hello world" app sample for flask app. You may have a different file.
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
from flask import Flask

model_url = 'http://192.168.1.5/model'
app = flask.Flask(__name__)
app.config["DEBUG"] = True
api = Api(app)
gambarkresek = "https://drive.google.com/uc?id=1J_6jAIsMgfYMgFo2zhzMXjRMtyvnGwVt"
gambarbercak = "https://drive.google.com/uc?id=1WE-vzfprTXLKZmw-rGApGeAHJ_zFFoRD"
gambarsmut = "https://drive.google.com/uc?id=1Ozocwhm5fkbCOaLbRrgbpWEENXorHA56"
dict = {0:'Hawar Daun Bakteri/Kresek',1:'Bercak Daun Cokelat',2:'Leaf Smut'}
img_dict = {'Hawar Daun Bakteri/Kresek':gambarkresek,'Bercak Daun Cokelat':gambarbercak,'Leaf Smut':gambarsmut}
allowed_files = {'.png','.jpg','.jpeg'}
allowed_mime = {'image/png','image/jpeg','image/jpg'}

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
                                        solusi = get_solusi(penyakit)
                                        result = {"penyakit":penyakit,"gambar":img_dict[penyakit],"solusi":solusi}
                                        #Masukkin data ke BigQuery
                                        #lokasi=None karena belom bisa mengmbil lokasi user
                                        lokasi=None
                                        if penyakit != "tidak ada penyakit yang terdeteksi":
                                                feedback = save_data(penyakit, lokasi)
                                        return(result)
                                        #expected return:
					#{
 						#"penyakit": "Hawar Daun Bakteri/Kresek",
   						#"gambar": "https://drive.google.com/uc?id=1J_6jAIsMgfYMgFo2zhzMXjRMtyvnGwVt",
    						#"solusi": [
        						#"Penanaman benih dan bibit yang sehat.",
        						#"Penanaman dengan sistem Legowo dan menggunakan sistem pengairan secara berselang (intermitten irrigation). Sistem tersebut membantu mengurangi terjadinya embun dan air gutasi dan gesekan daun antar tanaman yang menjadi media penularan pathogen.",
        						#"Menghindari penggunaan pupuk Nitrogen yang terlalu tinggi. Hindari juga penggunaan pupuk Kalium yang terlalu sedikit. Gunakan pupuk Nitrogen seimbang dengan pupuk Kalium.",
        						#"Menjaga kebersihan sawah dari gulma dan sisa - sisa tanaman yang terinfeksi."
    							#]
					#}
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
                        return({"penyakit":dict[id],"gambar":img_dict[dict[id]],"solusi":res})
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

@app.route('/') 
def hello_world():
   return 'Hello from Team Rice is Nice(Bangkit 2021)' 
if __name__ == '__main__':
   app.run()
