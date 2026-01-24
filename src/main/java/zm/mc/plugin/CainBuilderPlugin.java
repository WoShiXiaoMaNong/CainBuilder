package zm.mc.plugin;


import org.bukkit.plugin.java.JavaPlugin;

import zm.mc.common.LoggerUtil;
import zm.mc.plugin.commands.CommandRegister;


public class CainBuilderPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Cain Builder has been enabled!");
        LoggerUtil.init(this);
        CommandRegister.registerCommands(this);
    }


    
}
