package zm.mc.impl.command.excutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.annotation.CainCommand;
import zm.mc.core.command.AbsCainCommandExecutor;
import zm.mc.impl.service.PlayerOnlineTimeService;


@CainCommand(
    name = "cain.onlinetime", 
    commandDescription = "Demo command for get the online time",
    usage = "/cain.onlinetime",
    aliases = {"cot", "ot"},
    permissionDefault = PermissionDefault.TRUE, 
    permisstionDescription = "Permission to use /cain.onlinetime command")
public class PlayerOnelineCommand extends AbsCainCommandExecutor{

 
	public PlayerOnelineCommand(CainBuilderPlugin plugin) {
		super(plugin);
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       
            if (sender == null || !(sender instanceof Player)) {
                return true;
            } 
            Player player = (Player) sender;
           
            if( this.hasPermission(sender)){
                long onlineTime = PlayerOnlineTimeService.instance.playerOnlineTime(player.getUniqueId());
                player.sendMessage(player.getName() + ", 你的在线时长: " + onlineTime/1000 + " (秒)");
            } else {
                player.sendMessage("You do not have permission to execute this command.");
            }
                
            return true;
   
    }


}
