import boto3
from config import config
from botocore.exceptions import NoCredentialsError


def upload_to_aws(local_file, s3_file):
    """ Upload a file image to s3 in aws """
    
    # s3 bucket name in aws
    bucket='smart-fridge-pics'
    
    # obtain parameters from configuration file
    params = config(section='s3_default')
    
    print('Connecting to the s3...')
    s3_client = boto3.client("s3", **params)
    
    if s3_client is not None:
        print('Connection to s3 was successful!!!')
    
    # path of the file to be inserted
    path = "/home/pi/Desktop/db_config/" + local_file
    
    try:
        s3_client.upload_file(path, bucket, s3_file)
        print("{0} was successfully uploaded to s3!!".format(s3_file))
        return True
    except FileNotFoundError:
        print("The file was not found")
        return False
    except NoCredentialsError:
        print("Credentials not available")
        return False
