package doodieman.towerdefense.lobby.mapselector.gui;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameSave;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class MapSelectorMenuSingleplayer extends GUI {

    private final HashMap<Integer, MapSlot> mapSlots = new HashMap<>();

    public MapSelectorMenuSingleplayer(Player player) {
        super(player, 6, "Vælg et map");

        mapSlots.put(10, new MapSlot("eventyr", "§a§oBegynder"));
        mapSlots.put(19, new MapSlot("træstammen", "§a§oBegynder"));
        mapSlots.put(28, new MapSlot("oerkenen", "§a§oBegynder"));

        mapSlots.put(12, new MapSlot("haven", "§e§oØvede"));
        mapSlots.put(21, new MapSlot("majsmarken", "§e§oØvede"));
        mapSlots.put(30, new MapSlot("jordskælv", "§e§oØvede"));

        mapSlots.put(14, new MapSlot("udflugten", "§6§oAvanceret"));
        mapSlots.put(23, new MapSlot("grønland", "§6§oAvanceret"));
        mapSlots.put(32, new MapSlot(null, "§6§oAvanceret"));

        mapSlots.put(16, new MapSlot("skak", "§c§oExpert"));
        mapSlots.put(25, new MapSlot("domino", "§c§oExpert"));
        mapSlots.put(34, new MapSlot(null, "§c§oExpert"));

    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 45; i < 54; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(49, GUIItem.BACK.getItem());

        mapSlots.forEach((slot, mapSlot) -> {
            ItemBuilder itemBuilder;

            if (mapSlot.getMapID() != null) {

                Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

                //Doesn't have a save
                if (!GameUtil.getInstance().hasSavedGame(player,map)) {

                    itemBuilder = new ItemBuilder(Material.EMPTY_MAP);
                    itemBuilder.name("§f§n"+map.getMapName());
                    itemBuilder.lore("§f- §7§o"+mapSlot.getDifficulty(), "", "§fTryk for at spille!");
                    if (map.getMapVisual().size() > 0) {
                        itemBuilder.addLore("");
                        itemBuilder.addLore(map.getMapVisual());
                    }
                }

                //Has a save
                else {

                    GameSave save = GameUtil.getInstance().getSavedGame(player,map);

                    itemBuilder = new ItemBuilder(Material.EMPTY_MAP);
                    itemBuilder.name("§f§n"+map.getMapName());
                    itemBuilder.lore(
                        "§f- §7§o"+mapSlot.getDifficulty(),
                        "",
                        "§7[§aSAVE§7] "+save.getFormattedDate(),
                        "",
                        "§7Gemt klokken: §f"+save.getFormattedTime().replace(":","§7:§f"),
                        "§7Runde: §f"+save.getRound()+"§7/§f"+save.getDifficulty().getRounds(),
                        "",
                        "§f§oVenstreklik§f for at spille videre!",
                        "§f§oHøjreklik§f for at starte forfra!"
                    );
                    if (map.getMapVisual().size() > 0) {
                        itemBuilder.addLore("");
                        itemBuilder.addLore(map.getMapVisual());
                    }
                }



            } else {
                itemBuilder = new ItemBuilder(Material.PAPER);
                itemBuilder.name("§f§n???");
                itemBuilder.lore("§f- "+mapSlot.getDifficulty(), "", "§fKommer snart..");

            }

            this.layout.put(slot, itemBuilder.build());
        });

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 49) {
            new MapSelectorMenu(player).open();
            this.playClickSound();
            return;
        }

        //Open select difficulty menu
        if (mapSlots.containsKey(slot)) {
            MapSlot mapSlot = mapSlots.get(slot);
            if (mapSlot.getMapID() == null) return;
            Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

            if (!GameUtil.getInstance().hasSavedGame(player,map)) {
                new MapSelectorMenuDifficulty(player, map).open();
                this.playClickSound();
                return;
            }

            //Left click - Start brand new
            if (clickType == ClickType.RIGHT) {
                new MapSelectorMenuDifficulty(player, map).open();
                this.playClickSound();
            }

            //Right click - Load saved game
            else if (clickType == ClickType.LEFT) {
                this.playClickSound();
                player.closeInventory();

                GameUtil.getInstance().loadGame(player,map);
            }


        }

    }

    @RequiredArgsConstructor
    private class MapSlot {
        @Getter
        private final String mapID;
        @Getter
        private final String difficulty;
    }

}
