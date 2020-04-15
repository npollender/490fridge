from picamera import PiCamera
from time import sleep
import RPi.GPIO as GPIO
from hx711 import HX711
from upload_to_s3 import upload_to_aws
from connect import connect, execute_query

referenceUnit = -28150
camera = PiCamera()
GPIO.setmode(GPIO.BCM)
hx = HX711(5, 6)
hx.set_reading_format("MSB", "MSB")
hx.set_reference_unit(referenceUnit)

hx.reset()
while True:


        #hx.tare()
        val = hx.get_weight(5)
        real_val = (val * 1000 + 2212)
        
        
        print((val * 1000) + 2212)
        
        query = """ INSERT INTO Device_tb (d_name, d_pin, wifi_name, wifi_pwd) VALUES (%s, %s, %s, %s) ON CONFLICT (d_name) DO NOTHING"""
        data = ('GYN', 1234 , 'none', 'none')
        execute_query(query, data)

        query = """ INSERT INTO inventory_tb (partition, item_name, quantity, device_name) VALUES (%s, %s, %s, %s) ON CONFLICT (partition) DO UPDATE SET quantity = %s"""
        data = ('A', 'Beer', real_val, 'GYN', real_val)
        execute_query(query, data)


        hx.power_down()
        hx.power_up()
    

        camera.capture("image.jpg")
        upload_to_aws("image.jpg", "image.jpg")
    
    
        sleep(10)
        
        #send to server here
