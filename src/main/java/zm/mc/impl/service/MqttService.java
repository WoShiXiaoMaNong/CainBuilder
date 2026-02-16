package zm.mc.impl.service;

import org.eclipse.paho.client.mqttv3.MqttException;

import zm.mc.CainBuilderPlugin;
import zm.mc.impl.service.mq.CainMqttClient;
import zm.mc.impl.service.mq.MqttMsg;

public class MqttService {
    public static final MqttService instance = new MqttService();

    private volatile CainMqttClient mqttClient;

    private MqttService(){

    }

    public void init(CainBuilderPlugin plugin,String url ,String userName ,String mqttPwd) throws MqttException{
        if(this.mqttClient == null){
             this.mqttClient = new CainMqttClient(plugin,url,userName,mqttPwd);
        }else{
            this.mqttClient.reconnect();
        }
       
    }

    public void send(MqttMsg msg){
        if( this.mqttClient == null){
            return;
        }
        this.mqttClient.send(msg);
    }


}
