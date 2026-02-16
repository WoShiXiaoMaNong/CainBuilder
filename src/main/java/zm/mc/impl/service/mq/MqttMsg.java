package zm.mc.impl.service.mq;

public class MqttMsg {
    private String clientId;
    private String sender;
    private String msg;

    public MqttMsg( String sender, String msg) {
        this.msg = msg;
        this.sender = sender;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
