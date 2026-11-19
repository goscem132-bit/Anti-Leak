
package pl.kubix1011.antileak;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;

public final class BlockDataMapper {

    private BlockDataMapper() {
    }

    public static BlockData map(BlockData original) {

        Material material = original.getMaterial();

        if (!BlockMasker.shouldMask(material)) {
            return original.clone();
        }

        Material replacement =
            BlockMasker.getReplacementMaterial(material);

        BlockData result = replacement.createBlockData();

        // Preserve slab position and waterlogging
        if (original instanceof Slab oldSlab
                && result instanceof Slab newSlab) {

            newSlab.setType(oldSlab.getType());
            newSlab.setWaterlogged(oldSlab.isWaterlogged());
        }

        // Preserve stairs orientation and shape
        if (original instanceof Stairs oldStairs
                && result instanceof Stairs newStairs) {

            newStairs.setFacing(oldStairs.getFacing());
            newStairs.setHalf(oldStairs.getHalf());
            newStairs.setShape(oldStairs.getShape());
            newStairs.setWaterlogged(oldStairs.isWaterlogged());
        }

        return result;
    }
}
