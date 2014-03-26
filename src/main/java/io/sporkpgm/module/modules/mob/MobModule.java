package io.sporkpgm.module.modules.mob;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

@ModuleInfo(name = "MobMoudle", description = "The module that controls mob spawning")
public class MobModule extends Module{

	List<CreatureType> mobs = null;
	List<CreatureSpawnEvent.SpawnReason> reasons = null;

	public MobModule(List<CreatureType> mobs,List<CreatureSpawnEvent.SpawnReason> reasons){
		this.mobs = mobs;
		this.reasons = reasons;
	}

	public List<CreatureType> getMobs(){
		return mobs;
	}

	public List<CreatureSpawnEvent.SpawnReason> getReasons(){
		return reasons;
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event){
		if (mobs == null && reasons == null){
			event.setCancelled(true);
			return;
		}
	}

	@Override
	public Class<? extends Builder> builder(){
		return MobBuilder.class;

	}
}
