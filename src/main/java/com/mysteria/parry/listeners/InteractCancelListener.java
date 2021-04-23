package com.mysteria.parry.listeners;

import com.mysteria.parry.MysteriaParryPlugin;
import com.mysteria.parry.ParryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractCancelListener implements Listener {

	public InteractCancelListener() {
		Bukkit.getPluginManager().registerEvents(this, MysteriaParryPlugin.getInstance());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onInteract(PlayerInteractEvent e) {
		ParryManager manager = MysteriaParryPlugin.getInstance().getParryManager();
		Player p = e.getPlayer();

		if (manager.inFrames(p) || manager.isStaggered(p)) {
			e.setCancelled(true);
		}

	}

}
