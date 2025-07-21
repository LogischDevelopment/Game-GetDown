package tv.logisch.game.manager;

import org.bukkit.entity.Player;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.*;

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
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Player> getTopPlayers(int limit) {
        return coins.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    // return place for player and player and behind player
    public List<Player> getPlayersAround(Player player) {
        List<Player> sortedPlayers = getSortedPlayersByCoins();
        List<Player> result = new ArrayList<>();

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player entry = sortedPlayers.get(i);
            if (entry.getUniqueId().equals(player.getUniqueId())) {
                result.add(entry);

                if (i + 1 < sortedPlayers.size()) {
                    result.add(sortedPlayers.get(i + 1));
                }
                break;
            }
            result.clear();
            result.add(entry);
        }

        return result;
    }

    public void clearAll() {
        coins.clear();
    }

}
