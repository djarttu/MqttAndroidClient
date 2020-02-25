package com.example.mqttclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MqttHelper mqttHelper;
    String url= "https://iotjuttukoulu.s3.amazonaws.com/";
    TextView dataReceived;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.auki).setOnClickListener(this);
        dataReceived = (TextView) findViewById(R.id.osoite);

        startMqtt();
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived.setText(mqttMessage.toString());
                url=url+mqttMessage.toString();
                setImage(url);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
    public void setImage(String osoite){
        new DownLoadImageTask((ImageView)findViewById(R.id.image)).execute(url);
    }
    public void handleDoor(int i) throws MqttException {
        if(i==0)
            mqttHelper.publishMessage("kiinni");
        if(i==1)
            mqttHelper.publishMessage("avaa");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.auki) {
            try {
                handleDoor(1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}