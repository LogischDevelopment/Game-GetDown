package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import tv.logisch.game.GetDown;
import tv.logisch.game.enums.GameState;
import tv.logisch.game.manager.GameManager;
import tv.logisch.game.objects.EffectSetting;
import tv.logisch.game.objects.GameEffect;
import tv.logisch.game.objects.ItemSetting;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getBlockZ()) {
            return;
        }

        if(GameManager.get().state().equals(GameState.STARTING)) {
            e.setCancelled(true);
            return;
        }

        if(GameManager.get().state().equals(GameState.WAITING) || GameManager.get().state().equals(GameState.SHOPPING)) {
            if(e.getTo().getBlockY() < 50) {
                e.setTo(e.getPlayer().getWorld().getSpawnLocation());
            }
            return;
        }

        if(GameManager.get().state().equals(GameState.RUNNING)) {

            if(e.getPlayer().getLocation().getBlockY() <= 0) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if(GameManager.get().state().equals(GameState.RUNNING)) {
                        if(e.getPlayer().getLocation().getBlockY() <= 1) {
                            if(GameManager.get().playersFinished.contains(e.getPlayer())) return;
                            GameManager.get().playersFinished.add(e.getPlayer());
                            int finishedCount = GameManager.get().playersFinished.size();
                            if(finishedCount == 1) {
                                GameManager.get().playerCoinManager().addCoins(e.getPlayer(), 300);
                                e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("§6+300"));
                            } else if(finishedCount == 2) {
                                GameManager.get().playerCoinManager().addCoins(e.getPlayer(), 200);
                                e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("§6+200"));
                            } else if(finishedCount == 3) {
                                GameManager.get().playerCoinManager().addCoins(e.getPlayer(), 100);
                                e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("§6+100"));
                            }

                            Bukkit.getOnlinePlayers().forEach(p -> {
                                p.sendMessage(Component.text(GetDown.instance().prefix() + "§f"+e.getPlayer().getName()+" §7ist unten angekommen! ("+finishedCount+"/3)"));
                                p.playSound(p, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1.0f, 1.0f);
                            });

                            if(finishedCount >= 3 || finishedCount == Bukkit.getOnlinePlayers().size()) {
                                GameManager.get().state(GameState.FINISHED);

                                AtomicInteger seconds = new AtomicInteger(10);
                                AtomicInteger taskId = new AtomicInteger(0);
                                taskId.set(Bukkit.getScheduler().runTaskTimer(GetDown.instance(), () -> {
                                    if(seconds.get() <= 0) {
                                        GameManager.get().startShopping();
                                        Bukkit.getScheduler().cancelTask(taskId.get());
                                    } else {
                                        if(seconds.get() == 10 || seconds.get() <= 5) {
                                            Bukkit.getOnlinePlayers().forEach(p -> {
                                                p.sendMessage(Component.text(GetDown.instance().prefix() + "Die Shopping Phase beginnt in §f" + seconds.get() + " §7Sekunden!"));
                                                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                                            });
                                        }
                                    }
                                    seconds.getAndDecrement();
                                }, 0L, 20L).getTaskId());
                            }
                        }
                    }
                }, 2L);
                return;
            }

            Block block = e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0);
            Block block2 = e.getPlayer().getLocation().getBlock().getRelative(0, -2, 0);
            Block block3 = e.getPlayer().getLocation().getBlock().getRelative(0, -3, 0);

            if(block2.getType().equals(Material.OBSIDIAN)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -2, 0).equals(block2)) return;
                    if (Math.random() * 100 < GameManager.get().percentage()) {
                        block2.setType(Material.SLIME_BLOCK);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§aDu bist auf einen Slime Block gefallen!"));
                        Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                            if (block2.getType().equals(Material.SLIME_BLOCK)) {
                                block2.setType(Material.COBBLESTONE);
                            }
                        }, 5 * 20L);
                    } else {
                        block2.setType(Material.COBBLESTONE);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§cDu bist auf einen normalen Block gefallen!"));
                    }
                }, 1L);
            }
            if(block3.getType().equals(Material.OBSIDIAN)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -2, 0).equals(block3)) return;
                    if (Math.random() * 100 < GameManager.get().percentage()) {
                        block3.setType(Material.SLIME_BLOCK);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§aDu bist auf einen Slime Block gefallen!"));
                        Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                            if (block3.getType().equals(Material.SLIME_BLOCK)) {
                                block3.setType(Material.COBBLESTONE);
                            }
                        }, 5 * 20L);
                    } else {
                        block3.setType(Material.COBBLESTONE);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§cDu bist auf einen normalen Block gefallen!"));
                    }
                }, 1L);
            }

            if(block.getType().equals(Material.GOLD_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).equals(block)) return;
                    int min = 25;
                    int max = 55;
                    int coins = (int) (Math.random() * (max - min + 1)) + min;
                    GameManager.get().playerCoinManager().addCoins(e.getPlayer(), coins);
                    e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                    e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("§6+" + coins));
                    block.setType(Material.YELLOW_CONCRETE);
                }, 1);
            } else if(block.getType().equals(Material.DIAMOND_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).equals(block)) return;
                    int min = 75;
                    int max = 115;
                    int coins = (int) (Math.random() * (max - min + 1)) + min;
                    GameManager.get().playerCoinManager().addCoins(e.getPlayer(), coins);
                    e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("§a+" + coins));
                    e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.0f);
                    block.setType(Material.LIGHT_BLUE_CONCRETE);
                }, 1L);
            } else if(block.getType().equals(Material.IRON_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).equals(block)) return;
                    ItemStack randomItem = ItemSetting.getRandomItem();
                    if (randomItem != null) {
                        e.getPlayer().getInventory().addItem(randomItem);
                        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.0f, 1.0f);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§aDu hast ein Item erhalten: §f" + randomItem.getType().name()));
                    }
                    block.setType(Material.GRAY_CONCRETE);
                }, 1L);
            } else if(block.getType().equals(Material.OBSIDIAN)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).equals(block)) return;
                    if (Math.random() * 100 < GameManager.get().percentage()) {
                        block.setType(Material.SLIME_BLOCK);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§aDu bist auf einen Slime Block gefallen!"));
                        Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                            if (block.getType().equals(Material.SLIME_BLOCK)) {
                                block.setType(Material.COBBLESTONE);
                            }
                        }, 5 * 20L);
                    } else {
                        block.setType(Material.COBBLESTONE);
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§cDu bist auf einen normalen Block gefallen!"));
                    }
                }, 1L);
            } else if(block.getType().equals(Material.LAPIS_BLOCK)) {
                Bukkit.getScheduler().runTaskLater(GetDown.instance(), () -> {
                    if (!e.getPlayer().getLocation().getBlock().getRelative(0, -1, 0).equals(block)) return;
                    GameEffect effect = EffectSetting.getRandomEffect();
                    if (effect != null) {
                        e.getPlayer().addPotionEffect(new PotionEffect(effect.type(), effect.duration() * 20, effect.amplifier()));
                        e.getPlayer().sendMessage(Component.text(GetDown.instance().prefix() + "§aDu hast einen Effekt erhalten: §f" + effect.type().translationKey() + " §7(" + effect.duration() + " Sekunden, Stufe " + (effect.amplifier() + 1) + ")"));
                        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.0f, 1.0f);
                    }
                    block.setType(Material.CYAN_CONCRETE);
                }, 1L);
            }

        }

    }

}
