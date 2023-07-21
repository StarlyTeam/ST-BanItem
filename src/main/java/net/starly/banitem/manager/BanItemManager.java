package net.starly.banitem.manager;

import lombok.Getter;
import lombok.Setter;
import net.starly.banitem.page.PaginationManager;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BanItemManager {

    @Getter
    @Setter
    private HashMap<UUID, List<ItemStack>> banItemList;

    private static BanItemManager instance;

    public void init(HashMap<UUID, List<ItemStack>> banItems) {
        banItemList = banItems;
    }

    private BanItemManager() {
        banItemList = new HashMap<>();
    }

    public static BanItemManager getInstance() {
        if (instance == null) instance = new BanItemManager();
        return instance;
    }

    public boolean isBanned(ItemStack item, World world) {

        if (item == null) return false;

        for (ItemStack stack : banItemList.get(world.getUID())) {
            if (item.isSimilar(stack)) {
                return true;
            }
        }

        return false;
    }

    public PaginationManager toPaginationManager(World world) {
        if (!banItemList.containsKey(world.getUID()))
            return new PaginationManager(new ArrayList<>());

        return new PaginationManager(banItemList.get(world.getUID()));
    }

    public PaginationManager toPaginationManager(World world, int page) {
        PaginationManager manager = new PaginationManager(banItemList.get(world.getUID()));
        while (manager.getCurrentPage() < page) {
            if (manager.getPages().size() >= manager.getCurrentPage()) {
                break;
            }
            manager.nextPage();
        }
        return manager;
    }

}
