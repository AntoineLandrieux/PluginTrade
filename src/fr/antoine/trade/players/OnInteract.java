package fr.antoine.trade.players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnInteract implements Listener {
    public List<Integer> PLAYERS = Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12);
    public List<Integer> PLAYERS2 = Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17);
    public List<Integer> BORDERS = Arrays.asList(4, 13, 18, 19, 20, 22, 23, 24, 25);
    private final Inventory inventory1;
    private final Inventory inventory2;
    private final Player player1;
    private final Player player2;
    private int status1;
    private int status2;

    public OnInteract(Inventory inventory1, Inventory inventory2, Player player1, Player player2) {
        this.inventory1 = inventory1;
        this.inventory2 = inventory2;
        this.player1 = player1;
        this.player2 = player2;
        this.status1 = 0;
        this.status2 = 0;
    }

    private void changeStatus(Player player) {
        ItemStack valid = new ItemStack(Material.GREEN_WOOL);
        ItemMeta validMeta = valid.getItemMeta();
        if (validMeta != null)
            validMeta.setDisplayName("§r§l§cCancel");
        valid.setItemMeta(validMeta);
        ItemStack cancel = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelMeta = cancel.getItemMeta();
        if (cancelMeta != null)
            cancelMeta.setDisplayName("§r§l§aValidate");
        cancel.setItemMeta(cancelMeta);
        if (Objects.equals(player, player1)) {
            if (status1 == 0) {
                inventory1.setItem(21, valid);
                inventory2.setItem(21, valid);
                status1 = 1;
            } else {
                inventory1.setItem(21, cancel);
                inventory2.setItem(21, cancel);
                status1 = 0;
            }
        } else if (Objects.equals(player, player2)) {
            if (status2 == 0) {
                inventory2.setItem(26, valid);
                inventory1.setItem(26, valid);
                status2 = 1;
            } else {
                inventory2.setItem(26, cancel);
                inventory1.setItem(26, cancel);
                status2 = 0;
            }
        }
        if (status1 == status2) {
            for (int slot : new int[]{0, 1, 2, 3, 9, 10, 11, 12}) {
                ItemStack item = inventory1.getItem(slot);
                if (item != null)
                    player2.getInventory().addItem(item);
            }
            for (int slot : new int[]{5, 6, 7, 8, 14, 15, 16, 17}) {
                ItemStack item = inventory2.getItem(slot);
                if (item != null)
                    player1.getInventory().addItem(item);
            }
            inventory1.clear();
            inventory2.clear();
            player1.closeInventory();
            player2.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(inventory1) && !event.getInventory().equals(inventory2))
            return;
        for (int slot : new int[]{0, 1, 2, 3, 9, 10, 11, 12}) {
            ItemStack item = inventory1.getItem(slot);
            if (item != null)
                player1.getInventory().addItem(item);
        }
        for (int slot : new int[]{5, 6, 7, 8, 14, 15, 16, 17}) {
            ItemStack item = inventory2.getItem(slot);
            if (item != null)
                player2.getInventory().addItem(item);
        }
        inventory1.clear();
        inventory2.clear();
        if (event.getPlayer() == player1)
            player2.closeInventory();
        else if (event.getPlayer() == player2)
            player1.closeInventory();
    }

    @EventHandler
    public void onClick(InventoryClickEvent inventoryEvent) {
        if (!inventoryEvent.getInventory().equals(inventory1) && !inventoryEvent.getInventory().equals(inventory2))
            return;
        if (BORDERS.contains(inventoryEvent.getSlot()) || inventoryEvent.isShiftClick()) {
            inventoryEvent.setCancelled(true);
            return;
        }
        System.out.println(inventoryEvent.getSlot());
        if (inventoryEvent.getWhoClicked() == player1) {
            if ((PLAYERS2.contains(inventoryEvent.getSlot()))
                    || (inventoryEvent.getSlot() == 26)) {
                inventoryEvent.setCancelled(true);
                if (inventoryEvent.getSlot() == 21)
                    changeStatus(player1);
                return;
            }
        } else if (inventoryEvent.getWhoClicked() == player2) {
            if ((PLAYERS.contains(inventoryEvent.getSlot()))
                    || (inventoryEvent.getSlot() == 21)) {
                inventoryEvent.setCancelled(true);
                if (inventoryEvent.getSlot() == 21)
                    changeStatus(player1);
                return;
            }
        }
        if (inventoryEvent.getClickedInventory() == inventory1) {
            int slot = inventoryEvent.getSlot();
            ItemStack item = inventoryEvent.getCurrentItem();
            inventory2.setItem(slot, item);
        } else if (inventoryEvent.getClickedInventory() == inventory2) {
            int slot = inventoryEvent.getSlot();
            ItemStack item = inventoryEvent.getCurrentItem();
            inventory1.setItem(slot, item);
        }
    }
}
