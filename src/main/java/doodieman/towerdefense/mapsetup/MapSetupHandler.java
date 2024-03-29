package doodieman.towerdefense.mapsetup;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.io.IOException;

public class MapSetupHandler {

    @Getter
    private final MapSetupListener listener;

    public MapSetupHandler() {
        this.listener = new MapSetupListener(this);
    }

    public boolean doesMapExist(String mapID) {
        return TowerDefense.getInstance().getConfig().contains("maps."+mapID);
    }

    public void saveSchematic(String mapID) {

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        String schematicPath = TowerDefense.getInstance().getDataFolder() + "/maps/" + mapID + ".schematic";

        Location corner1 = LocationUtil.stringToLocation(config.getString("maps."+mapID+".corner1"));
        Location corner2 = LocationUtil.stringToLocation(config.getString("maps."+mapID+".corner2"));
        World world = corner1.getWorld();

        try {
            File file = new File(schematicPath);

            Vector minCorner = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
            Vector maxCorner = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());

            CuboidRegion region = new CuboidRegion(new BukkitWorld(world), minCorner, maxCorner);

            Schematic schematic = new Schematic(region);
            schematic.save(file, ClipboardFormat.SCHEMATIC);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
