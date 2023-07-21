package net.starly.banitem.listener;

import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.manager.BanItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (event.getPlayer().hasPermission("starly.banitem.bypass")) return;

        if (!BanItemManager.getInstance().isBanned(event.getItem(), event.getPlayer().getWorld())) return;

        MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "itemBanned").ifPresent(event.getPlayer()::sendMessage);
        event.setCancelled(true);
    }
}
