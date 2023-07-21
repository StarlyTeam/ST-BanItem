package net.starly.banitem.command.sub.impl;

import net.starly.banitem.BanItem;
import net.starly.banitem.command.sub.SubCommand;
import net.starly.banitem.context.MessageContent;
import net.starly.banitem.context.MessageType;
import net.starly.banitem.repo.BanItemRepo;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(String permission, int minArg, int maxArg, boolean consoleAble) {
        super(permission, minArg, maxArg, consoleAble);
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Plugin plugin = BanItem.getInstance();
        MessageContent content = MessageContent.getInstance();

        plugin.reloadConfig();
        content.initialize(plugin.getConfig());

        BanItemRepo.getInstance().saveData();
        BanItemRepo.getInstance().loadData();

        content.getMessageAfterPrefix(MessageType.NORMAL, "reloadComplete").ifPresent(sender::sendMessage);
    }
}
