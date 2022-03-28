package tk.yjservers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetWorldSpawnMain extends JavaPlugin implements Listener {

    public static FileConfiguration config;
    public static FileConfiguration dataFileConfig;
    public static File dataFile;
    public static List<Location> spawns = new ArrayList<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        initData();
        this.getCommand("setworldspawn").setExecutor(new setworldspawn());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onNewcomerJoin(PlayerJoinEvent e) {
        //weird glitch that newcomers teleport to the void, this is a good enough workaround lmfao
        Player p = e.getPlayer();
        p.setBedSpawnLocation(getLoc(p.getWorld()));
        if (config.getString("teleport").equals("all")) {
            tpPlayer(p);
        } else {
            if (!p.hasPlayedBefore()) {
                tpPlayer(p);
            }
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent e) {
        // doing this to allow players to spawn in the middle of a block instead of a corner
        if (!e.isBedSpawn()) {
            tpPlayer(e.getPlayer());
        }
    }

    private void tpPlayer(Player p) {
        Location loc = getLoc(p.getWorld());
        if (loc == null) {
            return;
        }

        p.teleport(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private Location getLoc(World w) {
        for (Location l : spawns) {
            if (l.getWorld().equals(w)) {
                return l;
            }
        }
        return null;
    }

    private void initData() {
        //create dataFile
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", true);
        }

        //load dataFile
        dataFileConfig = new YamlConfiguration();
        try {
            dataFileConfig.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        //populate signslist
        if (dataFileConfig.getKeys(false).isEmpty()) {
            Bukkit.getLogger().warning("No signs found in data.yml, this may be an error!");
        } else {
            for (String s: dataFileConfig.getKeys(false)) {
                if (getServer().getWorld(s) == null) {
                    getLogger().severe("Something went wrong while reading data.yml, was it changed manually?");
                    getLogger().severe("Check if there is a section called : " + s + ", by using CTRL+F with your editor.");
                    getLogger().severe("This plugin will now disable itself to prevent further errors.");
                    getPluginLoader().disablePlugin(getServer().getPluginManager().getPlugin("SetWorldSpawn"));
                    return;
                }
                World w = getServer().getWorld(s);
                int x = dataFileConfig.getInt(s + ".x");
                int y = dataFileConfig.getInt(s + ".y");
                int z = dataFileConfig.getInt(s + ".z");

                spawns.add(new Location(w, x, y, z));
            }
        }
    }
}
