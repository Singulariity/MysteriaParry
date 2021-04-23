package com.mysteria.parry.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Called when a player parried an entity
 */
@SuppressWarnings("unused")
public class ParryEvent extends Event implements Cancellable {

	private final Player whoParried;
	private final LivingEntity entity;
	private boolean isCancelled;

	public ParryEvent(@Nullable Player whoParried, @Nonnull LivingEntity entity) {
		this.whoParried = whoParried;
		this.entity = entity;
		this.isCancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public @Nonnull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Nullable
	public Player getWhoParried() {
		return whoParried;
	}

	@Nonnull
	public LivingEntity getEntity() {
		return entity;
	}

}
