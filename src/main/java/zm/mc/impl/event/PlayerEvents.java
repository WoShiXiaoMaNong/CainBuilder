package zm.mc.impl.event;

import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class PlayerEvents implements Listener {

    private static final Random random = new Random(System.currentTimeMillis());

    private static final DyeColor[] ALL_COLORS = DyeColor.values();

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        
        if(target.getType().equals( EntityType.SHEEP) ){
            event.setDamage(0);
            Sheep sheep = (Sheep)target;
            sheep.setColor(ALL_COLORS[random.nextInt(ALL_COLORS.length)]);
        }
     
  
    }



}
