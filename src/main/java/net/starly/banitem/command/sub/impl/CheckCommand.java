package net.starly.banitem.command.sub.impl;

import net.starly.banitem.BanItem;
import net.starly.banitem.command.sub.SubCommand;
import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.manager.BanItemManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CheckCommand extends SubCommand {

    public CheckCommand(String permission, int minArg, int maxArg, boolean consoleAble) {
        super(permission, minArg, maxArg, consoleAble);
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player player = BanItem.getInstance().getServer().getPlayer(args[1]);
        if (player == null) {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "noPlayer").ifPresent(sender::sendMessage);
            return;
        }

        for (int i = 0 ; i < player.getInventory().getSize() ; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);

            if (itemStack == null) continue;
            if (!BanItemManager.getInstance().isBanned(itemStack, player.getWorld())) continue;

            player.getInventory().clear(i);
        }

        player.updateInventory();
        MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "checkComplete").ifPresent(sender::sendMessage);
    }
}
