package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.ParryManager;
import com.mysteria.parry.events.ParryEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class ParryListener implements Listener {

	public ParryListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onEntityDamage(EntityDamageByEntityEvent e) {

		// Return if victim is not a player
		if (!(e.getEntity() instanceof Player)) return;

		// Return if attack is not a melee attack
		if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

		// Return if attacker is not a living entity or not a valid living entity
		if (!(e.getDamager() instanceof LivingEntity) || !e.getDamager().isValid()) return;

		Player victim = (Player) e.getEntity();
		ParryManager manager = MysteriaParryPlugin.getInstance().getParryManager();

		// Return if victim is not parrying
		if (!manager.isParrying(victim)) return;

		LivingEntity attacker = (LivingEntity) e.getDamager();

		ParryEvent parryEvent = new ParryEvent(victim, attacker);
		Bukkit.getPluginManager().callEvent(parryEvent);

		if (!parryEvent.isCancelled()) {
			// Cancel the original event if parry event is not cancelled
			e.setCancelled(true);

			manager.addStaggered(attacker, 40);

			// TODO
			//CustomSound.play(attacker.getLocation(), CustomSound.COMBAT_PARRY, 0.8f, 1);
			if (attacker instanceof Player) {
				((Player) attacker).getInventory().setHeldItemSlot(new Random().nextInt(9));
			}
			attacker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15, 1, false, false));
			attacker.setNoDamageTicks(15);
			attacker.setVelocity(new Vector().zero());
		}

	}

}
