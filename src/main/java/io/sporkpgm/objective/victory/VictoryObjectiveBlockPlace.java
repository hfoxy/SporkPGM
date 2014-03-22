package io.sporkpgm.objective.victory;

import com.mcath.manager.player.AthenaPlayer;
import com.mcath.manager.region.types.BlockRegion;
import org.bukkit.Location;

public class VictoryObjectiveBlockPlace {

	private AthenaPlayer player;

	private BlockRegion region;

	public VictoryObjectiveBlockPlace(BlockRegion region) {
		this(null, region);
	}

	public VictoryObjectiveBlockPlace(Location location) {
		this(null, location);
	}

	public VictoryObjectiveBlockPlace(AthenaPlayer player, Location location) {
		this(player, new BlockRegion(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
	}

	public VictoryObjectiveBlockPlace(AthenaPlayer player, BlockRegion region) {
		this.player = player;
		this.region = region;
	}

	public AthenaPlayer getPlayer() {
		return player;
	}

	public BlockRegion getRegion() {
		return region;
	}

}
