package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;

        GameState state = GameManager.get().state();
        if(state.equals(GameState.WAITING) || state.equals(GameState.STARTING) || state.equals(GameState.FINISHED) || state.equals(GameState.SHOPPING) || state.equals(GameState.ENDED)) {
            e.setCancelled(true);
            return;
        }

        if(state.equals(GameState.RUNNING)) {
            if(GameManager.get().playersFinished.contains(p)) {
                e.setCancelled(true);
                return;
            }
            if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
                p.teleport(GameManager.get().gameWorld().getSpawnLocation());
                p.playSound(p, Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
                return;
            }
            if(e.getFinalDamage() >= p.getHealth()) {

                e.setCancelled(true);
                p.setHealth(20.0);
                p.getInventory().clear();
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
                p.teleport(p.getWorld().getSpawnLocation());
                int coins = GameManager.get().playerCoinManager().getCoins(p);
                int min = (int) (coins * 0.05);
                int max = (int) (coins*0.15);
                coins = (int) (Math.random() * (max - min + 1) + min);
                GameManager.get().playerCoinManager().removeCoins(p, coins);
                p.sendTitlePart(TitlePart.TITLE, Component.text("§c-" + coins));
            }
            return;
        }

        if(state.equals(GameState.PVP)) {

            if(e.getFinalDamage() >= p.getHealth()) {
                ItemStack mainHand = p.getInventory().getItemInMainHand();
                ItemStack offHand = p.getInventory().getItemInOffHand();

                ItemStack totem = null;
                boolean offhand = false;

                if (mainHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    totem = mainHand;
                } else if (offHand.getType().equals(Material.TOTEM_OF_UNDYING)) {
                    totem = offHand;
                    offhand = true;
                }

                if (totem != null) {
                    p.setFireTicks(0);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1));
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                    p.playEffect(EntityEffect.PROTECTED_FROM_DEATH);

                    if (totem.getAmount() > 1) {
                        totem.setAmount(totem.getAmount() - 1);
                    } else {
                        if (offhand) {
                            p.getInventory().setItemInOffHand(null);
                        } else {
                            p.getInventory().setItemInMainHand(null);
                        }
                    }

                    e.setCancelled(true);
                    p.setHealth(20.0);
                    return;
                }
                e.setCancelled(true);
                p.setHealth(20.0);
                p.getInventory().forEach(is -> {
                    if(is == null || is.getType().equals(Material.AIR)) return;
                    p.getWorld().dropItem(p.getLocation(), is);
                });
                p.getInventory().clear();
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
                p.setGameMode(GameMode.SPECTATOR);
                AtomicInteger remaining = new AtomicInteger(0);
                AtomicReference<Player> winner = new AtomicReference<>(null);
                Bukkit.getOnlinePlayers().forEach(target -> {
                    target.sendMessage(Component.text(GetDown.instance().prefix() + "§c" + p.getName() + " §7ist ausgeschieden!"));
                    if(target.getGameMode() != GameMode.SPECTATOR) {
                        remaining.getAndIncrement();
                        winner.set(target);
                    }
                });
                if(remaining.get() <= 1) {
                    GameManager.get().stop(winner.get());
                }
            }

        }

    }

}
