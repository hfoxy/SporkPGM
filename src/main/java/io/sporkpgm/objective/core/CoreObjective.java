package io.sporkpgm.objective.core;

import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.objective.ObjectiveModule;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

@ModuleInfo(name = "CoreObjective", description = "Adds support for Destroy the Core (DTC)")
public class CoreObjective extends ObjectiveModule {
	int leak;


	@Override
	public SporkTeam getTeam() {
		return null;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void setComplete(boolean complete) {

	}

	@Override
	public OfflinePlayer getPlayer() {
		return null;
	}

	@Override
	public void update() {

	}

	@Override
	public ChatColor getStatusColour() {
		return null;
	}

	@Override
	public Class<? extends Builder> builder() {
		return null;
	}
}
