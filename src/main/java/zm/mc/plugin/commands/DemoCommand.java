package zm.mc.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;


@CainCommand(name = "cain.demo")
public class DemoCommand  implements CommandExecutor{

    private final CainBuilderPlugin plugin;

	public DemoCommand(CainBuilderPlugin plugin) {
		this.plugin = plugin;
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       
            if (sender == null || !(sender instanceof Player)) {
                return true;
            } 
                
            Player player = (Player) sender;

            if( player.hasPermission("cain.demo")){
                plugin.getLogger().info(player.getName() + " executed /cain.demo command.");
                player.sendMessage(player.getName() + " executed /cain.demo command.");
            } else {
                player.sendMessage("You do not have permission to execute this command.");
            }
                
            return true;
   
    }

}
