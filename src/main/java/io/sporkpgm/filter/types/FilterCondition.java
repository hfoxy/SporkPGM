package io.sporkpgm.filter.types;

import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.Filter;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;
import io.sporkpgm.match.Match;
import io.sporkpgm.team.SporkTeam;

public class FilterCondition extends Condition {

	private String search;
	private Filter filter;

	public FilterCondition(String name, State state, String filter) {
		super(name, state);
		this.search = filter;
	}

	@Override
	public State match(FilterContext context) {
		if(filter == null) {
			filter = Match.getMatch().getMap().getFilter(search);
		}

		if(filter == null) {
			return State.ABSTAIN;
		}

		return filter.matches(context);
	}

	@Override
	public String toString() {
		return "AccessCondition{" +
				"filter=" + (filter != null ? filter.toString() : filter)
				+ ",state=" + getState().toString() + "}";
	}

}
