package zm.mc.plugin.command;

import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.common.ClassScanner;
import zm.mc.common.LoggerUtil;
import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;

public class CommandRegister {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;

    private static final String COMMAND_PACKAGE_NAME_CFG_KEY = "command-package";

    private CommandRegister() {
        // Private constructor to prevent instantiation
    }

    public static void registerCommands(CainBuilderPlugin plugin) {
        logger.info("Command registration initiated. Getting command package from config.yml with key: " + COMMAND_PACKAGE_NAME_CFG_KEY);
        String commandPackage = plugin.getConfig().getString(COMMAND_PACKAGE_NAME_CFG_KEY);
        logger.info("Commands register start :" + commandPackage);

        List<Class<?>> commandClasses = ClassScanner.getClasses(commandPackage);
        logger.info("Found " + commandClasses.size() + " classes in package " + commandPackage);

  

        int commandCount = 0;
        for(Class<?> cls : commandClasses) {
            // check if the cls is  implements CommandExecutor
            if( !AbsCainCommandExecutor.class.isAssignableFrom(cls) ){
                logger.warn("Class " + cls.getName() + " does not implement " + AbsCainCommandExecutor.class.getSimpleName() + ", skipping.");
                continue;
            }

            if(cls.isAnnotationPresent(CainCommand.class)) {
                CainCommand commandAnnotation = cls.getAnnotation(CainCommand.class);
               
                try {
                    Object commandInstance = cls.getConstructor(CainBuilderPlugin.class).newInstance(plugin);
                    doRegisterCommand(plugin,  (AbsCainCommandExecutor) commandInstance,commandAnnotation);
                    commandCount++;
                } catch (Exception e) {
                    logger.severe("Failed to register command: " + commandAnnotation.name() + " with executor " + e.getMessage());
                    break;
                }
            }
        }
        logger.info("Commands register finished. Total registered commands: " + commandCount);

    }


    private static void doRegisterCommand(CainBuilderPlugin plugin, AbsCainCommandExecutor commandExecutor,CainCommand commandAnnotation) {
        String commandName = commandExecutor.getCommandName();

        // Register permission
        String perDesc = commandAnnotation.permisstionDescription();
        PermissionDefault permDefault = commandExecutor.getPermissionDefault();
        Permission perm = new Permission(commandName, perDesc,permDefault);
        plugin.getServer().getPluginManager().addPermission(perm);

        // Register command executor
  
        // Set the executor for the command
        plugin.getCommand(commandName).setExecutor(commandExecutor);
        logger.info("Registered command: " + commandName + "\twith executor " + commandExecutor.getClass().getName());
    }


}
