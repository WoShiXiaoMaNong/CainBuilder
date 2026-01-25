package zm.mc;


import org.bukkit.plugin.java.JavaPlugin;

import zm.mc.core.command.CommandRegister;
import zm.mc.core.util.LoggerUtil;


public class CainBuilderPlugin extends JavaPlugin {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;
    @Override
    public void onEnable() {
        getLogger().info("Cain Builder has been enabled! Start to init the plugin...");
        LoggerUtil.init(this);
        logger.info("Logger initialized.");
        
        logger.info("Registering commands...");
        CommandRegister.registerCommands(this);
        logger.info("All commands registered.");

        logger.info("Cain Builder plugin initialization complete.");
    }


    
}
