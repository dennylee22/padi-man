import mysql.connector as mysql
host = '10.79.48.3'
db = 'solusi'
user = 'padiman'
password = 'B21-CAP0157'

def get_solusi(penyakit):
	solusi = []
	con = mysql.connect(host=host, database=db, user=user, password=password)
	cursor = con.cursor()
	cursor.execute("SELECT * FROM solusi WHERE nama = '" + penyakit + "';")
	rows = cursor.fetchall()
	for x in range(0,len(rows)):
		solusi.append(rows[x][1])
	return(solusi)
