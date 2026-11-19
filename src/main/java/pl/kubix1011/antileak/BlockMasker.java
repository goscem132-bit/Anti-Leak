
package pl.kubix1011.antileak;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class BlockMasker {

    private final AntiLeak plugin;

    public BlockMasker(AntiLeak plugin) {
        this.plugin = plugin;
    }

    public boolean shouldMask(Player player) {
        return plugin.getVisibilityManager()
                .isEnabled(player);
    }

    public Material getReplacementMaterial() {
        return Material.STONE;
    }
}
