package zm.mc.impl.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatService {
    public static final ChatService INSTENCE = new ChatService();

    private ChatService(){

    }

    /**
     * <pre>
     * Send message to the player.
     * return error message when send failed.
     * retrun null when send succeed.
     * </pre>
     */
    public String sendTo(String playerName,String message){
        Player targetPlayer;
        targetPlayer = Bukkit.getPlayerExact(playerName);
        if(targetPlayer == null ){
            return "Player Not Found:" + playerName;
        }
        
        targetPlayer.sendMessage(message);
        return null;
    }

    public void sendToAll(String msg){
        Bukkit.broadcastMessage(msg);
    }

}
