package io.sporkpgm.region;

import io.sporkpgm.filter.Filter;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.region.types.groups.UnionRegion;
import io.sporkpgm.team.spawns.kits.SporkKit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;

public class FilteredRegion extends UnionRegion {

	private Map<AppliedValue, Object> values;
	private List<Filter> filters;

	public FilteredRegion(String name, Map<AppliedValue, Object> values, List<Region> regions, List<Filter> filters) {
		super("filtered-" + name + "-union", regions);
		this.values = values;
		this.filters = filters;
	}

	public void apply(AppliedValue value, SporkPlayer player) {
		if(value == AppliedValue.ENTER || value == AppliedValue.LEAVE) {
			Object msg = values.get(AppliedValue.MESSAGE);
			if(msg != null) {
				String message = (String) msg;
				message = message.replace("`", "§").replace("&", "§");

				player.getPlayer().sendMessage(ChatColor.RED + message);
			}

			return;
		}

		Object kit = values.get(AppliedValue.KIT);
		if(kit != null) {
			SporkKit sporkKit = (SporkKit) kit;
			sporkKit.apply(player);
		}

		// add support for velocities
	}

	public Map<AppliedValue, Object> getHashMap() {
		return values;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public boolean hasValue(AppliedValue value) {
		return getValue(value) != null;
	}

	public Object getValue(AppliedValue value) {
		return values.get(value);
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
		VELOCITY("velocity", String.class);

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

		public static String[] getAttributes() {
			String[] attributes = new String[values().length];
			for(int i = 0; i < values().length; i++) {
				attributes[i] = values()[i].name();
			}
			return attributes;
		}

	}

}
