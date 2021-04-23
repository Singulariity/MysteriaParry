package com.mysteria.parry;

import com.mysteria.parry.tasks.StaggerTask;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

@SuppressWarnings("unused")
public class ParryManager {

	private final HashMap<Player, Integer> parryingFrames = new HashMap<>();
	private final HashMap<LivingEntity, StaggerTask> staggeredEntities = new HashMap<>();

	protected ParryManager() {
		if (MysteriaParryPlugin.getInstance().getParryManager() != null) {
			throw new IllegalStateException();
		}
	}

	public HashMap<Player, Integer> getParryingFrames() {
		return parryingFrames;
	}

	public boolean isStaggered(@Nullable LivingEntity entity) {
		return entity != null && staggeredEntities.containsKey(entity);
	}

	public void addStaggered(@Nonnull LivingEntity entity, int duration) {
		removeStaggered(entity);
		staggeredEntities.put(entity, new StaggerTask(entity, duration));
	}

	public void removeStaggered(@Nullable LivingEntity entity) {
		if (isStaggered(entity)) {
			staggeredEntities.get(entity).cancel();
			staggeredEntities.remove(entity);
		}
	}

	public boolean isParrying(@Nullable Player p) {
		if (p != null) {
			int i = parryingFrames.getOrDefault(p, 0);
			return (5 < i && i <= 9);
		}
		return false;
	}

	public boolean inFrames(@Nullable Player p) {
		return p != null && parryingFrames.containsKey(p);
	}

}
