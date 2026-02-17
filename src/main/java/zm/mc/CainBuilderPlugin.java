package zm.mc;


import org.bukkit.plugin.java.JavaPlugin;

import zm.mc.core.CainBuilderRegister;
import zm.mc.core.util.LoggerUtil;


public class CainBuilderPlugin extends JavaPlugin {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;
    @Override
    public void onEnable() {
        getLogger().info("Cain Builder has been enabled! Start to init the plugin...");
        LoggerUtil.init(this);
        logger.info("Logger initialized.");
        logger.info("*******************Register Commands****************");
        logger.info("Registering commands...");
        CainBuilderRegister.registerCommands(this);  // <<<< register commands 
        logger.info("All commands registered.");
        
        logger.info("********************Register Events*****************");
        logger.info("Registering Events...");
        CainBuilderRegister.registerEvents(this); // <<<< register events 
        logger.info("All Events registered.");
        logger.info("Cain Builder plugin initialization complete.");
        logger.info("****************************************************");
        
    }


    
}
