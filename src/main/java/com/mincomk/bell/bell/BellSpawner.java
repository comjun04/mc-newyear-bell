package com.mincomk.bell.bell;

import com.mincomk.bell.Bell;
import com.mincomk.bell.math.MincoMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BellSpawner {
    private static final Vector[] goldBlocks = {
            new Vector(0, 0, 0),
            new Vector(1, -1, 0),
            new Vector(-1, -1, 0),


            new Vector(0, -2, 0),
            new Vector(0, -2, 1),
            new Vector(1, -2, 0),
            new Vector(1, -2, 1),
            new Vector(-1, -2, 0),
            new Vector(0, -2, -1),
            new Vector(-1, -2, -1),
            new Vector(1, -2, -1),
            new Vector(-1, -2, 1),
            new Vector(-2, -2, 0),
            new Vector(2, -2, 0),


            new Vector(2, -3, -1),
            new Vector(2, -3, 0),
            new Vector(2, -3, 1),
            new Vector(-2, -3, -1),
            new Vector(-2, -3, 0),
            new Vector(-2, -3, 1),
            new Vector(-1, -3, 2),
            new Vector(0, -3, 2),
            new Vector(1, -3, 2),
            new Vector(-1, -3, -2),
            new Vector(0, -3, -2),
            new Vector(1, -3, -2),

            new Vector(2, -4, -1),
            new Vector(2, -4, 0),
            new Vector(2, -4, 1),
            new Vector(-2, -4, -1),
            new Vector(-2, -4, 0),
            new Vector(-2, -4, 1),
            new Vector(-1, -4, 2),
            new Vector(0, -4, 2),
            new Vector(1, -4, 2),
            new Vector(-1, -4, -2),
            new Vector(0, -4, -2),
            new Vector(1, -4, -2),

            new Vector(2, -5, -1),
            new Vector(2, -5, 0),
            new Vector(2, -5, 1),
            new Vector(-2, -5, -1),
            new Vector(-2, -5, 0),
            new Vector(-2, -5, 1),
            new Vector(-1, -5, 2),
            new Vector(0, -5, 2),
            new Vector(1, -5, 2),
            new Vector(-1, -5, -2),
            new Vector(0, -5, -2),
            new Vector(1, -5, -2),

            new Vector(2, -6, -1),
            new Vector(2, -6, 0),
            new Vector(2, -6, 1),
            new Vector(-2, -6, -1),
            new Vector(-2, -6, 0),
            new Vector(-2, -6, 1),
            new Vector(-1, -6, 2),
            new Vector(0, -6, 2),
            new Vector(1, -6, 2),
            new Vector(-1, -6, -2),
            new Vector(0, -6, -2),
            new Vector(1, -6, -2),
    };

    public static void spawnBell(ArmorStand as) {
        var pdc = as.getPersistentDataContainer();
        var idKey = new NamespacedKey(Bell.BELL_KEY, "id");
        var id = pdc.get(idKey, PersistentDataType.STRING);

        var i = 0;
        for (var v : goldBlocks) {
            var loc = as.getLocation();
            var vecRel = MincoMath.translateAlongLook(v, loc.getYaw(), loc.getPitch());
            var spawnLoc = loc.clone().add(vecRel);
            var bd = (BlockDisplay) loc.getWorld().spawnEntity(spawnLoc, EntityType.BLOCK_DISPLAY);
            bd.setBlock(Material.GOLD_BLOCK.createBlockData());
            bd.setGravity(false);
            bd.setRotation(as.getLocation().getYaw(), -as.getLocation().getPitch());

            var bdpdc = bd.getPersistentDataContainer();
            var bellIdKey = new NamespacedKey(Bell.BELL_KEY, "id");
            bdpdc.set(bellIdKey, PersistentDataType.STRING, id);
            var bellGoldKey = new NamespacedKey(Bell.BELL_KEY, "gold");
            bdpdc.set(bellGoldKey, PersistentDataType.BOOLEAN, true);
            var bellGoldNoKey = new NamespacedKey(Bell.BELL_KEY, "goldno");
            bdpdc.set(bellGoldNoKey, PersistentDataType.INTEGER, i);
            i++;
        }
    }

    public static void updateBell(ArmorStand as) {
        var pdc = as.getPersistentDataContainer();
        var idKey = new NamespacedKey(Bell.BELL_KEY, "id");
        var id = pdc.get(idKey, PersistentDataType.STRING);
        var entities = as.getWorld().getEntities();
        List<BlockDisplay> bds = new ArrayList<>();
        for (var e : entities) {
            if (e instanceof BlockDisplay) {
                var bd = (BlockDisplay) e;
                var bdpdc = bd.getPersistentDataContainer();
                var bellIdKey = new NamespacedKey(Bell.BELL_KEY, "id");
                var bellId = bdpdc.get(bellIdKey, PersistentDataType.STRING);
                if (bellId.equals(id)) {
                    var bellGoldKey = new NamespacedKey(Bell.BELL_KEY, "gold");
                    var bellGold = bdpdc.get(bellGoldKey, PersistentDataType.BOOLEAN);
                    if (bellGold) {
                        bds.add(bd);
                    }
                }
            }
        }

        bds.sort((bd1, bd2) -> {
            var pdc1 = bd1.getPersistentDataContainer();
            var bellGoldNoKey1 = new NamespacedKey(Bell.BELL_KEY, "goldno");
            var bellGoldNo1 = pdc1.get(bellGoldNoKey1, PersistentDataType.INTEGER);
            var pdc2 = bd2.getPersistentDataContainer();
            var bellGoldNoKey2 = new NamespacedKey(Bell.BELL_KEY, "goldno");
            var bellGoldNo2 = pdc2.get(bellGoldNoKey2, PersistentDataType.INTEGER);
            return bellGoldNo1 - bellGoldNo2;
        });

        var loc = as.getLocation();
        var i = 0;
        for (var v : goldBlocks) {
            var bd = bds.get(i);
            var newLoc = loc.clone().add(MincoMath.translateAlongLook(v, loc.getYaw(), loc.getPitch()));
            bd.teleport(newLoc);
            bd.setRotation(loc.getYaw(), -loc.getPitch());
            i++;
        }
    }

    public static ArmorStand spawnArmorStand(Location loc, String id) {
        var as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setVisible(false);
        as.setMarker(true);

        var pdc = as.getPersistentDataContainer();

        var bellKey = new NamespacedKey(Bell.BELL_KEY, "bell");
        pdc.set(bellKey, PersistentDataType.BOOLEAN, true);

        var bellIdKey = new NamespacedKey(Bell.BELL_KEY, "id");
        pdc.set(bellIdKey, PersistentDataType.STRING, id);
        return as;
    }

    public static void removeAll(World w, String id) {
        var entities = w.getEntities();
        for (var e : entities) {
            if (e instanceof BlockDisplay) {
                var bd = (BlockDisplay) e;
                var bdpdc = bd.getPersistentDataContainer();
                var bellIdKey = new NamespacedKey(Bell.BELL_KEY, "id");
                var bellId = bdpdc.get(bellIdKey, PersistentDataType.STRING);
                if (bellId.equals(id)) {
                    bd.remove();
                }
            }
        }
    }

    public static ArmorStand findArmorById(World w, String id) {
        var entities = w.getEntities();
        for (var e : entities) {
            if (e instanceof ArmorStand) {
                var as = (ArmorStand) e;
                var aspdc = as.getPersistentDataContainer();
                var bellIdKey = new NamespacedKey(Bell.BELL_KEY, "id");
                var bellId = aspdc.get(bellIdKey, PersistentDataType.STRING);
                if (bellId.equals(id)) {
                    return as;
                }
            }
        }
        throw new IllegalArgumentException("No bell with id " + id);
    }
}
