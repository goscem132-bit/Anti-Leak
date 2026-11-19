
package pl.kubix1011.antileak;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public final class AntiLeak extends JavaPlugin {

    private static AntiLeak instance;

    private VisibilityManager visibilityManager;

    @Override
    public void onLoad() {
        instance = this;

        PacketEvents.setAPI(
            SpigotPacketEventsBuilder.build(this)
        );

        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();

        visibilityManager = new VisibilityManager(this);
        visibilityManager.load();

        if (getCommand("antileak") == null) {
            getLogger().severe(
                "Command antileak is missing from plugin.yml!"
            );

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("antileak").setExecutor(
            new AntiLeakCommand(this)
        );

        getLogger().info("--------------------------------");
        getLogger().info("AntiLeak enabled!");
        getLogger().info("VisibilityManager loaded!");
        getLogger().info("AntiLeak command registered!");
        getLogger().info("Default AntiLeak: ON");
        getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {
        if (visibilityManager != null) {
            visibilityManager.save();
        }

        if (PacketEvents.getAPI() != null) {
            PacketEvents.getAPI().terminate();
        }

        instance = null;

        getLogger().info("AntiLeak disabled!");
    }

    public static AntiLeak getInstance() {
        return instance;
    }

    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }
}
