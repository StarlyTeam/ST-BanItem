package net.starly.banitem.gui;

import net.starly.banitem.BanItem;
import net.starly.banitem.manager.BanItemManager;
import net.starly.banitem.page.PaginationManager;
import net.starly.core.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class SettingInventory {

    private static SettingInventory instance;

    public static SettingInventory getInstance() {
        if (instance == null) instance = new SettingInventory();
        return instance;
    }

    private final Map<UUID, PaginationManager> pageMap = new HashMap<>();
    private final Map<UUID, World> worldMap = new HashMap<>();

    public void openInventory(Player player, World world) {
        Plugin plugin = BanItem.getInstance();
        PaginationManager manager = BanItemManager.getInstance().toPaginationManager(world);
        Inventory inventory = plugin.getServer().createInventory(null,54,world.getName() + " - 밴아이템 설정");

        manager.getCurrentPageData().getItems().stream()
                .filter(Objects::nonNull)
                .forEach(inventory::addItem);

        inventory.setItem(48, new ItemBuilder(Material.ARROW)
                .setDisplayName("§b이전 페이지")
                .build()
        );

        inventory.setItem(50, new ItemBuilder(Material.ARROW)
                .setDisplayName("§b다음 페이지")
                .build()
        );

        pageMap.put(player.getUniqueId(), manager);
        worldMap.put(player.getUniqueId(), world);

        player.openInventory(inventory);

        registerEvent(player, world);
    }

    public void openInventory(Player player) {
        PaginationManager manager = pageMap.get(player.getUniqueId());
        Inventory inventory = BanItem.getInstance().getServer().createInventory(null,54,worldMap.get(player.getUniqueId()).getName() + " - 밴아이템 설정");

        manager.getCurrentPageData().getItems().forEach(inventory::addItem);

        inventory.setItem(48, new ItemBuilder(Material.ARROW)
                .setDisplayName("§b이전 페이지")
                .build()
        );

        inventory.setItem(50, new ItemBuilder(Material.ARROW)
                .setDisplayName("§b다음 페이지")
                .build()
        );

        pageMap.put(player.getUniqueId(), manager);
        player.openInventory(inventory);

        registerEvent(player, worldMap.get(player.getUniqueId()));
    }

    public void nextPage(Player player) {
        if (!pageMap.containsKey(player.getUniqueId())) return;
        pageMap.get(player.getUniqueId()).nextPage();

        openInventory(player);
    }

    public void prevPage(Player player) {
        if (!pageMap.containsKey(player.getUniqueId())) return;
        pageMap.get(player.getUniqueId()).prevPage();

        openInventory(player);
    }

    private void registerEvent(Player player, World world) {
        Plugin plugin = BanItem.getInstance();
        Listener clickListener = new Listener() {};
        Listener closeListener = new Listener() {};


        plugin.getServer().getPluginManager().registerEvent(InventoryClickEvent.class, clickListener, EventPriority.LOWEST, (listeners, event) -> {
            if (event instanceof InventoryClickEvent) {
                InventoryClickEvent clickEvent = (InventoryClickEvent) event;
                if (!player.getUniqueId().equals(clickEvent.getWhoClicked().getUniqueId())) {
                    return;
                }

                if (clickEvent.getSlot() == 48) {
                    clickEvent.setCancelled(true);
                    prevPage((Player) clickEvent.getWhoClicked());
                } else if (clickEvent.getSlot() == 50) {
                    clickEvent.setCancelled(true);
                    nextPage((Player) clickEvent.getWhoClicked());
                }
            }
        }, plugin);

        plugin.getServer().getPluginManager().registerEvent(InventoryCloseEvent.class, closeListener, EventPriority.LOWEST, (listeners, event) -> {
            if (event instanceof InventoryCloseEvent) {
                InventoryCloseEvent closeEvent = (InventoryCloseEvent) event;
                if (!player.getUniqueId().equals(closeEvent.getPlayer().getUniqueId())) {
                    return;
                }

                closeEvent.getInventory().clear(48);
                closeEvent.getInventory().clear(50);

                List<ItemStack> itemList = Arrays.stream(closeEvent.getInventory().getContents()).collect(Collectors.toList());
                BanItemManager.getInstance().getBanItemList().put(world.getUID(), itemList);

                HandlerList.unregisterAll(clickListener);
                HandlerList.unregisterAll(closeListener);
            }
        }, plugin);
    }

}
