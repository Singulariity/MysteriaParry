package com.mysteria.parry.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called when an entity performs riposte on another entity
 */
@SuppressWarnings("unused")
public class RiposteEvent extends Event implements Cancellable {

	private final LivingEntity attacker;
	private final LivingEntity entity;
	private Double damage;
	private boolean isCancelled;

	public RiposteEvent(@Nonnull LivingEntity attacker, @Nonnull LivingEntity entity, @Nonnull Double damage) {
		this.attacker = attacker;
		this.entity = entity;
		this.damage = damage;
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

	@Nonnull
	public LivingEntity getAttacker() {
		return attacker;
	}

	@Nonnull
	public LivingEntity getEntity() {
		return entity;
	}

	@Nonnull
	public Double getDamage() {
		return damage;
	}

	public void setDamage(@Nonnull Double damage) {
		this.damage = damage;
	}

}
