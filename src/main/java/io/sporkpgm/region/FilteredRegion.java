package io.sporkpgm.region;

import io.sporkpgm.filter.Filter;
import io.sporkpgm.region.types.BlockRegion;
import io.sporkpgm.region.types.groups.UnionRegion;
import io.sporkpgm.team.spawns.kits.SporkKit;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class FilteredRegion extends Region {

	private Map<AppliedValue, Object> values;
	private UnionRegion region;
	private List<Filter> filters;

	public FilteredRegion(String name, Map<AppliedValue, Object> values, List<Region> regions, List<Filter> filters) {
		super(name);
		this.values = values;
		this.region = new UnionRegion(null, regions);
		this.filters = filters;
	}

	public List<BlockRegion> getValues() {
		return region.getValues();
	}

	public boolean isInside(BlockRegion block) {
		return region.isInside(block);
	}

	public boolean isAbove(BlockRegion block) {
		return region.isAbove(block);
	}

	public boolean isBelow(BlockRegion block) {
		return region.isBelow(block);
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public enum AppliedValue {

		ENTER("enter", Filter.class),
		LEAVE("leave", Filter.class),
		BLOCK("block", Filter.class),
		BLOCK_PLACE("block-place", Filter.class),
		BLOCK_BREAK("block-break", Filter.class),
		USE("use", Filter.class),
		KIT("kit", SporkKit.class),
		MESSAGE("message", String.class),
		VELOCITY("velocity", Vector.class);

		private String attribute;
		private Class<?> returns;

		AppliedValue(String attribute, Class<?> returns) {
			this.attribute = attribute;
			this.returns = returns;
		}

		public String getAttribute() {
			return attribute;
		}

		public Class<?> getReturns() {
			return returns;
		}

	}

}
