package zm.mc.plugin.commands;

import org.bukkit.command.CommandExecutor;

import zm.mc.plugin.CainBuilderPlugin;

public abstract  class AbsCainCommand implements CommandExecutor{
    
    protected final CainBuilderPlugin plugin;

    public AbsCainCommand(CainBuilderPlugin plugin) {
        this.plugin = plugin;
    }
}
