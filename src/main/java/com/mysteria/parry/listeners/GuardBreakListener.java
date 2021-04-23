package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.events.GuardBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

public class GuardBreakListener implements Listener {

	public GuardBreakListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	private final HashMap<UUID, Double> DAMAGES = new HashMap<>();

	@EventHandler(ignoreCancelled = true)
	private void onEntityDamage(EntityDamageByEntityEvent e) {

		// Return if victim is not a player
		if (!(e.getEntity() instanceof Player)) return;

		// Return if attack is not a melee attack
		if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

		// Return if attacker is not a living victim or not a valid living victim
		if (!(e.getDamager() instanceof LivingEntity) || !e.getDamager().isValid()) return;

		Player victim = (Player) e.getEntity();

		// Return if attack is not blocked by a shield
		if (e.getFinalDamage() != 0 || !victim.isBlocking()) return;

		addDamage(victim, e.getDamage());
		if (!(e.getDamager() instanceof Player)) victim.setNoDamageTicks(5);

		if (DAMAGES.get(victim.getUniqueId()) < 25) return;

		GuardBreakEvent guardBreakEvent = new GuardBreakEvent(victim);
		Bukkit.getPluginManager().callEvent(guardBreakEvent);

		if (!guardBreakEvent.isCancelled()) {
			// Cancel the original event if guard break event is not cancelled
			e.setCancelled(true);

			MysteriaParryPlugin.getInstance().getParryManager().addStaggered(victim, 40);

			victim.getWorld().playSound(victim.getLocation(), "item.shield.break", 1, 0.6F);
			victim.setCooldown(Material.SHIELD, 100);

			ItemStack mainHandItem = victim.getInventory().getItemInMainHand();
			ItemStack offHandItem = victim.getInventory().getItemInOffHand();
			victim.getInventory().setItemInOffHand(null);
			victim.getInventory().setItemInMainHand(null);

			new BukkitRunnable() {
				@Override
				public void run() {
					victim.getInventory().setItemInMainHand(mainHandItem);
					victim.getInventory().setItemInOffHand(offHandItem);
				}
			}.runTaskLater(MysteriaParryPlugin.getInstance(), 1);
			victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15, 1, false, false));
			victim.setNoDamageTicks(15);
			victim.setVelocity(new Vector().zero());
		}

	}


	private void addDamage(@Nonnull Player p, double damage) {
		UUID uuid = p.getUniqueId();
		if (DAMAGES.containsKey(uuid)) {
			DAMAGES.put(uuid, DAMAGES.get(uuid) + damage);
		} else {
			DAMAGES.put(uuid, damage);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				DAMAGES.put(uuid, DAMAGES.get(uuid) - damage);
			}
		}.runTaskLater(MysteriaParryPlugin.getInstance(), 40);
	}

}
