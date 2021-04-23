package com.mysteria.parry.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;


/**
 * Called when an entity's guard broken
 */
@SuppressWarnings("unused")
public class GuardBreakEvent extends Event implements Cancellable {

	private final LivingEntity entity;
	private boolean isCancelled;

	public GuardBreakEvent(@Nonnull LivingEntity entity) {
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

	public LivingEntity getEntity() {
		return entity;
	}
}
