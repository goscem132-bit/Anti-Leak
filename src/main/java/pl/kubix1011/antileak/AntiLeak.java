
package pl.kubix1011.antileak;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

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

        getServer().getPluginManager().registerEvents(
            new PlayerListener(this),
            this
        );

        if (getCommand("antileak") == null) {
            getLogger().severe(
                "Command antileak is missing!"
            );

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("antileak").setExecutor(
            new AntiLeakCommand(this)
        );

        getLogger().info("AntiLeak enabled!");
        getLogger().info("VisibilityManager loaded!");
        getLogger().info("PlayerListener registered!");
        getLogger().info("PacketEvents initialized!");
    }

    @Override
    public void onDisable() {
        if (visibilityManager != null) {
            visibilityManager.save();
        }

        instance = null;

        PacketEvents.getAPI().terminate();

        getLogger().info("AntiLeak disabled!");
    }

    public static AntiLeak getInstance() {
        return instance;
    }

    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }
}
