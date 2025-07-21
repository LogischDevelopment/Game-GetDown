package tv.logisch.game.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent e) {
        if(!(e.getEntity() instanceof Player p)) return;
        if(e.getBow() == null) return;
        p.getInventory().remove(e.getBow());

        if(!(e.getProjectile() instanceof Arrow arrow)) return;

        ItemStack arrowItem = e.getConsumable();
        NamespacedKey tag = new NamespacedKey("logisch", "custom_arrow");
        if(arrowItem == null || !arrowItem.getPersistentDataContainer().has(tag, PersistentDataType.STRING)) return;

        String type = arrowItem.getPersistentDataContainer().get(tag, org.bukkit.persistence.PersistentDataType.STRING);
        if(type == null || type.isEmpty()) return;
        arrow.getPersistentDataContainer().set(tag, org.bukkit.persistence.PersistentDataType.STRING, type);

    }

}
