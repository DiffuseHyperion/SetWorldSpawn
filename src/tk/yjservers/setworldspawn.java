package tk.yjservers;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        p.sendMessage(ChatColor.RED + "Expected integer");
                        return true;
                    }
                }
                int x1;
                int y1;
                int z1;
                switch (args.length) {
                    case 3:
                        if (Objects.equals(args[0], "~")) {
                            x1 = (p).getLocation().getBlockX();
                        } else {
                            x1 = Integer.parseInt(args[0]);
                        }
                        if (Objects.equals(args[1], "~")) {
                            y1 = (p).getLocation().getBlockY();
                        } else {
                            y1 = Integer.parseInt(args[1]);
                        }
                        if (Objects.equals(args[2], "~")) {
                            z1 = (p).getLocation().getBlockZ();
                        } else {
                            z1 = Integer.parseInt(args[2]);
                        }
                        break;
                    case 0:
                        x1 = (p).getLocation().getBlockX();
                        y1 = (p).getLocation().getBlockY();
                        z1 = (p).getLocation().getBlockZ();
                        break;
                    default:
                        p.sendMessage(ChatColor.RED + "Incomplete (expected 3 coordinates)");
                        return true;
                }
                if (p.getWorld().setSpawnLocation(x1, y1, z1)) {
                    p.sendMessage("Set this world spawn point to " + x1 + ", " + y1 + ", " + z1);
                } else {
                    p.sendMessage(ChatColor.RED + "Something went wrong");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid dimension");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only executable by player");
        }
        return true;
    }
}
