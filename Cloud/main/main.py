import flask
import mimetypes
from flask import request, jsonify
import pathlib
from werkzeug.utils import secure_filename
import mysql.connector as mysql
from db import get_solusi
import json
import requests
import os

model_url = 'http://192.168.1.4:9000/model'
app = flask.Flask(__name__)
app.config["DEBUG"] = True

allowed_files = {'.png','.jpg','.jpeg'}
allowed_mime = {'image/png','image/jpeg','image/jpg'}

#URL buat endpoint http://34.87.146.226:9000/upload
#Bikin HTTP Post request ke URL-nya, pake variabel "file" buat gambarnya
@app.route('/upload', methods=['POST'])
def upload():
	if request.method == "POST":
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
					dict = {"penyakit":penyakit,"solusi":solusi}
					return json.dumps(dict)
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

app.run(host='0.0.0.0',port=9000)
