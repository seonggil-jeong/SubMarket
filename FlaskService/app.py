from flask import Flask, request
import pandas as pd
from prophet import Prophet
import py_eureka_client.eureka_client as eureka_client

rest_port = 5000
eureka_client.init(eureka_server="http://localhost:8761/eureka",        # Eureka Server Address
                   app_name="flask-service",        # Service Name
                   instance_ip="127.0.0.1",     # 등록될 Ip
                   instance_port=rest_port)     # prot

rest_port = 5000
app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    app.run()

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    app.run()
