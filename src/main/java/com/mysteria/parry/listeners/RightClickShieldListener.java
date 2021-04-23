package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.ParryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class RightClickShieldListener implements Listener {

	public RightClickShieldListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	@EventHandler
	private void onRightClickShield(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		ParryManager manager = MysteriaParryPlugin.getInstance().getParryManager();

		if (
				p.getInventory().getItemInOffHand().getType() != Material.SHIELD
						|| !p.isSneaking()
						|| (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
						|| p.getLocation().add(0, -0.1, 0).getBlock().isPassable()
						|| manager.inFrames(p)
						|| manager.isStaggered(p)
		) return;

		e.setCancelled(true);

		HashMap<Player, Integer> frames = manager.getParryingFrames();

		int cd = 20;

		p.setCooldown(Material.SHIELD, cd);
		p.setVelocity(new Vector().zero());

		new BukkitRunnable() {
			@Override
			public void run() {

				ItemStack shield = p.getInventory().getItemInOffHand();
				p.getInventory().setItemInOffHand(null);

				new BukkitRunnable() {
					@Override
					public void run() {

						if (p.getInventory().getItemInOffHand().getType() == Material.AIR) {
							p.getInventory().setItemInOffHand(shield);
						} else {
							if (p.getInventory().firstEmpty() != -1) {
								p.getInventory().addItem(shield);
							} else {
								p.getWorld().dropItem(p.getLocation(), shield)
										.setOwner(p.getUniqueId());
							}
						}

					}
				}.runTaskLater(MysteriaParryPlugin.getInstance(), 4);

			}
		}.runTaskLater(MysteriaParryPlugin.getInstance(), 5);


		new BukkitRunnable() {
			int i = 0;
			final Location loc = p.getLocation();
			@Override
			public void run() {
				if (i >= cd || !p.isValid()) {
					frames.remove(p);
					this.cancel();
					return;
				}
				p.teleport(loc);
				frames.put(p, i);
				i++;
			}
		}.runTaskTimer(MysteriaParryPlugin.getInstance(), 0, 1);






	}


}
