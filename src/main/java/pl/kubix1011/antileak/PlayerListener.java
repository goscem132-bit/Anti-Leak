
package pl.kubix1011.antileak;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {

    private final AntiLeak plugin;

    public PlayerListener(AntiLeak plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(
            plugin,
            () -> plugin.getVisibilityManager()
                .applyViewDistance(player)
        );
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(
            plugin,
            () -> plugin.getVisibilityManager()
                .applyViewDistance(player)
        );
    }
}
