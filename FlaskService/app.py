from flask import Flask
import pandas as pd
import py_eureka_client.eureka_client as eureka_client
# pip install py-eureka-client

rest_port = 5000
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="flask-service",
                   instance_ip="192.168.170.169",
                   instance_port=rest_port)
app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    app.run()
