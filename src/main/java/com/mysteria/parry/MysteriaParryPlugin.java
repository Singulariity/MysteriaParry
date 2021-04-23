package com.mysteria.parry;

import com.mysteria.parry.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysteriaParryPlugin extends JavaPlugin {

	private static MysteriaParryPlugin instance;
	private ParryManager parryManager;

	public MysteriaParryPlugin() {
		if (instance != null) throw new IllegalStateException();
		instance = this;
	}

	@Override
	public void onEnable() {
		parryManager = new ParryManager();

		registerListeners();
	}


	private void registerListeners() {
		new AttackCancelListener();
		new InteractCancelListener();
		new ItemPickupCancelListener();
		new GuardBreakListener();
		new ParryListener();
		new RiposteListener();
		new RightClickShieldListener();
	}


	public static MysteriaParryPlugin getInstance() {
		if (instance == null) throw new IllegalStateException();
		return instance;
	}

	public ParryManager getParryManager() {
		return parryManager;
	}

}
