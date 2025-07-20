package tv.logisch.game.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import tv.logisch.game.GetDown;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow arrow)) {
            return;
        }
        if (!(e.getEntity() instanceof Player p)) {
            return;
        }

        NamespacedKey key = new NamespacedKey("logisch", "custom_arrow");
        if(!arrow.getPersistentDataContainer().has(key)) return;

        String type = arrow.getPersistentDataContainer().get(key, org.bukkit.persistence.PersistentDataType.STRING);
        if (type == null || type.isEmpty()) {
            return;
        }

        if(type.equalsIgnoreCase("knockback_arrow_1")) {
            if(!(arrow.getShooter() instanceof LivingEntity shooter)) {
                return;
            }
            Vector direction = p.getLocation().toVector().subtract(shooter.getLocation().toVector()).normalize();
            double knockbackStrength = 1.5;
            direction.setY(0.3);

            p.setVelocity(direction.multiply(knockbackStrength));
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Knockback Arrow getroffen!"));
        } else if(type.equalsIgnoreCase("knockback_arrow_3")) {
            if(!(arrow.getShooter() instanceof LivingEntity shooter)) {
                return;
            }
            Vector direction = p.getLocation().toVector().subtract(shooter.getLocation().toVector()).normalize();
            double knockbackStrength = 3.0;
            direction.setY(0.5);

            p.setVelocity(direction.multiply(knockbackStrength));
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Knockback Arrow getroffen!"));
        } else if(type.equalsIgnoreCase("damage_arrow_1")) {
            e.setDamage(1.0);
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Damage Arrow getroffen! §8(§c-1 Herz§8)"));
        } else if(type.equalsIgnoreCase("damage_arrow_2")) {
            e.setDamage(2.0);
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Damage Arrow getroffen! §8(§c-2 Herzen§8)"));
        } else if(type.equalsIgnoreCase("damage_arrow_3")) {
            e.setDamage(3.0);
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Damage Arrow getroffen! §8(§c-3 Herzen§8)"));
        } else if(type.equalsIgnoreCase("levitation_arrow")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 5*20, 1));
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Levitation Arrow getroffen! §8(§c5 Sekunden Levitation§8)"));
        } else if(type.equalsIgnoreCase("poison_arrow")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 1));
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Poison Arrow getroffen! §8(§c5 Sekunden Gift§8)"));
        } else if(type.equalsIgnoreCase("fire_arrow")) {
            p.setFireTicks(5 * 20);
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Fire Arrow getroffen! §8(§c5 Sekunden Feuer§8)"));
        } else if(type.equalsIgnoreCase("blindness_arrow")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 1));
            p.sendMessage(Component.text(GetDown.instance().prefix()+"Du wurdest von einem Blindness Arrow getroffen! §8(§c5 Sekunden Blindheit§8)"));
        }

    }

}
