package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupCancelListener implements Listener {

	public ItemPickupCancelListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	@EventHandler(ignoreCancelled = true)
	private void onPickup(EntityPickupItemEvent e) {

		if (MysteriaParryPlugin.getInstance().getParryManager().isStaggered(e.getEntity())) {
			e.setCancelled(true);
		}

	}

}
