package net.starly.banitem.listener;

import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.manager.BanItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class ItemCraftListener implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getWhoClicked().hasPermission("starly.banitem.bypass")) return;
        if (!BanItemManager.getInstance().isBanned(event.getRecipe().getResult(), event.getWhoClicked().getWorld())) return;

        MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "itemBanned").ifPresent(event.getWhoClicked()::sendMessage);
        event.setCancelled(true);
    }
}
