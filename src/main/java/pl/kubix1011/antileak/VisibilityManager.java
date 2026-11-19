
package pl.kubix1011.antileak;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class VisibilityManager {

    private final AntiLeak plugin;
    private final File file;

    private FileConfiguration config;

    private static final int ANTILEAK_DISTANCE = 2;

    public VisibilityManager(AntiLeak plugin) {
        this.plugin = plugin;
        this.file = new File(
            plugin.getDataFolder(),
            "players.yml"
        );
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isEnabled(UUID uuid) {
        return config.getBoolean(
            "players." + uuid,
            true
        );
    }

    public boolean isEnabled(Player player) {
        return isEnabled(player.getUniqueId());
    }

    public void setEnabled(UUID uuid, boolean enabled) {
        config.set("players." + uuid, enabled);
        save();
    }

    public void setEnabled(Player player, boolean enabled) {
        setEnabled(player.getUniqueId(), enabled);
        applyViewDistance(player);
    }

    public void applyViewDistance(Player player) {
        if (isEnabled(player)) {
            player.setSendViewDistance(
                ANTILEAK_DISTANCE
            );
        } else {
            player.setSendViewDistance(-1);
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().log(
                Level.SEVERE,
                "Could not save players.yml!",
                exception
            );
        }
    }
}

