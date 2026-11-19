
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
        boolean defaultEnabled = plugin.getConfig()
            .getBoolean("settings.default-enabled", true);

        return config.getBoolean(
            "players." + uuid,
            defaultEnabled
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
            int distance = plugin.getConfig()
                .getInt("settings.view-distance", 2);

            player.setSendViewDistance(
                Math.max(2, distance)
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

