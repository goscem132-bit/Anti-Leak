
package pl.kubix1011.antileak;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;

public final class AntiLeakPacketListener
        extends PacketListenerAbstract {

    private final AntiLeak plugin;

    public AntiLeakPacketListener(AntiLeak plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

        if (event.getPlayer() == null) {
            return;
        }

        // Packet processing will be implemented
        // in the next stage.

    }
}
