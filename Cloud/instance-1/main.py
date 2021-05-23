import flask
import mimetypes
from flask import request, jsonify
import pathlib
from werkzeug.utils import secure_filename
from ml import get_penyakit
import os

app = flask.Flask(__name__)
app.config["DEBUG"] = True

dict = {0:'Hawar Daun Bakteri/Kresek',1:'Bercak Daun Cokelat',2:'Leaf Smut'}


@app.route('/model', methods=['POST'])
def model():
	file = request.files['file']
	filename = secure_filename(file.filename)
	file.save("/images/" + filename)
	res = dict[get_penyakit("/images/"+filename)]
	os.remove("/images/" + filename)
	return (res)

app.run(host='0.0.0.0',port=9000)
