package tv.logisch.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.objects.EffectSetting;
import tv.logisch.game.objects.GameEffect;
import tv.logisch.game.objects.ItemSetting;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        if(GameManager.get().state().equals(GameState.STARTING)) {
            e.setCancelled(true);
            return;
        }

        if(GameManager.get().state().equals(GameState.WAITING) || GameManager.get().state().equals(GameState.SHOPPING)) {
            int y = e.getTo().getBlockY();
            if(y-10 < e.getPlayer().getWorld().getSpawnLocation().getBlockY()) {
                e.setTo(e.getPlayer().getWorld().getSpawnLocation());
            }
            return;
        }

        if(GameManager.get().state().equals(GameState.RUNNING)) {

            if(e.getPlayer().getLocation().getBlockY() <= 0) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if(GameManager.get().state().equals(GameState.RUNNING)) {
                        if(e.getPlayer().getLocation().getBlockY() <= 0) {
                            GameManager.get().playersFinished.add(e.getPlayer());
                            // TODO: implement player message and global message

                            if(GameManager.get().playersFinished.size() >= 3) {
                                GameManager.get().startShopping();
                            }
                        }
                    }
                }, 20L);
                return;
            }

            Block block = e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0);

            if(block.getType().equals(Material.GOLD_BLOCK)) {
                int min = 25;
                int max = 55;
                int coins = (int) (Math.random() * (max - min + 1)) + min;
                GameManager.get().playerCoinManager().addCoins(e.getPlayer(), coins);
            } else if(block.getType().equals(Material.DIAMOND_BLOCK)) {
                int min = 75;
                int max = 115;
                int coins = (int) (Math.random() * (max - min + 1)) + min;
                GameManager.get().playerCoinManager().addCoins(e.getPlayer(), coins);
            } else if(block.getType().equals(Material.LAPIS_BLOCK)) {
                ItemStack randomItem = ItemSetting.getRandomItem();
                if(randomItem != null) {
                    e.getPlayer().getInventory().addItem(randomItem);
                }
            } else if(block.getType().equals(Material.OBSIDIAN)) {
                if(Math.random() * 100 < GameManager.get().percentage()) {
                    block.setType(Material.SLIME_BLOCK);
                }
            } else if(block.getType().equals(Material.REDSTONE)) {
                GameEffect effect = EffectSetting.getRandomEffect();
                if(effect != null) {
                    e.getPlayer().addPotionEffect(new PotionEffect(effect.type(), effect.duration() * 20, effect.amplifier()));
                }
            }

        }

    }

}
