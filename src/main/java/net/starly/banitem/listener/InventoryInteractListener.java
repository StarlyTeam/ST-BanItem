package net.starly.banitem.listener;

import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.manager.BanItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class InventoryInteractListener implements Listener {

    @EventHandler
    public void onInteract(InventoryInteractEvent event) {
        if (event.getWhoClicked().hasPermission("starly.banitem.bypass")) return;

        AtomicBoolean removed = new AtomicBoolean(false);

        Arrays.stream(event.getWhoClicked().getInventory().getContents())
                .filter(itemStack -> BanItemManager.getInstance().isBanned(itemStack, event.getWhoClicked().getWorld()))
                .forEach(itemStack -> {
                    itemStack.setType(Material.AIR);
                    removed.set(true);
                });
        ((Player) event.getWhoClicked()).updateInventory();

        if (removed.get())
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "itemBanned").ifPresent(event.getWhoClicked()::sendMessage);
    }
}
