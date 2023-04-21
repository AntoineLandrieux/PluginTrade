package fr.antoine.trade.commands;

import fr.antoine.trade.Main;
import fr.antoine.trade.players.OnInteract;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Trade implements CommandExecutor {
    private final Main main;
    public HashMap<Player, Player>playerHashMap = new HashMap<>();

    public Trade(Main main) {
        this.main = main;
    }

    public void init(Player player, Player target) {
        Inventory customInventory = Bukkit.createInventory(null, 27, "§r§l§9TRADE :");
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        if (glassMeta != null)
            glassMeta.setDisplayName("");
        glassPane.setItemMeta(glassMeta);
        for (int slot : OnInteract.BORDERS) {
            customInventory.setItem(slot, glassPane);
        }
        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        ItemMeta redWoolMeta = redWool.getItemMeta();
        if (redWoolMeta != null)
            redWoolMeta.setDisplayName("§r§l§aValidate");
        redWool.setItemMeta(redWoolMeta);
        customInventory.setItem(21, redWool);
        customInventory.setItem(26, redWool);
        player.openInventory(customInventory);
        target.openInventory(customInventory);
        Bukkit.getPluginManager().registerEvents(new OnInteract(customInventory, customInventory, player, target, main), main);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info("[" + main.getConfig().getString("server.name") +
                    "]: Only players can use this command");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // trade
            player.sendMessage("§9[§r" + main.getConfig().getString("server.name") +
                    "§9]:§9 Trade command:§6\n/trade <player>\n/trade <accept | deny>");
        } else if (args.length == 1) {
            // trade <player|accept|deny>
            if (args[0].equalsIgnoreCase("accept")) {
                // trade <accept>
                if (playerHashMap.get(player) == null) {
                    player.sendMessage("§9[§r" + main.getConfig().getString("server.name") +
                            "§9]:§c No request in progress");
                } else {
                    init(playerHashMap.get(player), player);
                    playerHashMap.remove(player);
                }
            } else if (args[0].equalsIgnoreCase("deny")) {
                // trade <deny>
                if (playerHashMap.get(player) == null) {
                    player.sendMessage("§9[§r" + main.getConfig().getString("server.name") +
                            "§9]:§c No request in progress !");
                } else {
                    playerHashMap.get(player).sendMessage("§9[§r"+main.getConfig().getString("server.name") +
                            "§9]:§6 " + player.getName() + "§c have deny");
                    player.sendMessage("§9[§r" + main.getConfig().getString("server.name") +
                            "§9]:§c You refused the request !");
                    playerHashMap.remove(player);
                }
            } else {
                // trade <player>
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§9[§r" + main.getConfig().getString("server.name") +
                            "§9]:§c The player §6" + args[0] + "§c is offline or does not exist");
                } else if (target == player) {
                    player.sendMessage(
                            "§9[§r"+main.getConfig().getString("server.name") +
                                    "§9]:§c You can't trade with yourself"
                    );
                } else {
                    if (playerHashMap.get(target) != null) {
                        playerHashMap.remove(target);
                    }
                    // TODO: add cooldown
                    playerHashMap.put(target, player);
                    player.sendMessage(
                            "§9[§r" + main.getConfig().getString("server.name") +
                                    "§9]:§a A request has been sent to §9" + args[0]
                    );
                    target.sendMessage(
                            "§9[§r" + main.getConfig().getString("server.name") +
                                    "§9]:§a You have a trade request from player §9" +
                                    player.getName() +
                                    "\n§aYou can use §9/trade accept §aor §9/trade deny"
                    );
                }
            }
        } else {
            player.sendMessage(
                    "§9[§r" + main.getConfig().getString("server.name") +
                            "§9]:§c Too many argument, use :§6\n/trade <player>\n/trade <accept | deny>"
            );
            return false;
        }
        return true;
    }
}
