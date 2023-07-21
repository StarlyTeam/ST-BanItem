package net.starly.banitem.command.sub.impl;

import net.starly.banitem.BanItem;
import net.starly.banitem.command.sub.SubCommand;
import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.gui.SettingInventory;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingCommand extends SubCommand {

    public SettingCommand(String permission, int minArg, int maxArg, boolean consoleAble) {
        super(permission, minArg, maxArg, consoleAble);
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        World world = BanItem.getInstance().getServer().getWorld(args[1]);

        if (world == null) {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "noWorld").ifPresent(sender::sendMessage);
            return;
        }

        SettingInventory.getInstance().openInventory(player, world);
    }
}
