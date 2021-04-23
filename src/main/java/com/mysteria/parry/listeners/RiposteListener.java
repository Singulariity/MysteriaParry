package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.ParryManager;
import com.mysteria.parry.events.RiposteEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RiposteListener implements Listener {

	public RiposteListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}


	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	private void onEntityDamage(EntityDamageByEntityEvent e) {

		// Return if attacker is not a living entity
		if (!(e.getDamager() instanceof LivingEntity)) return;

		// Return if attack is not a melee attack
		if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

		// Return if victim is not a living entity or not a valid living entity
		if (!(e.getEntity() instanceof LivingEntity) || !e.getEntity().isValid()) return;

		LivingEntity victim = (LivingEntity) e.getEntity();
		ParryManager manager = MysteriaParryPlugin.getInstance().getParryManager();

		// Return if victim is not in staggered state
		if (!manager.isStaggered(victim)) return;

		LivingEntity attacker = (LivingEntity) e.getDamager();

		RiposteEvent riposteEvent = new RiposteEvent(attacker, victim, e.getDamage());
		Bukkit.getPluginManager().callEvent(riposteEvent);

		if (!riposteEvent.isCancelled()) {
			// Cancel the original event if riposte event is not cancelled
			e.setCancelled(true);

			manager.removeStaggered(victim);

			int damageModifier = 0;
			double damageMultiplier = 1.1;
			// TODO
			//CustomSound.play(victim.getLocation(), CustomSound.COMBAT_RIPOSTE, 0.8f, 1);
			victim.getWorld().playEffect(victim.getLocation().add(0, 1.3, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

			if (attacker instanceof Player) {
				((Player) attacker).playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
			}

			if (victim instanceof Player) {
				if (attacker instanceof Player) {
					damageModifier += 5;
					damageMultiplier += 0.1;
				}
			} else {
				damageModifier += 10;
			}
			victim.damage(riposteEvent.getDamage() * damageMultiplier + damageModifier, attacker);

			attacker.setNoDamageTicks(20);
			victim.setNoDamageTicks(20);

			new BukkitRunnable() {
				@Override
				public void run() {
					Vector vector = victim.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();
					vector.add(new Vector(0, 0.7,0));
					victim.setVelocity(vector.multiply(0.6));
					attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 10, false, false));
				}
			}.runTaskLater(MysteriaParryPlugin.getInstance(), 1);

		}

	}

}
