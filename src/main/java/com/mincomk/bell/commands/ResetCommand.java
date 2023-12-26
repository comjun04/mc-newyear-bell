package com.mincomk.bell.commands;

import com.mincomk.bell.Bell;
import com.mincomk.bell.bell.BellSpawner;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {   @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        var id = args[0];
        var p = (Player) commandSender;

        var as = BellSpawner.findArmorById(p.getWorld(), id);
        var pdc = as.getPersistentDataContainer();
        var keyTick = new NamespacedKey(Bell.BELL_KEY, "tick");
        pdc.set(keyTick, PersistentDataType.BOOLEAN, false);
        return true;
    }
}
