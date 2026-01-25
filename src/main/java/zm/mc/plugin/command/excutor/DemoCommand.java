package zm.mc.plugin.command.excutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;
import zm.mc.plugin.command.AbsCainCommandExecutor;


@CainCommand(
    name = "cain.demo", 
    commandDescription = "Demo command for CainBuilderPlugin",
    permissionDefault = PermissionDefault.OP, permisstionDescription = "Permission to use /cain.demo command")
public class DemoCommand extends AbsCainCommandExecutor{

 
	public DemoCommand(CainBuilderPlugin plugin) {
		super(plugin);
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       
            if (sender == null || !(sender instanceof Player)) {
                return true;
            } 
                
            Player player = (Player) sender;

            if( this.hasPermission(sender)){
                player.sendMessage(player.getName() + " executed /cain.demo command1234.");
            } else {
                player.sendMessage("You do not have permission to execute this command.1234");
            }
                
            return true;
   
    }


}
