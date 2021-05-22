import flask
import mimetypes
from flask import request, jsonify
import pathlib
from werkzeug.utils import secure_filename

app = flask.Flask(__name__)
app.config["DEBUG"] = True

allowed_files = {'.png','.jpg','.jpeg'}
allowed_mime = {'image/png','image/jpeg','image/jpg'}

@app.route('/upload', methods=['GET','POST'])
def upload():
	if request.method == "POST":
		if "file" in request.files:
			file = request.files['file']
		if pathlib.Path(file.filename).suffix in allowed_files :
			if file.mimetype in allowed_mime :
				filename = "gambar.jpg"
				file.save("/images/" + filename)
				return (file.mimetype)
		else:
			return ("File extension not allowed")
	else:
		return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form method=post enctype=multipart/form-data>
      <input type=file name=file>
      <input type=submit value=Upload>
    </form>
    '''

app.run(host='0.0.0.0',port=9000)
