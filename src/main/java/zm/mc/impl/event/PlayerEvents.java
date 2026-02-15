package zm.mc.impl.event;

import java.util.Random;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;
import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import zm.mc.impl.service.PlayerOnlineTimeService;


public class PlayerEvents implements Listener {

    private static final Random random = new Random(System.currentTimeMillis());

    private static final DyeColor[] ALL_COLORS = DyeColor.values();

   

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();

        if( ! (damager.getType().equals(EntityType.PLAYER)) ){
            return;
        }
        
        if(target.getType().equals( EntityType.SHEEP) ){
            event.setDamage(0);
            Sheep sheep = (Sheep)target;
            sheep.setColor(ALL_COLORS[random.nextInt(ALL_COLORS.length)]);
        }
     
  
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerOnlineTimeService.instance.playerLogin(player);
        getServer().getLogger().log(Level.INFO, "Player " + event.getPlayer().getName() + " is logging in<<<<<<<<<<<<<<<<<<<!");
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerOnlineTimeService.instance.playerLogout(player);
        getServer().getLogger().log(Level.INFO, "Player " + event.getPlayer().getName() + " is quit<<<<<<<<<<<<<<<<<<<!");
    }


}
