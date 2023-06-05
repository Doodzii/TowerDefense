package doodieman.towerdefense.game.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TurretUtil {

    final GameHandler handler;

    public TurretUtil(GameHandler handler) {
        this.handler = handler;
    }

    //Check if item is a turret
    public boolean isTurretItem(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR))
            return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey("turret");
    }

    //Get turret type from itemstack
    public TurretType getTurretFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("turret");
        return TurretType.getByID(compound.getString("type"));
    }

    //Create a brand new turret
    public GameTurret createTurret(Game game, TurretType turretType, Location location) {
        GameTurret turret = new GameTurret(game, turretType, location);
        turret.render();
        game.getTurrets().add(turret);
        return turret;
    }

    public void removeTurretItems(Player player, TurretType turretType, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (amount <= 0) return;
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            if (!isTurretItem(item) || !getTurretFromItem(item).equals(turretType)) continue;

            int itemAmount = item.getAmount();

            //Needs to remove more than in this itemstack
            if (amount >= itemAmount) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                amount -= itemAmount;

            } else {
                item.setAmount(itemAmount - amount);
                amount = 0;
            }
        }
    }




}
