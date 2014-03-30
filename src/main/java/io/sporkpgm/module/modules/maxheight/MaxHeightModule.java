package io.sporkpgm.module.modules.maxheight;

import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

@ModuleInfo(name = "MaxHeightModule", description = "Prevents players from building above a defined height", multiple = false)
public class MaxHeightModule extends Module {

	int height;

	public MaxHeightModule(int height) {
		this.height = height;
	}

	@EventHandler
	public void onBlockChange(BlockChangeEvent event) {
		if(!event.hasPlayer()) {
			return;
		}

		if(event.getLocation().getBlockY() > height) {
			event.setCancelled(true);
			event.getPlayer().getPlayer().sendMessage(ChatColor.RED + "You have reached the maximum build height");
		}
	}

	public Class<? extends Builder> builder() {
		return null;
	}

}
