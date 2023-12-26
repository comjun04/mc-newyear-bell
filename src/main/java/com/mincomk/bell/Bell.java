package com.mincomk.bell;

import com.mincomk.bell.bell.BellSpawner;
import com.mincomk.bell.commands.InitCommand;
import com.mincomk.bell.commands.ResetCommand;
import com.mincomk.bell.commands.StartCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bell extends JavaPlugin {
    public static final String BELL_KEY = "bell";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Bell plugin enabled");
        getCommand("init").setExecutor(new InitCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("reset").setExecutor(new ResetCommand());

        // on tick
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            var w = getServer().getWorlds().get(0);
            w.getEntities().stream().filter((Entity en) ->
                en.getPersistentDataContainer()
                        .has(new org.bukkit.NamespacedKey(Bell.BELL_KEY, "tick"),
                                org.bukkit.persistence.PersistentDataType.BOOLEAN)
            ).forEach((Entity en) -> {
                en.setRotation((float) 0, (float) Math.toDegrees(Math.sin((double) en.getTicksLived() / 10) /6));
                BellSpawner.updateBell((ArmorStand) en);
            });
        }, 1l, 1l);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
