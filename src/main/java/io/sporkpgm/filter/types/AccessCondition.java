package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import io.sporkpgm.team.SporkTeam;

public class AccessCondition extends Condition {

	private SporkTeam team;

	public AccessCondition(String name, State state, SporkTeam team) {
		super(name, state);
		this.team = team;
	}

	@Override
	public State match(FilterContext context) {
		if(context.getTeam() == null)
			return State.ABSTAIN;
		else if(context.getTeam() == team)
			return State.ALLOW;
		else
			return State.DENY;
	}

	public SporkTeam getTeam() {
		return team;
	}

	@Override
	public String toString() {
		return "AccessCondition{team=" + team.getName() + ",state=" + getState().toString() + "}";
	}

}
