package zm.mc.impl.command.excutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.annotation.CainCommand;
import zm.mc.core.command.AbsCainCommandExecutor;
import zm.mc.core.util.LoggerUtil;
import zm.mc.impl.service.MqttService;


@CainCommand(
    name = "cain.chat", 
    commandDescription = "Start cain chat(Connect the mqtt server)",
    usage = "/cain.chat {tcp://mqtt.host.name:port} {userName} {pwd}",
    aliases = {"cc", "cchat"},
    permissionDefault = PermissionDefault.OP, 
    permisstionDescription = "Permission to use /cain.chat command")
public class PlayerChatCommand extends AbsCainCommandExecutor{

 
	public PlayerChatCommand(CainBuilderPlugin plugin) {
		super(plugin);
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
       
            if (sender == null || !(sender instanceof Player)) {
                return true;
            } 
            Player player = (Player) sender;
            
            if(args == null || args.length < 3 ){
                return true;
            }
            String url = args[0];
            String userName = args[1];
            String pwd = args[2];
            try{
                if( this.hasPermission(sender)){
                    player.sendMessage("Start to init Mqtt");
                    MqttService.instance.init(plugin, url,userName,pwd);
                    player.sendMessage("Mqtt start succeed!");
                } else {
                    player.sendMessage("You do not have permission to execute this command.");
                }
            }catch(Exception e){
                LoggerUtil.INSTANCE.severe("init mqtt error." + e.getMessage());
            }
                
            return true;
   
    }


}
