package net.starly.banitem.page;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaginationManager {
    @Getter private final List<ItemPage> pages;
    @Getter
    private int currentPage;

    public PaginationManager(List<ItemStack> items) {
        this.pages = paginateItems(items);
        this.currentPage = 1;
    }

    public void nextPage() {
        if (currentPage < pages.size()) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 1) currentPage--;
    }

    public ItemPage getCurrentPageData() { return pages.get(currentPage - 1); }

    public List<ItemPage> paginateItems(List<ItemStack> items) {
        List<ItemPage> pages = new ArrayList<>();

        if (items.isEmpty()) {
            pages.add(new ItemPage(1,new ArrayList<>()));
            return pages;
        }

        int itemCount = items.size();
        int pageCount = (int) Math.ceil((double) itemCount / 45);
        for (int i = 0; i < pageCount; i++) {
            int start = i * 45;
            int end = Math.min(start + 45, itemCount);
            List<ItemStack> pageItems = items.subList(start, end);
            pages.add(new ItemPage(i + 1, pageItems));
        }
        return pages;
    }
}
