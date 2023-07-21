package net.starly.banitem.command;

import net.starly.banitem.BanItem;
import net.starly.banitem.command.sub.impl.CheckCommand;
import net.starly.banitem.command.sub.impl.ReloadCommand;
import net.starly.banitem.command.sub.impl.SettingCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BanItemExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6§l/밴아이템 설정 [월드] - 밴아이템을 설정합니다");
            sender.sendMessage("§6§l/밴아이템 확인 [플레이어] - 플레이어의 밴아이템 소유 여부를 확인합니다");
            sender.sendMessage("§6§l/밴아이템 리로드 - 구성파일을 다시 로드합니다");
            return true;
        }

        if (args[0].equalsIgnoreCase("설정"))
            new SettingCommand("starly.banitem.setting", 2, 2, false).run(sender, args);
        else if (args[0].equalsIgnoreCase("확인"))
            new CheckCommand("starly.banitem.check", 2, 2, true).run(sender, args);
        else if (args[0].equalsIgnoreCase("리로드"))
            new ReloadCommand("starly.banitem.reload", 1, 1, true).run(sender,args);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 2) return Arrays.asList("설정","확인","리로드");
        else if (args[0].equalsIgnoreCase("설정")) {
            return BanItem.getInstance().getServer().getWorlds().stream()
                    .map(World::getName)
                    .filter(name -> name.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        else if (args[0].equalsIgnoreCase("확인")) {
            return BanItem.getInstance().getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[1]))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
