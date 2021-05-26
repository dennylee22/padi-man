from google.cloud import bigquery
from datetime import datetime

def save_data(penyakit ,lokasi):
	client = bigquery.Client().from_service_account_json("/home/c0050428/keys.json")
	table = "fast-metric-312505.padiman_data.data_penyakit"
	data = [
    		{u"penyakit": penyakit, u"waktu": datetime.today().strftime('%Y-%m-%d'), u"lokasi": lokasi}
		]
	error = client.insert_rows_json(table, data)
	return error
