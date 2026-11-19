
package pl.kubix1011.antileak;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class VisibilityManager {

    private final AntiLeak plugin;
    private final File file;
    private FileConfiguration config;

    public VisibilityManager(AntiLeak plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
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
