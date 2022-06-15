from flask import Flask, request
import pandas as pd
from prophet import Prophet
import py_eureka_client.eureka_client as eureka_client
import numpy as np

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

@app.route('/seller/analysis')
def seller_value():
    parameter = request.get_json()
    value_list = parameter["response"]

    ds = []
    y = []
    for value in value_list:
        date = value.get("date")
        y = value.get("value")

        date = date[:4] + "-" + date[4:]
        print("date : ", date)
        print("y", y)
        ds.append(pd.to_datetime(date))
        y.append(y)

    s_ds = pd.Series(ds, name="ds")
    s_y = pd.Series(y, name="y")
    df = pd.DataFrame(s_ds).join(s_y)

    m = Prophet()
    m.fit(df)

    future = m.make_future_dataframe(periods=31)
    forecast = m.predict(future)

    result = int(np.mean(forecast.tail(31)["yhat"].values))
    print("result : ", result)
    return str(result)


if __name__ == '__main__':
    app.run(debug=True, port=rest_port)
