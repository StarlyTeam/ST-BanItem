package net.starly.banitem;

import lombok.Getter;
import net.starly.banitem.command.BanItemExecutor;
import net.starly.banitem.context.MessageContent;
import net.starly.banitem.listener.InventoryInteractListener;
import net.starly.banitem.listener.ItemCraftListener;
import net.starly.banitem.listener.ItemInteractListener;
import net.starly.banitem.listener.ItemPickUpListener;
import net.starly.banitem.repo.BanItemRepo;
import net.starly.core.bstats.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BanItem extends JavaPlugin {

    @Getter
    private static BanItem instance;

    @Override
    public void onEnable() {
        /* DEPENDENCY
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        instance = this;
        new Metrics(this, 19034);

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        /* DATA
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        BanItemRepo.getInstance().loadData();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getCommand("밴아이템").setExecutor(new BanItemExecutor());
        getCommand("밴아이템").setTabCompleter(new BanItemExecutor());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        registerListeners(
                new InventoryInteractListener(),
                new ItemCraftListener(),
                new ItemPickUpListener(),
                new ItemInteractListener()
        );
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        BanItemRepo.getInstance().saveData();
    }
}
