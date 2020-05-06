package io.github.phantamanta44.threng.event;

import appeng.util.Platform;
import io.github.phantamanta44.threng.tile.base.IReadyable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Deque;

// essentially just a generalization of the tile init mechanism in AE2's TickHandler
public class TileReadyHandler {

    private final Deque<IReadyable> readyQueue = new ArrayDeque<>();

    public void enqueue(IReadyable tile) {
        if (Platform.isServer()) {
            readyQueue.offer(tile);
        }
    }

    // increased priority guarantees that this runs before AE2's update tick
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (!readyQueue.isEmpty()) {
                readyQueue.pop().onReady();
            }
        }
    }

}
