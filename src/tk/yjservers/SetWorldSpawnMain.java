package tk.yjservers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetWorldSpawnMain extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getCommand("setworldspawn").setExecutor(new setworldspawn());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onNewcomerJoin(PlayerJoinEvent e) {
        //weird glitch that newcomers teleport to the void, this is a good enough workaround lmfao
        Player p = e.getPlayer();
        if (!p.hasPlayedBefore()) {
            p.teleport(p.getWorld().getSpawnLocation());
        }
    }
}
