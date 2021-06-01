#!/usr/bin/python3
import flask
import mimetypes
from flask import request, jsonify
import pathlib
from werkzeug.utils import secure_filename
from ml import get_penyakit
import os
from flask_restful import Api, Resource

app = flask.Flask(__name__)
app.config["DEBUG"] = True
api = Api(app)
daftar_penyakit = {-1:'tidak ada penyakit yang terdeteksi', 0:'Hawar Daun Bakteri/Kresek', 1:'Bercak Daun Cokelat', 2:'Leaf Smut'}

class Model(Resource):
	def post(self):
		file = request.files['file']
		filename = secure_filename(file.filename)
		file.save("/images/" + filename)
		res = daftar_penyakit[get_penyakit("/images/"+filename)]
		os.remove("/images/" + filename)
		#print(res)
		return(res)
api.add_resource(Model,"/model")

app.run(host='0.0.0.0',port=9000)
