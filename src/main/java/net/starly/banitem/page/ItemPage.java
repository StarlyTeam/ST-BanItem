package net.starly.banitem.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemPage {
    private final int pageNum;
    private final List<ItemStack> items;
}
