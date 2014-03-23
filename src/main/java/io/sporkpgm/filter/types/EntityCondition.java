package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import org.bukkit.entity.EntityType;

public class EntityCondition extends Condition {

	private EntityType entity;

	public EntityCondition(State state, EntityType entity) {
		super(state);
		this.entity = entity;
	}

	@Override
	public State match(FilterContext context) {
		if(context.getEntity() == null) {
			return State.ABSTAIN;
		}
		if(context.getEntity() == entity) {
			return State.ALLOW;
		}
		return State.DENY;
	}

	public EntityType getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return "EntityCondition{entity=" + entity.toString() + ",state=" + getState().toString() + "}";
	}

}
