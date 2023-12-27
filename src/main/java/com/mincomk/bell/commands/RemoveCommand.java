package com.mincomk.bell.commands;

import com.mincomk.bell.Bell;
import com.mincomk.bell.bell.BellSpawner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;

        String id = args[0];
        Player p = (Player) sender;

        ArmorStand as;
        try {
            as = BellSpawner.findArmorById(p.getWorld(), id);
        } catch (IllegalArgumentException e) {
            p.sendMessage(Component.text("해당 id를 가진 bell이 없습니다.", NamedTextColor.RED));
            return true;
        }

        var world = as.getWorld();

        BellSpawner.removeAll(world, id);
        as.remove();

        return true;
    }
}
