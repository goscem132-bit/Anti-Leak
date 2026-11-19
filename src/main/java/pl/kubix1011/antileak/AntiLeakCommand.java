
package pl.kubix1011.antileak;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AntiLeakCommand implements CommandExecutor {

    private final AntiLeak plugin;

    public AntiLeakCommand(AntiLeak plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length != 2) {
            sender.sendMessage(
                ChatColor.RED +
                "Usage: /antileak <player|@a> <on|off>"
            );
            return true;
        }

        boolean enabled;

        if (args[1].equalsIgnoreCase("on")) {
            enabled = true;
        } else if (args[1].equalsIgnoreCase("off")) {
            enabled = false;
        } else {
            sender.sendMessage(
                ChatColor.RED + "Use on or off!"
            );
            return true;
        }

        if (args[0].equalsIgnoreCase("@a")) {

            int changed = 0;

            for (Player player : Bukkit.getOnlinePlayers()) {
                plugin.getVisibilityManager().setEnabled(
                    player.getUniqueId(),
                    enabled
                );

                changed++;
            }

            sender.sendMessage(
                ChatColor.GREEN +
                "AntiLeak updated for " + changed +
                " players. Status: " +
                (enabled ? "ON" : "OFF")
            );

            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage(
                ChatColor.RED +
                "Player is not online: " + args[0]
            );
            return true;
        }

        plugin.getVisibilityManager().setEnabled(
            target.getUniqueId(),
            enabled
        );

        sender.sendMessage(
            ChatColor.GREEN +
            "AntiLeak for " + target.getName() +
            " is now " + (enabled ? "ON" : "OFF")
        );

        target.sendMessage(
            ChatColor.YELLOW +
            "Your AntiLeak status is now " +
            (enabled ? "ON" : "OFF")
        );

        return true;
    }
}
