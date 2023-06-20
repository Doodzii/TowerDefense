package doodieman.towerdefense;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import doodieman.towerdefense.chat.ChatHandler;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.lobby.mapselector.MapSelectorHandler;
import doodieman.towerdefense.lobby.spawn.SetspawnCommand;
import doodieman.towerdefense.lobby.spawn.SpawnCommand;
import doodieman.towerdefense.lobby.spawn.SpawnHandler;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.maps.MapHandler;
import doodieman.towerdefense.mapsetup.command.MapSetupCommand;
import doodieman.towerdefense.mapsetup.MapSetupHandler;
import doodieman.towerdefense.playerdata.PlayerDataHandler;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import doodieman.towerdefense.simplecommands.discord.DiscordCommand;
import doodieman.towerdefense.turretsetup.TurretSetupHandler;
import doodieman.towerdefense.turretsetup.command.TurretSetupCommand;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class TowerDefense extends JavaPlugin {

    @Getter
    private static TowerDefense instance;


    @Getter
    private MapGridHandler mapGridHandler;
    @Getter
    private MapSetupHandler mapSetupHandler;
    @Getter
    private ChatHandler chatHandler;
    @Getter
    private MapHandler mapHandler;
    @Getter
    private GameHandler gameHandler;
    @Getter
    private MapSelectorHandler mapSelectorHandler;
    @Getter
    private TurretSetupHandler turretSetupHandler;
    @Getter
    private PlayerDataHandler playerDataHandler;
    @Getter
    private SpawnHandler spawnHandler;

    /*
        external plugin dependencies
    */
    @Getter
    private WorldEditPlugin worldedit;
    @Getter
    private NPCRegistry npcRegistry;
    @Getter
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        this.worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        this.npcRegistry = CitizensAPI.createNamedNPCRegistry("towerdefense", new MemoryNPCDataStore());
        RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        this.luckPerms = luckPermsProvider.getProvider();

        Bukkit.getPluginManager().registerEvents(new GlobalListener(),this);

        //Initialize handlers and commands
        this.loadHandlers();
        this.loadCommands();
    }

    @Override
    public void onDisable() {
        this.gameHandler.exitAllGames();
        this.npcRegistry.deregisterAll();
        HologramsAPI.getHolograms(this).forEach(Hologram::delete);
    }

    private void loadHandlers() {
        this.playerDataHandler = new PlayerDataHandler();
        this.chatHandler = new ChatHandler();
        this.mapGridHandler = new MapGridHandler();
        this.mapSetupHandler = new MapSetupHandler();
        this.turretSetupHandler = new TurretSetupHandler();
        this.mapHandler = new MapHandler();
        this.gameHandler = new GameHandler();
        this.mapSelectorHandler = new MapSelectorHandler();
        this.spawnHandler = new SpawnHandler();
    }

    private void loadCommands() {
        Bukkit.getPluginCommand("mapsetup").setExecutor(new MapSetupCommand(this.mapSetupHandler));
        Bukkit.getPluginCommand("turretsetup").setExecutor(new TurretSetupCommand(this.turretSetupHandler));
        Bukkit.getPluginCommand("test").setExecutor(new TestCommand());
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetspawnCommand());
        Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand());
        Bukkit.getPluginCommand("discord").setExecutor(new DiscordCommand());
    }

    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
}
