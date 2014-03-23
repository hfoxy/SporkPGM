package io.sporkpgm.filter;

import io.sporkpgm.filter.event.FilterTriggerEvent;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

public class FilterContext {

	private SporkTeam team;
	private Block block;
	private EntityType entity;
	private FilterTriggerEvent triggerEvent;
	private Event cause;

	public FilterContext(FilterTriggerEvent triggerEvent, SporkTeam team, Block block, EntityType entity) {
		this.triggerEvent = triggerEvent;
		this.team = team;
		this.block = block;
		this.entity = entity;
	}

	public FilterContext(Object... objects) {
		for(Object o : objects) {
			if(o instanceof SporkTeam) {
				this.team = (SporkTeam) o;
			} else if(o instanceof Block) {
				this.block = (Block) o;
			} else if(o instanceof EntityType) {
				this.entity = (EntityType) o;
			} else if(o instanceof FilterTriggerEvent) {
				this.triggerEvent = (FilterTriggerEvent) o;
			} else if(o instanceof Event) {
				this.cause = (Event) o;
			}
		}
	}

	public SporkTeam getTeam() {
		return team;
	}

	public Block getBlock() {
		return block;
	}

	public EntityType getEntity() {
		return entity;
	}

	public FilterTriggerEvent getEvent() {
		return triggerEvent;
	}

	public Event getCause() {
		return cause;
	}

	@Override
	public String toString() {
		String toString = "FilterContext{";
		if(team != null) {
			toString += "team=" + team.getName() + ",";
		} else {
			toString += "team=null,";
		}
		if(block != null) {
			toString += "block=" + block.toString() + ",";
		} else {
			toString += "block=null,";
		}
		if(entity != null) {
			toString += "entity=" + entity.toString();
		} else {
			toString += "entity=null";
		}
		toString += "}";
		return toString;
	}
	
}
