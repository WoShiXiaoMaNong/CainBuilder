package zm.mc.plugin.commands;

import java.util.List;

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
                String commandName = commandAnnotation.name();
                try {
                    Object commandInstance = cls.getConstructor(CainBuilderPlugin.class).newInstance(plugin);
                    
                    plugin.getCommand(commandName).setExecutor((AbsCainCommand) commandInstance);
                    logger.info("Registered command: " + commandName + "\twith executor " + cls.getName());
                } catch (Exception e) {
                    logger.severe("Failed to register command: " + commandName + " with executor " + e.getMessage());
                    break;
                }
            }
        }

    }


    //Scan for more commands and register them here in the future

}
