
package pl.kubix1011.antileak;

import org.bukkit.Material;
import org.bukkit.block.Block;

public final class BlockMasker {

    private BlockMasker() {
    }

    public static boolean shouldMask(Material material) {

        if (material.isAir()) {
            return false;
        }

        if (material == Material.WATER
                || material == Material.LAVA) {
            return false;
        }

        if (material.isInteractable()) {
            return false;
        }

        return true;
    }

    public static Material getReplacementMaterial() {
        return Material.STONE;
    }

    public static boolean shouldMask(Block block) {
        return shouldMask(block.getType());
    }
}
