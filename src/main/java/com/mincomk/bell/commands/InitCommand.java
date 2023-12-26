package com.mincomk.bell.commands;

import com.mincomk.bell.Bell;
import com.mincomk.bell.bell.BellSpawner;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class InitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        var x = Integer.parseInt(args[0]);
        var y = Integer.parseInt(args[1]);
        var z = Integer.parseInt(args[2]);
        var id = args[3];
        var yaw = Float.parseFloat(args[4]);
        var pitch = Float.parseFloat(args[5]);
        var world = ((Player) commandSender).getWorld();

        var as = BellSpawner.spawnArmorStand(new Location(world, x, y, z), id);
        as.setRotation(yaw, pitch);
        BellSpawner.spawnBell(as);

        return true;
    }
}
