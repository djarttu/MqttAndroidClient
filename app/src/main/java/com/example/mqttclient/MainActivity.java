package com.example.mqttclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String url2="";
    Button btnAuki;
    Button btnKiinni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.auki).setOnClickListener(this);
        findViewById(R.id.kiinni).setOnClickListener(this);

        btnAuki = findViewById(R.id.auki);
        btnKiinni = findViewById(R.id.kiinni);

        btnAuki.setEnabled(false);
        btnKiinni.setEnabled(false);

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
                url2=url+mqttMessage.toString();
                setImage(url2);

                btnAuki.setEnabled(true);
                btnKiinni.setEnabled(true);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
    public void setImage(String osoite){
        new DownLoadImageTask((ImageView)findViewById(R.id.image)).execute(url2);
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
            btnAuki.setEnabled(false);
            btnKiinni.setEnabled(false);
        }
        if(v.getId() == R.id.kiinni){
            try {
                handleDoor(0);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            btnAuki.setEnabled(false);
            btnKiinni.setEnabled(false);
        }
    }
}