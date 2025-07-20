package tv.logisch.game.manager;

import org.bukkit.entity.Player;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerCoinManager {

    private HashMap<Player, Integer> coins;

    public PlayerCoinManager() {
        this.coins = new HashMap<>();
    }

    public void addCoins(Player player, int amount) {
        coins.put(player, coins.getOrDefault(player, 0) + amount);
    }

    public void removeCoins(Player player, int amount) {
        if (coins.containsKey(player)) {
            int currentCoins = coins.get(player);
            if (currentCoins >= amount) {
                coins.put(player, currentCoins - amount);
            } else {
                coins.put(player, 0); // Prevent negative coins
            }
        }
    }

    public void setCoins(Player player, int amount) {
        if(amount < 0) amount = 0;
        coins.put(player, amount);
    }

    public int getCoins(Player player) {
        return coins.getOrDefault(player, 0);
    }

    public void resetCoins(Player player) {
        coins.put(player, 0);
    }

    public List<Player> getSortedPlayersByCoins() {
        return coins.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by coins in descending order
                .map(HashMap.Entry::getKey)
                .toList();
    }

    public void clearAll() {
        coins.clear();
    }

}
