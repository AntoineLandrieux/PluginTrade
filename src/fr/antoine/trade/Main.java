package fr.antoine.trade;

import fr.antoine.trade.commands.Trade;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("[TRADE]: plugin trade enable");
        Objects.requireNonNull(getCommand("trade")).setExecutor(new Trade(this));
    }

    @Override
    public void onDisable() {
        System.out.println("[TRADE]: plugin trade disable");
    }
}
