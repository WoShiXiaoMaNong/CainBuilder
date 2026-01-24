package zm.mc.common;

import zm.mc.plugin.CainBuilderPlugin;

public class LoggerUtil {
    private static CainBuilderPlugin plugin;

    public static final LoggerUtil INSTANCE = new LoggerUtil();

    public static void init(CainBuilderPlugin aPlugin) {
		plugin = aPlugin;
	}

	private LoggerUtil() {
	}


    /**
     * Log a error message
     * @param message
     */
    public void severe(String message) {
        plugin.getLogger().severe(message);
    }

    public void info(String message) {
        plugin.getLogger().info(message);
    }

    public void warn(String message) {
        plugin.getLogger().warning(message);
    }

}
