package net.starly.banitem.listener;

import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.manager.BanItemManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickUpListener implements Listener {

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) return;
        if (event.getEntity().hasPermission("starly.banitem.bypass")) return;
        if (!BanItemManager.getInstance().isBanned(event.getItem().getItemStack(), event.getEntity().getWorld())) return;

        event.setCancelled(true);
    }
}
