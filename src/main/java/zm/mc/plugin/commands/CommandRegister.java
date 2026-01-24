package zm.mc.plugin.commands;

import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.common.ClassScanner;
import zm.mc.common.LoggerUtil;
import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;

public class CommandRegister {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;

    private CommandRegister() {
        // Private constructor to prevent instantiation
    }

    public static void registerCommands(CainBuilderPlugin plugin) {
        String commandPackage = "zm.mc.plugin.commands";
        logger.info("Commands register start :" + commandPackage);

        List<Class<?>> commandClasses = ClassScanner.getClasses(commandPackage);
        logger.info("Found " + commandClasses.size() + " classes in package " + commandPackage);
        for(Class<?> cls : commandClasses) {
            // check if the cls is  implements CommandExecutor
            if( !AbsCainCommand.class.isAssignableFrom(cls) ){
                logger.warn("Class " + cls.getName() + " does not implement AbsCainCommand.");
                continue;
            }

            if(cls.isAnnotationPresent(CainCommand.class)) {
                CainCommand commandAnnotation = cls.getAnnotation(CainCommand.class);
               
                try {
                    Object commandInstance = cls.getConstructor(CainBuilderPlugin.class).newInstance(plugin);
                    doRegisterCommand(plugin,  (AbsCainCommand) commandInstance,commandAnnotation);
                } catch (Exception e) {
                    logger.severe("Failed to register command: " + commandAnnotation.name() + " with executor " + e.getMessage());
                    break;
                }
            }
        }

    }


    private static void doRegisterCommand(CainBuilderPlugin plugin, AbsCainCommand commandExecutor,CainCommand commandAnnotation) {
        String commandName = commandAnnotation.name();

        // Register permission
        String perDesc = commandAnnotation.permisstionDescription();
        PermissionDefault permDefault = commandAnnotation.permissionDefault();
        Permission perm = new Permission(commandName, perDesc,permDefault);
        plugin.getServer().getPluginManager().addPermission(perm);

        // Register command executor
        

        plugin.getCommand(commandName).setExecutor(commandExecutor);
        logger.info("Registered command: " + commandName + "\twith executor " + commandExecutor.getClass().getName());
    }

}
