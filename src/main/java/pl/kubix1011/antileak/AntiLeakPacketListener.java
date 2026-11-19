
package pl.kubix1011.antileak;

import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.Column;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;

public final class AntiLeakPacketListener extends PacketListenerAbstract {

    private final AntiLeak plugin;

    public AntiLeakPacketListener(AntiLeak plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (!plugin.getVisibilityManager().isEnabled(
                player.getUniqueId())) {
            return;
        }

        if (event.getPacketType().equals(
                PacketType.Play.Server.CHUNK_DATA)) {

            handleChunkData(event);
            return;
        }

        if (event.getPacketType().equals(
                PacketType.Play.Server.BLOCK_CHANGE)) {

            handleBlockChange(event);
            return;
        }

        if (event.getPacketType().equals(
                PacketType.Play.Server.MULTI_BLOCK_CHANGE)) {

            handleMultiBlockChange(event);
        }
    }

    private void handleChunkData(PacketSendEvent event) {

        WrapperPlayServerChunkData packet =
                new WrapperPlayServerChunkData(event);

        Column column = packet.getColumn();

        if (column == null) {
            return;
        }

        BaseChunk[] chunks = column.getChunks();

        if (chunks == null) {
            return;
        }

        for (BaseChunk chunk : chunks) {

            if (chunk == null || chunk.isEmpty()) {
                continue;
            }

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {

                        WrappedBlockState original =
                                chunk.get(x, y, z);

                        if (original == null) {
                            continue;
                        }

                        WrappedBlockState replacement =
                                createReplacement(original);

                        if (replacement != null) {
                            chunk.set(x, y, z, replacement);
                        }
                    }
                }
            }
        }

        packet.setColumn(column);
        packet.write();
    }

    private void handleBlockChange(PacketSendEvent event) {

        WrapperPlayServerBlockChange packet =
                new WrapperPlayServerBlockChange(event);

        WrappedBlockState original = packet.getBlockState();

        if (original == null) {
            return;
        }

        WrappedBlockState replacement =
                createReplacement(original);

        if (replacement == null) {
            return;
        }

        packet.setBlockState(replacement);
        packet.write();
    }

    private void handleMultiBlockChange(PacketSendEvent event) {

        WrapperPlayServerMultiBlockChange packet =
                new WrapperPlayServerMultiBlockChange(event);

        WrapperPlayServerMultiBlockChange.EncodedBlock[] blocks =
                packet.getBlocks();

        if (blocks == null || blocks.length == 0) {
            return;
        }

        boolean changed = false;

        for (WrapperPlayServerMultiBlockChange.EncodedBlock block : blocks) {

            if (block == null) {
                continue;
            }

            WrappedBlockState original =
                    block.getBlockState(packet.getClientVersion());

            if (original == null) {
                continue;
            }

            WrappedBlockState replacement =
                    createReplacement(original);

            if (replacement != null) {
                block.setBlockState(replacement);
                changed = true;
            }
        }

        if (changed) {
            packet.setBlocks(blocks);
            packet.write();
        }
    }

    private WrappedBlockState createReplacement(
            WrappedBlockState original) {

        String state = original.toString();

        if (state == null || state.isEmpty()) {
            return null;
        }

        int propertiesIndex = state.indexOf('[');

        String blockName = propertiesIndex >= 0
                ? state.substring(0, propertiesIndex)
                : state;

        String properties = propertiesIndex >= 0
                ? state.substring(propertiesIndex)
                : "";

        if (blockName.startsWith("minecraft:")) {
            blockName = blockName.substring(10);
        }

        Material material = Material.matchMaterial(blockName);

        if (material == null || !BlockMasker.shouldMask(material)) {
            return null;
        }

        Material replacement =
                BlockMasker.getReplacementMaterial(material);

        String replacementState =
                replacement.name().toLowerCase(Locale.ROOT);

        if (replacement == Material.STONE_SLAB
                || replacement == Material.STONE_STAIRS) {

            replacementState += properties;
        }

        return WrappedBlockState.getByString(replacementState);
    }
}

