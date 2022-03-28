package tk.yjservers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tk.yjservers.SetWorldSpawnMain.*;

public class setworldspawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                Pattern pa = Pattern.compile("[0-9|~]+");
                for (String s : args) {
                    Matcher m = pa.matcher(s);
                    if (m.matches()) {
                        p.sendMessage(ChatColor.RED + "Unexpected character found! You can only use numbers, or tildes.");
                        return true;
                    }
                }
                int x;
                int y;
                int z;
                switch (args.length) {
                    case 3:
                        if (Objects.equals(args[0], "~")) {
                            x = (p).getLocation().getBlockX();
                        } else {
                            x = Integer.parseInt(args[0]);
                        }
                        if (Objects.equals(args[1], "~")) {
                            y = (p).getLocation().getBlockY();
                        } else {
                            y = Integer.parseInt(args[1]);
                        }
                        if (Objects.equals(args[2], "~")) {
                            z = (p).getLocation().getBlockZ();
                        } else {
                            z = Integer.parseInt(args[2]);
                        }
                        break;
                    case 0:
                        x = (p).getLocation().getBlockX();
                        y = (p).getLocation().getBlockY();
                        z = (p).getLocation().getBlockZ();
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "Not enough arguments! Either enter 3 integers or no arguments to use your location.");
                        return true;
                }

                Location loc = new Location(p.getWorld(), x, y, z);
                String worldname = loc.getWorld().getName();

                if (spawns.contains(loc)) {
                    p.sendMessage(ChatColor.YELLOW + "There is already a world spawn for this world. Overwriting the previous spawn.");
                } else {
                    dataFileConfig.createSection(loc.getWorld().getName());
                    dataFileConfig.createSection(worldname + ".x");
                    dataFileConfig.createSection(worldname + ".y");
                    dataFileConfig.createSection(worldname + ".z");

                }
                dataFileConfig.set(worldname + ".x", loc.getBlockX());
                dataFileConfig.set(worldname + ".y", loc.getBlockY());
                dataFileConfig.set(worldname + ".z", loc.getBlockZ());

                spawns.add(loc);
                try {
                    dataFileConfig.save(dataFile);
                    p.sendMessage("Set this world spawn point to " + x + ", " + y + ", " + z);
                } catch (IOException e) {
                    e.printStackTrace();
                    p.sendMessage(ChatColor.RED + "Something went wrong while saving the file! The stack trace has been printed to the console.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You can only use this command in the overworld!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command is only executable by a player!");
        }
        return true;
    }
}
