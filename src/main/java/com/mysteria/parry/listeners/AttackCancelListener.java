package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.ParryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackCancelListener implements Listener {

	public AttackCancelListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onStaggeredAttack(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity) {

			ParryManager manager = MysteriaParryPlugin.getInstance().getParryManager();

			// Cancelling attacks of entity while staggered
			if (manager.isStaggered((LivingEntity) e.getDamager())) {
				e.setCancelled(true);
				return;
			}

			// Cancelling attacks of player while parrying
			if (e.getDamager() instanceof Player) {
				if (manager.inFrames((Player) e.getDamager())) {
					e.setCancelled(true);
				}
			}

		}

	}
}
