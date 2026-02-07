package zm.mc.core.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.SimplePluginManager;

import zm.mc.CainBuilderPlugin;
import zm.mc.core.annotation.CainCommand;
import zm.mc.core.util.ClassScanner;
import zm.mc.core.util.LoggerUtil;

public class CommandRegister {
    private static final LoggerUtil logger = LoggerUtil.INSTANCE;

    private static final String COMMAND_PACKAGE_NAME_CFG_KEY = "command-package";
    private static final String EVENT_PACKAGE_NAME_CFG_KEY = "event-package";

    private CommandRegister() {
        // Private constructor to prevent instantiation
    }

    public static void registerEvents(CainBuilderPlugin plugin){
        logger.info("Command registration initiated. Getting command package from config.yml with key: " + EVENT_PACKAGE_NAME_CFG_KEY);
        String eventPackage = plugin.getConfig().getString(EVENT_PACKAGE_NAME_CFG_KEY);
        logger.info("Commands register start :" + eventPackage);

        List<Class<?>> eventClasses = ClassScanner.getClasses(eventPackage);
        logger.info("Found " + eventClasses.size() + " classes in package " + eventPackage);

       
        int eventCount = 0;
        for(Class<?> cls : eventClasses) {
            // check if the cls is  implements CommandExecutor
            if( !Listener.class.isAssignableFrom(cls) ){
                logger.warn("Class " + cls.getName() + " does not implement " + Listener.class.getSimpleName() + ", skipping.");
                continue;
            }
            try{
                Constructor<?> constructor = cls.getDeclaredConstructor();
                Listener listener = (Listener) constructor.newInstance();
                plugin.getServer().getPluginManager().registerEvents(listener, plugin);
                logger.info("Class " + cls.getName() + " Register done.");
            }catch(Exception e){
                logger.severe("Create listener error!" + e.getMessage());
            }
            eventCount++;
        }
        logger.info("Event register finished. Total registered events: " + eventCount);

    }

    public static void registerCommands(CainBuilderPlugin plugin) {
        logger.info("Command registration initiated. Getting command package from config.yml with key: " + COMMAND_PACKAGE_NAME_CFG_KEY);
        String commandPackage = plugin.getConfig().getString(COMMAND_PACKAGE_NAME_CFG_KEY);
        logger.info("Commands register start :" + commandPackage);

        List<Class<?>> commandClasses = ClassScanner.getClasses(commandPackage);
        logger.info("Found " + commandClasses.size() + " classes in package " + commandPackage);

        logger.info("Retrieving CommandMap from SimplePluginManager.");
        CommandMap commandMap = getCommandMap(plugin);
        if (commandMap == null) {
            logger.severe("CommandMap is null, cannot register command " );
            throw new RuntimeException("Get CommandMap error!");
        }

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
                    doRegisterCommand(plugin,  (AbsCainCommandExecutor) commandInstance,commandAnnotation, commandMap);
                    commandCount++;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    logger.severe("Failed to register command: " + commandAnnotation.name() + " with executor " + e.getMessage());
                    break;
                }
            }
        }
        logger.info("Commands register finished. Total registered commands: " + commandCount);

    }


    private static void doRegisterCommand(CainBuilderPlugin plugin, AbsCainCommandExecutor commandExecutor,CainCommand commandAnnotation,CommandMap commandMap) {
        String commandName = commandExecutor.getCommandName();

        // Register permission
        String perDesc = commandAnnotation.permisstionDescription();
        PermissionDefault permDefault = commandExecutor.getPermissionDefault();
        Permission perm = new Permission(commandName, perDesc,permDefault);
        plugin.getServer().getPluginManager().addPermission(perm);

        // Register command dynamically
        PluginCommand dynamicCommand = createPluginCommandWithReflect( commandName,plugin);
        dynamicCommand.setName(commandName);
        dynamicCommand.setDescription( commandAnnotation.commandDescription() );
        dynamicCommand.setUsage( commandAnnotation.usage() );
        String[] aliases = commandAnnotation.aliases();
        if( aliases != null && aliases.length >0 ){
            dynamicCommand.setAliases( List.of(aliases) );
        }
        boolean registered = commandMap.register("_",dynamicCommand);
        if(!registered){
            logger.severe("Failed to register command: " + commandName + " with executor " + commandExecutor.getClass().getName());
            return;
        }
       
        // Set the executor for the command
        PluginCommand  pluginCommand = plugin.getCommand(commandName);
        if( pluginCommand == null ){
            logger.severe("Failed to get PluginCommand for: " + commandName + " after registration.");
            return;
        }
        pluginCommand.setExecutor(commandExecutor);
        logger.info("Registered command: " + commandName + "\twith executor " + commandExecutor.getClass().getName());
    }



    private static CommandMap getCommandMap(CainBuilderPlugin plugin) {
        CommandMap commandMap = null;
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap)(field.get(plugin.getServer().getPluginManager()));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            logger.severe("Failed to get CommandMap: " + e.getMessage());
        }
        return commandMap;
        
    }

  private static PluginCommand createPluginCommandWithReflect(String name, CainBuilderPlugin plugin) {
        try {
            
            java.lang.reflect.Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, org.bukkit.plugin.Plugin.class);
            
          
            boolean originalAccessible = constructor.canAccess(null);
            
          
            constructor.setAccessible(true);
            
         
            PluginCommand command = constructor.newInstance(name, plugin);
            
         
            constructor.setAccessible(originalAccessible);
            
            return command;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.severe("Failed to create PluginCommand for " + name + ": " + e.getMessage());
            logger.severe("Maybe due to incompatible Bukkit/Spigot version.Please check the PluginCommand constructor in your server version.");
            return null;
        }
        }

}
