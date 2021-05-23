from keras.models import load_model
from keras.preprocessing import image
import numpy as np

def get_penyakit(path):
	model = load_model('/home/c0050428/model.h5')
	img = image.load_img(path, target_size=(160,160))
	x = image.img_to_array(img)
	x = np.expand_dims(x, axis=0)

	images = np.vstack([x])
	classes = model.predict(images, batch_size=10)
	predicted_index = np.argmax(classes)
	return(predicted_index)
