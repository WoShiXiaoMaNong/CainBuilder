package zm.mc.impl.command.excutor;

import static org.bukkit.Bukkit.getServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.annotation.CainCommand;
import zm.mc.core.command.AbsCainCommandExecutor;
import zm.mc.impl.service.MqttService;
import zm.mc.impl.service.mq.MqttMsg;


@CainCommand(
    name = "cain.msg", 
    commandDescription = "跨服聊天(公聊)",
    usage = "/cain.msg 服务器id 聊天内容",
    aliases = {"cmsg"},
    permissionDefault = PermissionDefault.TRUE, 
    permisstionDescription = "Permission to use /cain.demo command")
public class CainChat extends AbsCainCommandExecutor{

 
	public CainChat(CainBuilderPlugin plugin) {
		super(plugin);
	}
  
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            
            if( args == null || args.length < 2){
                getServer().getLogger().info("command error! args length is : " + args.length);
                return false;
            }
            String clientId = args[0];
            String msgStr = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
    
            MqttMsg msg = new MqttMsg(sender.getName(),msgStr);
            msg.setTargetClientId(clientId);

            MqttService.instance.send(msg);
            return true;
   
    }


}
