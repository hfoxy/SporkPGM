package io.sporkpgm.filter;

import io.sporkpgm.filter.other.Context;
import io.sporkpgm.filter.other.State;
import io.sporkpgm.map.event.BlockChangeEvent;
import io.sporkpgm.player.event.PlayingPlayerMoveEvent;
import io.sporkpgm.region.Region;
import io.sporkpgm.region.types.groups.UnionRegion;
import io.sporkpgm.team.spawns.kits.SporkKit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppliedRegion extends UnionRegion {

	private Map<AppliedValue, Object> values;
	private List<Filter> filters;

	public AppliedRegion(String name, Map<AppliedValue, Object> values, List<Region> regions, List<Filter> filters) {
		super("filtered-" + name + "-union", regions);
		this.values = values;
		this.filters = filters;
	}

	public void apply(Context context) {
		apply(context, false);
	}

	public void apply(Context context, boolean log) {
		if(context.isDenied()) {
			return;
		}

		List<AppliedValue> applied = new ArrayList<>();
		if(context.hasMovement()) {
			PlayingPlayerMoveEvent move = context.getMovement();
			if(isInside(move.getFrom(), log) && isInside(move.getTo(), log)) {
				return;
			} else if(!isInside(move.getFrom(), log) && !isInside(move.getTo(), log)) {
				return;
			} else if(!isInside(move.getFrom(), log) && isInside(move.getTo(), log)) {
				applied.add(AppliedValue.ENTER);
				applied.add(AppliedValue.KIT);
				applied.add(AppliedValue.VELOCITY);
			} else if(isInside(move.getFrom(), log) && !isInside(move.getTo(), log)) {
				applied.add(AppliedValue.LEAVE);
			}
		}

		if(context.hasModification()) {
			BlockChangeEvent block = context.getModification();

			if(isInside(block.getLocation(), log)) {
				applied.add(AppliedValue.BLOCK);
				if(block.hasPlayer() && block.isBreak()) {
					applied.add(AppliedValue.BLOCK_BREAK);
				} else if(block.hasPlayer() && block.isPlace()) {
					applied.add(AppliedValue.BLOCK_PLACE);
				}
			}
		}

		/*
		if(applied.size() > 0) {
			Log.info(getName() + ": " + applied);
		}
		*/

		String message = null;
		if(hasValue(AppliedValue.MESSAGE)) {
			message = (String) values.get(AppliedValue.MESSAGE);
			message = ChatColor.RED + message.replace("`", "§").replace("&", "§");
		}

		if(applied.contains(AppliedValue.ENTER) || applied.contains(AppliedValue.LEAVE)) {
			if(hasValue(AppliedValue.ENTER)) {
				Filter filter = (Filter) getValue(AppliedValue.ENTER);
				if(filter.result(context) == State.DENY) {
					context.deny();
					if(message != null) {
						context.getPlayer().getPlayer().sendMessage(message);
					}
				}
			} else if(hasValue(AppliedValue.LEAVE)) {
				Filter filter = (Filter) getValue(AppliedValue.LEAVE);
				if(filter.result(context) == State.DENY) {
					context.deny();
					if(message != null) {
						context.getPlayer().getPlayer().sendMessage(message);
					}
				}
			}
		}

		if(applied.contains(AppliedValue.BLOCK)) {
			if(hasValue(AppliedValue.BLOCK)) {
				Filter filter = (Filter) getValue(AppliedValue.BLOCK);
				if(filter.result(context) == State.DENY) {
					context.deny();
				}
			}

			if(hasValue(AppliedValue.BLOCK_BREAK)) {
				Filter filter = (Filter) getValue(AppliedValue.BLOCK_BREAK);
				if(filter.result(context) == State.DENY) {
					context.deny();
					if(message != null) {
						context.getPlayer().getPlayer().sendMessage(message);
					}
				}
			} else if(hasValue(AppliedValue.BLOCK_PLACE)) {
				Filter filter = (Filter) getValue(AppliedValue.BLOCK_PLACE);
				if(filter.result(context) == State.DENY) {
					context.deny();
					if(message != null) {
						context.getPlayer().getPlayer().sendMessage(message);
					}
				}
			}
		}

		if(applied.contains(AppliedValue.KIT)) {
			if(hasValue(AppliedValue.KIT)) {
				SporkKit sporkKit = (SporkKit) values.get(AppliedValue.KIT);
				sporkKit.apply(context.getPlayer());
			}
		}

		// TODO: add support for velocities
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
		VELOCITY("velocity", String.class),
		UNKNOWN("unknown", Object.class);

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