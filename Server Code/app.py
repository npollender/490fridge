import os

from botocore.config import Config
from botocore.session import Session
from google.cloud import vision
from google.cloud.vision import enums
from google.cloud.vision import types

from connect import execute_query

s = Session()
s3 = s.create_client('s3', config=Config(connect_timeout=5, read_timeout=60, retries={'max_attempts': 2}))


def lambda_handler(event, context):
    print('Loading function')

    # Get the object from the event and show its content type
    bucket = "smart-fridge-pics"
    key = "image.jpg"
    try:
        img_response = s3.get_object(Bucket=bucket, Key=key)
        # print("CONTENT TYPE: " + img_response['ContentType'])
        # return response['ContentType']
    except Exception as e:
        print(e)
        print(
            'Error getting object {} from bucket {}. Make sure they exist and your bucket is in the same region as '
            'this function.'.format(key, bucket))
        raise e

    # obtain the credentials
    os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "vision_api.json"

    # Instantiates a client
    vision_client = vision.ImageAnnotatorClient()

    content = img_response['Body'].read()

    image_o = types.Image(content=content)

    features = [
        {"type": enums.Feature.Type.LOGO_DETECTION},
        {"type": enums.Feature.Type.LABEL_DETECTION},
        {"type": enums.Feature.Type.TEXT_DETECTION},
        {"type": enums.Feature.Type.OBJECT_LOCALIZATION},
    ]

    request = types.AnnotateImageRequest(image=image_o, features=features)

    # Performs image detection and annotation for an image
    response = vision_client.annotate_image(request)

    # print(objects)
    print("\n***Output from text_annotations****\n")
    texts1 = [text.description for text in response.text_annotations]
    texts1 = list(set(texts1))  # removes duplicates
    print(texts1)

    print("\n***Output from localized_object_annotations****\n")
    texts2 = [text.name for text in response.localized_object_annotations]
    texts2 = list(set(texts2))  # removes duplicates
    print(texts2)

    print("\n***Output from label_annotations****\n")
    texts3 = [text.description for text in response.label_annotations]
    texts3 = list(set(texts3))  # removes duplicates
    print(texts3)

    print("\n***Output from logo_annotations****\n")
    texts4 = [text.description for text in response.logo_annotations]
    texts4 = list(set(texts4))  # removes duplicates
    print(texts4)

    all_things_found = texts1 + texts2 + texts3 + texts4

    if response.error.message:
        raise Exception("{}\nFor more info on error messages, check: https://cloud.google.com/apis/design/errors".format(
                response.error.message))

    # insert the result into RDS
    query = """UPDATE camera_tb SET things_found = %s WHERE image_desc = %s"""
    data = (all_things_found, key)
    execute_query(query, data)
