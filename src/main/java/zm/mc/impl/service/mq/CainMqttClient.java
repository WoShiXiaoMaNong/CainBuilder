package zm.mc.impl.service.mq;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.util.LoggerUtil;
import zm.mc.core.util.StringUtils;

public class CainMqttClient {

    private MqttClient mqttClient;

    private final CainBuilderPlugin plugin;

    private  int qos;
    private  String topic;
    private String clientId;

    private Gson gson;

    public CainMqttClient(CainBuilderPlugin plugin,String url ,String userName ,String pwd) {
        this.plugin = plugin;
        init(url,userName,pwd);   
    }

    public void close(){
        if(this.mqttClient == null){
            return;
        }
        try {
            this.mqttClient.close();
        } catch (Exception e) {
            LoggerUtil.INSTANCE.severe(e.getMessage());
        }
        
    }
    
    public void reconnect() throws MqttException{
        if(this.mqttClient != null && !this.mqttClient.isConnected()){
            LoggerUtil.INSTANCE.info("Start to reconnect mqtt!");
            this.mqttClient.reconnect();
            LoggerUtil.INSTANCE.info("Mqtt reconnected!");
        }
    }

    public void send(MqttMsg msg) {
        try {
            if(msg == null){
                return;
            }
            msg.setClientId(this.clientId);
            String payload = this.gson.toJson(msg);
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos( this.qos);
            mqttClient.publish( this.topic, message);
        } catch (Exception e) {
            getServer().getLogger().log(Level.WARNING, "Send message error!", e);
        }
    }

    private void init(String url ,String userName ,String pwd) {
        this.gson = new Gson();
        this.qos = plugin.getConfig().getInt("qos");
        this.topic = plugin.getConfig().getString("topic");
        this.clientId = plugin.getConfig().getString("clientId");
        int keepAliveInterval = plugin.getConfig().getInt("keepAliveInterval"); 
        int connectionTimeout = plugin.getConfig().getInt("connectionTimeout");
    
        if( StringUtils.isEmpty(url) ||  StringUtils.isEmpty(clientId) ||  StringUtils.isEmpty(userName) ||  StringUtils.isEmpty(pwd)){
            getServer().getLogger().log(Level.INFO,"[SKIP]No mqtt config.");
            return;
        }
        try {
             this.mqttClient = new MqttClient(url,clientId, new MemoryPersistence());

            // 配置参数信息

            MqttConnectOptions options = new MqttConnectOptions();

            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，

            // 这里设置为true表示每次连接到服务器都以新的身份连接

            options.setCleanSession(true);

            // 设置用户名

            options.setUserName(userName);

            // 设置密码

            options.setPassword(pwd.toCharArray());

            // 设置超时时间 单位为秒

            options.setConnectionTimeout(connectionTimeout);
            options.setAutomaticReconnect(true);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制

            options.setKeepAliveInterval(keepAliveInterval);

            // 连接

            mqttClient.connect(options);

            // 订阅
            mqttClient.subscribe(topic);

            // 设置回调
            mqttClient.setCallback(new CainMqttCallback(this.plugin));
           getServer().getLogger().log(Level.INFO,"MQTT init succeed!");
        } catch (Exception e) {
            getServer().getLogger().log(Level.WARNING,"Mqtt Client Init error",e);
        }

    }

   
}
