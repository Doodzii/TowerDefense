package doodieman.towerdefense.mapsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCmdList {

    public MapSetupCmdList(Player player, String[] args, MapSetupHandler handler) {

        FileConfiguration config = TowerDefense.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("maps");

        player.sendMessage("§aListe af alle maps:");
        for (String mapID : section.getKeys(false)) {
            String fancyName = section.getString(mapID+".name");
            player.sendMessage("§a- §7ID: §f"+mapID+" §7NAME: §f"+fancyName);
        }

    }

}
