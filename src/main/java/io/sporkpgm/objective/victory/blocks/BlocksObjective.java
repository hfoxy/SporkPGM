package io.sporkpgm.objective.victory.blocks;

import com.mcath.manager.AthenaManager;
import com.mcath.manager.module.InitModule;
import com.mcath.manager.module.ModuleInfo;
import com.mcath.manager.module.skyfall.SkyfallModule;
import com.mcath.manager.objective.ObjectiveModule;
import com.mcath.manager.objective.victory.VictoryObjective;
import com.mcath.manager.team.AthenaTeam;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

@ModuleInfo(name = "BlocksObjective", description = "Legacy support for Skyfall and Victory in 1 objective type", listener = false, multiple = true)
public class BlocksObjective extends ObjectiveModule implements InitModule {

	VictoryObjective objective;
	SkyfallModule skyfall;

	public BlocksObjective(VictoryObjective objective, SkyfallModule skyfall) {
		this.objective = objective;
		this.skyfall = skyfall;
	}

	public OfflinePlayer getPlayer() {
		return objective.getPlayer();
	}

	public boolean isComplete() {
		return objective.isComplete();
	}

	public void setComplete(boolean complete) {
		objective.setComplete(complete);
	}

	public AthenaTeam getTeam() {
		return objective.getTeam();
	}

	public ChatColor getStatusColour() {
		return objective.getStatusColour();
	}

	public void update() {
		objective.update();
	}

	public void start() {
		AthenaManager.registerListener(objective);
		AthenaManager.registerListener(skyfall);
		skyfall.setTasks(true);
		objective.start();
	}

	public void stop() {
		AthenaManager.unregisterListener(objective);
		AthenaManager.unregisterListener(skyfall);
		skyfall.setTasks(false);
		objective.stop();
	}

}
