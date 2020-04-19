from flask import Flask
from flask import jsonify
from flask import request
import sqlite3
import datetime
from flask import g

app = Flask(__name__)

def create_connection(db_file):
    conn = None

    try:
        conn = sqlite3.connect(db_file)
        print(sqlite3.version)
        return conn
    except Error as e:
        print(e)

def create_user(user, conn):

    sql = ''' INSERT INTO users(username,pass) VALUES(?,?) '''
    cur = conn.cursor()
    cur.execute(sql, user)
    conn.commit()
    print(sql)
    ret = cur.lastrowid
    return ret

def main(username, password, conn):
    print(conn)
    with conn:
        user = (username, password)
        print(user)
        print(username)
        print(password)
        user_id = create_user(user,conn)

    conn.close()

def get_db():
    if not hasattr(g, 'corona.db'):
        g.conn = create_connection('corona.db')
    return g.conn

@app.teardown_appcontext
def close_db(error):
    if hasattr(g, 'corona.db'):
        g.conn.close()

@app.route("/signup", methods = ['POST'])
def signup():
    conn = get_db()
    cur = conn.cursor()
    content = request.get_json()
    for key in content.keys():
        cur.execute('SELECT username FROM users WHERE username = ?',(key,))
        print(key)
        if cur.fetchone() != None:
            return jsonify({"success":"There is already an account with this username. Please try again"})
        else:
            print("except")
            main(key, content[key], conn)

    return jsonify({"success": "success"})

@app.route("/signin", methods=['POST'])
def signin():
    content = request.get_json()
    conn = get_db()
    cur = conn.cursor()
    for key in content.keys():
        cur.execute('SELECT pass FROM users WHERE username = ?', (key,))
        s_id = cur.fetchone()
        if s_id == None:
            return jsonify({'success':'fail'})
        elif s_id[0] == content[key]:
            return jsonify({'success':'success'})
        else:
            return jsonify({'success':'fail'})


@app.route('/loc', methods=['POST'])
def put_location():
    content = request.get_json()
    conn = get_db()
    cur = conn.cursor()

    username = content["username"]
    latitude = float(content["latitude"])
    longitude = float(content["longitude"])
    hashCode = content["hashCode"]
    #date = int(content["date"])
    

    sql = ''' INSERT INTO location(username,hashkey,datetime,latitude,longitude) VALUES (?,?,?,?,?)'''
    data = (username,hashCode,datetime.datetime.now(),latitude,longitude)
    cur.execute(sql, data)
    conn.commit()
    return jsonify({'success':'success'})

@app.route('/hascorona', methods=['POST'])
def has_corona():
    content = request.get_json()
    conn = get_db()
    cur = conn.cursor()
    username = content["username"]
    sql = ''' INSERT INTO hascorona(username,datetime) VALUES(?,?) '''
    data = (username,datetime.datetime.now())
    cur.execute(sql, data)
    conn.commit()

    sql = ''' SELECT l2.username, l2.datetime, l2.latitude, l2.longitude, l2.hashkey FROM location l
                JOIN hascorona h JOIN location l2 WHERE h.username = l.username AND l.hashkey = l2.hashkey AND l.username <> l2.username
                AND datetime(l2.datetime) BETWEEN datetime(l.datetime, '-1 hours') AND datetime(l.datetime, '+1 hours') '''

    cur.execute(sql)
    rows = cur.fetchall()

    for row in rows:
        sql = ''' INSERT INTO exposed (username, datetime, latitude, longitude, hashkey) VALUES (?,?,?,?,?) '''
        cur.execute(sql, tuple(row))
    conn.commit()
    return jsonify({'success':'success'})
    

@app.route('/getcontagiousplaces', methods=['POST'])
def get_cont_locs():
    content = request.get_json()
    conn = get_db()
    cur = conn.cursor()

    username = content["username"]

    sql = ''' SELECT * FROM exposed WHERE username = ?'''
    cur.execute(sql, (username, ))
    if cur.fetchone() != None:
        return jsonify({'result':'yes'})
    return jsonify({'result':'no'})
