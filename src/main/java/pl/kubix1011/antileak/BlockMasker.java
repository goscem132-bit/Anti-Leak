
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

        // Fluids remain unchanged
        if (material == Material.WATER
                || material == Material.LAVA) {
            return false;
        }

        // Fire remains unchanged
        if (material == Material.FIRE
                || material == Material.SOUL_FIRE) {
            return false;
        }

        String name = material.name();

        // Signs remain unchanged
        if (name.endsWith("_SIGN")
                || name.endsWith("_HANGING_SIGN")) {
            return false;
        }

        // Doors remain unchanged
        if (name.endsWith("_DOOR")) {
            return false;
        }

        // Fences remain unchanged
        if (name.endsWith("_FENCE")
                || name.endsWith("_FENCE_GATE")) {
            return false;
        }

        // Functional blocks remain unchanged
        if (material.isInteractable()) {
            return false;
        }

        // All remaining blocks are processed
        return true;
    }

    public static Material getReplacementMaterial(
            Material original
    ) {

        if (!shouldMask(original)) {
            return original;
        }

        // Non-solid blocks become invisible
        if (!original.isSolid()) {
            return Material.AIR;
        }

        String name = original.name();

        // Slabs become stone slabs
        if (name.endsWith("_SLAB")) {
            return Material.STONE_SLAB;
        }

        // Stairs become stone stairs
        if (name.endsWith("_STAIRS")) {
            return Material.STONE_STAIRS;
        }

        // Everything else becomes stone
        return Material.STONE;
    }

    public static boolean shouldMask(Block block) {
        return shouldMask(block.getType());
    }

    public static Material getReplacementMaterial(Block block) {
        return getReplacementMaterial(block.getType());
    }
}
