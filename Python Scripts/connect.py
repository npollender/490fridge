import psycopg2
from config import config

def execute_query(query, data):
    """ Execute a single query on PostgreSQL database server """
    conn = None
    try:
        conn = connect()
        cursor = conn.cursor()
        cursor.execute(query, data)
        conn.commit()

    except (Exception, psycopg2.Error) as error:
        if (conn):
            print("Failed to insert record into db", error)
    finally:
        if conn is not None:
            conn.close()


def connect():
    """ Connect to the PostgreSQL database server """
    conn = None
    try:
        # read connection parameters
        params = config()
        
        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        conn = psycopg2.connect(**params)
        
        # create a cursor
        cur = conn.cursor()
        
        # execute a statement
        print('PostgreSQL database version:')
        cur.execute('SELECT version()')
        
        # display the PostgreSQL database server version
        db_version = cur.fetchone()
        if db_version is not None:
            print('Connection to PostgreSQL was successful!!!')
        
        # close the communication with the PostgreSQL
        cur.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    return conn