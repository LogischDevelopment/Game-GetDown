package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
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
import tv.logisch.game.worlds.ColorObject;
import tv.logisch.game.worlds.WorldManager;

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
            Entity causingEntity = e.getDamageSource().getCausingEntity();
            if(causingEntity instanceof Player damager && GameManager.get().playersFinished.contains(damager)) {
                e.setCancelled(true);
                return;
            }
            if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
                p.teleport(GameManager.get().gameWorld().getSpawnLocation());
                p.playSound(p, Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
                return;
            }
            if(p.getLocation().getBlockY() >= p.getWorld().getSpawnLocation().getBlockY()) {
                e.setCancelled(true);
                return;
            }
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                double newDamage = e.getDamage() * 1.25;
                Block block = p.getLocation().getBlock().getRelative(0, -1, 0);
                if (block.getType().equals(Material.OBSIDIAN)) {
                    if (p.getLocation().getBlock().getRelative(0, -1, 0).equals(block)) {
                        if (Math.random() * 100 < GameManager.get().percentage()) {
                            block.setType(Material.HAY_BLOCK);
                            p.sendMessage(Component.text(GetDown.instance().prefix() + "§aDu bist auf einen Slime Block gefallen!"));
                            Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                                if (block.getType().equals(Material.HAY_BLOCK)) {
                                    ColorObject colorObject = WorldManager.color;
                                    Material material = colorObject == null ? Material.YELLOW_CONCRETE : colorObject.materials().stream().skip((int) (Math.random() * colorObject.materials().size())).findFirst().orElse(Material.YELLOW_CONCRETE);
                                    block.setType(material);
                                }
                            }, 5 * 20L);
                            e.setCancelled(true);
                            return;
                        } else {
                            ColorObject colorObject = WorldManager.color;
                            Material material = colorObject == null ? Material.YELLOW_CONCRETE : colorObject.materials().stream().skip((int) (Math.random() * colorObject.materials().size())).findFirst().orElse(Material.YELLOW_CONCRETE);
                            block.setType(material);
                            p.sendMessage(Component.text(GetDown.instance().prefix() + "§cDu bist auf einen normalen Block gefallen!"));
                        }
                    }
                }
                if(newDamage >= p.getHealth()) {
                    e.setCancelled(true);
                    p.setHealth(20.0);
                    p.getInventory().clear();
                    p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
                    p.teleport(p.getWorld().getSpawnLocation());
                    int coins = GameManager.get().playerCoinManager().getCoins(p);
                    int min = (int) (coins * 0.05);
                    int max = (int) (coins * 0.15);
                    coins = (int) (Math.random() * (max - min + 1) + min);
                    GameManager.get().playerCoinManager().removeCoins(p, coins);
                    p.sendTitlePart(TitlePart.TITLE, Component.text("§c-" + coins));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
                } else {
                    e.setDamage(newDamage);
                }
                return;
            }
            if(e.getFinalDamage() >= p.getHealth()) {

                e.setCancelled(true);
                p.setHealth(20.0);
                p.getInventory().clear();
                p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
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
                    p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
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
                p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
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
            e.setCancelled(false);

        }

    }

}
