package zm.mc.plugin.command;

import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.permissions.PermissionDefault;

import zm.mc.plugin.CainBuilderPlugin;
import zm.mc.plugin.annotation.CainCommand;

public abstract class AbsCainCommandExecutor implements CommandExecutor{
    
    protected final CainBuilderPlugin plugin;

    public AbsCainCommandExecutor(CainBuilderPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract String getUsage();
    public abstract String getDescription();
    public abstract List<String> getAliases();

    public CainCommand getCainCommandAnnotation() {
        return this.getClass().getAnnotation(CainCommand.class);
    }

    public String getCommandName() {
        CainCommand annotation = getCainCommandAnnotation();
        return (annotation != null) ? annotation.name() : null;
    }

    public PermissionDefault getPermissionDefault() {
        CainCommand annotation = getCainCommandAnnotation();
        return (annotation != null) ? annotation.permissionDefault() : null;
    }

    public boolean hasPermission(org.bukkit.command.CommandSender sender) {
        String commandName = getCommandName();
        if (commandName == null) {
            return false;
        }
        return sender.hasPermission(commandName);
    }
}
