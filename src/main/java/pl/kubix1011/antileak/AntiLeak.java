
package pl.kubix1011.antileak;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public final class AntiLeak extends JavaPlugin {

    private static AntiLeak instance;

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

        getLogger().info("================================");
        getLogger().info("AntiLeak successfully enabled!");
        getLogger().info("PacketEvents initialized!");
        getLogger().info("Minecraft target: 1.21.11");
        getLogger().info("================================");
    }

    @Override
    public void onDisable() {
        if (PacketEvents.getAPI() != null) {
            PacketEvents.getAPI().terminate();
        }

        getLogger().info("AntiLeak disabled!");

        instance = null;
    }

    public static AntiLeak getInstance() {
        return instance;
    }
}
