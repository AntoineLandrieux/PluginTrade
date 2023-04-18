package fr.antoine.trade.players;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnInteract implements Listener {
    
    public static List<Integer> PLAYERS = Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12);
    public static List<Integer> PLAYERS2 = Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17);
    public static List<Integer> BORDERS = Arrays.asList(4, 13, 18, 19, 20, 22, 23, 24, 25);
    private final Inventory inventory1;
    private final Inventory inventory2;
    private final Player player1;
    private final Player player2;
    
    public OnInteract(Inventory inventory1, Inventory inventory2, Player player1, Player player2) {
        this.inventory1 = inventory1;
        this.inventory2 = inventory2;
        this.player1 = player1;
        this.player2 = player2;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(inventory1) && !event.getInventory().equals(inventory2))
            return;
        Inventory inventory = event.getInventory();
        if (Objects.equals(inventory, inventory1)) {
            for (int slot : PLAYERS) {
                ItemStack item = inventory2.getItem(slot);
                if (item != null)
                    player1.getInventory().addItem(item);
            } for (int slot : PLAYERS2) {
                ItemStack item = inventory2.getItem(slot);
                if (item != null)
                    player2.getInventory().addItem(item);
            }
            inventory2.clear();
        } else if (Objects.equals(inventory, inventory2)) {
            for (int slot : PLAYERS) {
                ItemStack item = inventory1.getItem(slot);
                if (item != null)
                    player1.getInventory().addItem(item);
            }
            for (int slot : PLAYERS2) {
                ItemStack item = inventory1.getItem(slot);
                if (item != null)
                    player2.getInventory().addItem(item);
            }
            inventory1.clear();
        }
        player1.sendMessage("§cTrade was canceled by §6" + event.getPlayer().getName() + "§c !");
        player2.sendMessage("§cTrade was canceled by §6" + event.getPlayer().getName() + "§c !");
    }

    @EventHandler
    public void onClick(InventoryClickEvent inventoryEvent) {
        if (!inventoryEvent.getInventory().equals(inventory1) && !inventoryEvent.getInventory().equals(inventory2))
            return;
        if (BORDERS.contains(inventoryEvent.getRawSlot()) || inventoryEvent.isShiftClick()) {
            inventoryEvent.setCancelled(true);
        }
        if (inventoryEvent.getWhoClicked() == player1) {
            if ((PLAYERS2.contains(inventoryEvent.getRawSlot()))
                    || (inventoryEvent.getRawSlot() == 26)) {
                inventoryEvent.setCancelled(true);
            }
        } else if (inventoryEvent.getWhoClicked() == player2) {
            if ((PLAYERS.contains(inventoryEvent.getRawSlot()))
                    || (inventoryEvent.getRawSlot() == 21)) {
                inventoryEvent.setCancelled(true);
            }
        }
        if (inventoryEvent.getClickedInventory() == inventory1) {
            int slot = inventoryEvent.getRawSlot();
            ItemStack item = inventoryEvent.getCurrentItem();
            inventory2.setItem(slot, item);
        } else if (inventoryEvent.getClickedInventory() == inventory2) {
            int slot = inventoryEvent.getRawSlot();
            ItemStack item = inventoryEvent.getCurrentItem();
            inventory1.setItem(slot, item);
        }
    }
}
