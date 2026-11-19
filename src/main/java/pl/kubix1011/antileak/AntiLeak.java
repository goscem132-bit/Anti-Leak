
package pl.kubix1011.antileak;

import org.bukkit.plugin.java.JavaPlugin;

public final class AntiLeak extends JavaPlugin {

    private static AntiLeak instance;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("--------------------------------");
        getLogger().info("       AntiLeak ENABLED");
        getLogger().info("       Version: 1.0.0");
        getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiLeak disabled.");
        instance = null;
    }

    public static AntiLeak getInstance() {
        return instance;
    }
}
