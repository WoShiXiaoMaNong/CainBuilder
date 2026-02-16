package zm.mc.impl.service.mq;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.util.LoggerUtil;

public class CainMqttCallback implements MqttCallback{

    private final CainBuilderPlugin plugin;
    private Gson gson;

    public CainMqttCallback( CainBuilderPlugin plugin){
        this.plugin = plugin;
        this.gson = new Gson();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        //do nothing
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String clientId = plugin.getConfig().getString("clientId");
        String payload = new String(mqttMessage.getPayload());
        if( payload.isEmpty()){
            return;
        }
        MqttMsg msg = this.parse(payload);
        if( clientId!= null &&clientId.equals(msg.getClientId())){
            return;
        }

        String message = String.format("%s :%s", msg.getSender(),msg.getMsg());
        Bukkit.broadcastMessage(ChatColor.GOLD + "[外部] " + ChatColor.WHITE + message);
    }

    private MqttMsg parse(String payload){
        MqttMsg msg;
        try {
            msg = this.gson.fromJson(payload, MqttMsg.class);
        } catch (Exception e) {
            LoggerUtil.INSTANCE.severe("parse error:" + payload);
            LoggerUtil.INSTANCE.severe("parse error:" + e.getMessage());
            msg = new MqttMsg("Error",payload);
        }
        return msg;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //do nothing
    }
}