package zm.mc.impl.service.mq;


import org.bukkit.ChatColor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.util.LoggerUtil;
import zm.mc.core.util.StringUtils;
import zm.mc.impl.service.ChatService;

public class CainMqttCallback implements MqttCallback{

    private final CainBuilderPlugin plugin;
    private final Gson gson;

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
            return; // send by the server itself, ignore
        }
         if( msg.getTargetClientId() != null && clientId != null && !clientId.equals(msg.getTargetClientId())){
            return; // not send to me
        }
      
        if(StringUtils.isEmpty(msg.getReceiver())){
            this.publicChat(msg);
        }else{
            this.sendToPlayer(msg);
        }
        
    }

    private void publicChat(MqttMsg msg){
        String message = String.format("%s :%s", msg.getSender(),msg.getMsg());
        ChatService.INSTENCE.sendToAll(ChatColor.GOLD + "[" + msg.getClientId() + "] " + ChatColor.WHITE + message);
    }

    private void sendToPlayer(MqttMsg msg){
        String message = String.format("%s :%s", msg.getSender(),msg.getMsg());
        String errorMsg = ChatService.INSTENCE.sendTo( msg.getReceiver(), ChatColor.GOLD + "["+ msg.getClientId() +"] " + ChatColor.WHITE + message);
        if( errorMsg != null){
            LoggerUtil.INSTANCE.severe(errorMsg);
            //tdb. some error message should be send to the sender back.
        }
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