
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
        saveDefaultConfig();
        reloadConfig();

        PacketEvents.getAPI().init();

        visibilityManager = new VisibilityManager(this);
        visibilityManager.load();

        PacketEvents.getAPI().getEventManager().registerListener(
            new AntiLeakPacketListener(this)
        );

        getServer().getPluginManager().registerEvents(
            new PlayerListener(this),
            this
        );

        if (getCommand("antileak") == null) {
            getLogger().severe("Command antileak is missing!");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("antileak").setExecutor(
            new AntiLeakCommand(this)
        );

        getLogger().info("--------------------------------");
        getLogger().info("AntiLeak enabled!");
        getLogger().info("Configuration loaded!");
        getLogger().info("PacketEvents initialized!");
        getLogger().info("VisibilityManager loaded!");
        getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {
        if (visibilityManager != null) {
            visibilityManager.save();
        }

        PacketEvents.getAPI().terminate();

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
