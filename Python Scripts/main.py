import psycopg2
import boto3
from connect import connect, execute_query
from upload_to_s3 import upload_to_aws

# sample statement to upload a file to s3 bucket
upload_to_aws("wall-murals-banana-tree.jpg", "bananas.jpg")


## sample insert statements to PostgreSQL
# query = """ INSERT INTO Device_tb (d_name, d_pin, wifi_name, wifi_pwd) VALUES (%s, %s, %s, %s)"""
# data = ('GYN', 1234 , 'none', 'none')
# execute_query(query, data)
# 
# query = """ INSERT INTO inventory_tb (partition, item_name, quantity, device_name) VALUES (%s, %s, %s, %s)"""
# data = ('dsf', 'Beer', 20, 'GYN')
# execute_query(query, data)

