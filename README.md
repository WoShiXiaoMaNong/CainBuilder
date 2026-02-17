# CainBuilder
mc plugin.

### 命令说明
#### 1. cain.mqtt (连接mqtt 服务器)
    - /cain.mqtt {tcp://mqtt.host.name:port} {userName} {pwd}
    - 配置文件 在 config.xml
     - 订阅主题
     - 当前服务器的client id (当有多个服务器连接相同主题时，请保证每个mc 服务器的client id 不重复)
>> 注意，连接成功后，在当前服务器中的聊天，会被同步发送到其他连接到相同mqtt 主题的所有服务器。（相当于多服务器公聊）

#### 2. cain.msg (跨服务器聊天)
    - /cain.msg 服务器id 聊天内容
    - 改命令用于发送聊天信息到单个 服务器id
>> 注意 1. 服务器启动后，需要先时用 cain.mqtt 连接 mqtt 服务器.


### 配置说明(config.yml)
#### command-package : 自定义的命令
    - 继承 AbsCainCommandExecutor
    - 使用注解 @CainCommand
        - 用于写明命令的名称，权限等。
> 所有位于 command-package 下面，并且符合上述两点的java类，都会被自动注册为一个指令

#### 指令示例：
```java
package zm.mc.impl.command.excutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.annotation.CainCommand;
import zm.mc.core.command.AbsCainCommandExecutor;


@CainCommand(
    name = "cain.demo", 
    commandDescription = "Demo command for CainBuilderPlugin",
    usage = "/cain.demo",
    aliases = {"cda", "cdemo"},
    permissionDefault = PermissionDefault.OP, 
    permisstionDescription = "Permission to use /cain.demo command")
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
                player.sendMessage(player.getName() + " executed /cain.demo command.");
            } else {
                player.sendMessage("You do not have permission to execute this command.");
            }
            return true;
    }
}
```
>> 这个类位于 command-package 指定的包路径下,并且继承了AbsCainCommandExecutor，而且使用了@CainCommand配置了指令相关的属性，所以它会被自动注册为一个游戏指令，无需任何其他处理。


#### event-package ： 自定义的事件监听器
    - 实现 Listener 接口
    - 使用 @EventHandler 监听事件
> 所有位于 event-package 下面，并且符合上述两点的java类，都会被自动注册未一个事件监听器。
#### 监听器示例
```java
package zm.mc.impl.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
public class PlayerEvents implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        getServer().getLogger().log(Level.INFO, "Player " + event.getPlayer().getName() + " is logging in!");
    }
}
```
>> 这个类位于 event-package 指定的包路径下，所以它会被自动注册为一个事件监听器，无需任何其他处理