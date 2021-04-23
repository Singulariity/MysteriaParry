package com.mysteria.parry.tasks;

import com.mysteria.parry.MysteriaParryPlugin;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

public class StaggerTask extends BukkitRunnable {

	private final LivingEntity entity;
	private final Location location;
	private final int duration;
	private int timer;

	public StaggerTask(@Nonnull LivingEntity entity, int duration) {
		this.entity = entity;
		this.location = entity.getLocation();
		this.duration = duration;
		this.timer = 0;
		runTaskTimer(MysteriaParryPlugin.getInstance(), 0, 1);
	}

	public void run() {

		if (!entity.isValid() || timer > duration) {
			MysteriaParryPlugin.getInstance().getParryManager().removeStaggered(entity);
			return;
		}

		entity.teleport(location);
		timer++;

	}

}