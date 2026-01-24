package zm.mc.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;


@CainCommand(name = "cain.demo", permissionDefault = PermissionDefault.OP, permisstionDescription = "Permission to use /cain.demo command")
public class DemoCommand extends AbsCainCommand{

 
	public DemoCommand(CainBuilderPlugin plugin) {
		super(plugin);
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       
            if (sender == null || !(sender instanceof Player)) {
                return true;
            } 
                
            Player player = (Player) sender;

            if( player.hasPermission("cain.demo")){
                player.sendMessage(player.getName() + " executed /cain.demo command.");
            } else {
                player.sendMessage("You do not have permission to execute this command.");
            }
                
            return true;
   
    }

}
