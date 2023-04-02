package fr.antoine.trade.commands;

import fr.antoine.trade.*;
import fr.antoine.trade.players.OnInteract;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.Objects;

public class Trade implements CommandExecutor {
    private static final int[] BORDER = {4,13,18,19,20,22,23,24,25};
    private final Main main;

    public Trade(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String msg, String[] arg) {
        if (!(commandSender instanceof Player))
            return false;
        Player player = (Player)commandSender;
        if (arg.length != 1) {
            player.sendMessage("How many argument : use /trade <player>");
            return false;
        }
        Player target = Bukkit.getPlayer(arg[0]);
        if (target == null) {
            player.sendMessage("No player named "+arg[0]+" : use /trade <player>");
            return false;
        }
        if (Objects.equals(target, player)) {
            player.sendMessage("You can't trade with yourself");
            return false;
        }
        Inventory customInventory = Bukkit.createInventory(null, 27, "§r§l§9TRADE :");
        ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerSkullMeta = (SkullMeta) playerSkull.getItemMeta();
        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        if (borderMeta != null)
            borderMeta.setDisplayName("§r§l");
        border.setItemMeta(borderMeta);
        for (int i : BORDER)
            customInventory.setItem(i, border);
        if (playerSkullMeta != null)
            playerSkullMeta.setDisplayName("§r§l§9"+player.getName());
        playerSkull.setItemMeta(playerSkullMeta);
        customInventory.setItem(18, playerSkull);
        ItemStack targetSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta targetSkullMeta = (SkullMeta) targetSkull.getItemMeta();
        if (targetSkullMeta != null)
            targetSkullMeta.setDisplayName("§r§l§9"+target.getName());
        targetSkull.setItemMeta(targetSkullMeta);
        customInventory.setItem(23, targetSkull);
        ItemStack cancel = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelMeta = cancel.getItemMeta();
        if (cancelMeta != null)
            cancelMeta.setDisplayName("§r§l§aValidate");
        cancel.setItemMeta(cancelMeta);
        customInventory.setItem(21, cancel);
        customInventory.setItem(26, cancel);
        player.openInventory(customInventory);
        target.openInventory(customInventory);
        Bukkit.getPluginManager().registerEvents(new OnInteract(customInventory, customInventory, player, target), main);
        return true;
    }
}