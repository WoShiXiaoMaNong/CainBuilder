package zm.mc.impl.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerOnlineTimeService{
     public static final PlayerOnlineTimeService instance = new PlayerOnlineTimeService();

     private final Map<UUID,LocalDateTime> playerOnlineTime;

     private PlayerOnlineTimeService(){
        this.playerOnlineTime =  new HashMap<>();
     }
    
     public void playerLogin(Player player){
        if(player == null){
            return;
        }
        playerOnlineTime.put(player.getUniqueId(),LocalDateTime.now());
     }
     
     public void playerLogout(Player player){
        if(player == null){
            return;
        }
        this.playerOnlineTime.remove(player.getUniqueId());
     }

     /**
      * return player online times(Millis)
      */
     public long playerOnlineTime(UUID playerUuid){
        if(playerUuid == null){
            return 0L;
        }
        LocalDateTime playerLoginLocalDateTime = this.playerOnlineTime.get(playerUuid);
        if( playerLoginLocalDateTime == null){
            return 0L;
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(playerLoginLocalDateTime.atZone(ZoneId.systemDefault()).toInstant(), now.atZone(ZoneId.systemDefault()).toInstant());
        return duration.toMillis();
     }

}