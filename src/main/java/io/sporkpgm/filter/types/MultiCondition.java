package io.sporkpgm.filter.types;

import com.google.common.collect.Lists;
import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.State;

import java.util.ArrayList;
import java.util.List;

public class MultiCondition extends Condition {

	private List<Condition> conditions;

	public MultiCondition(State state, Condition... conditions) {
		super(state);
		this.conditions = Lists.newArrayList(conditions);
	}

	private List<State> states(FilterContext context) {
		List<State> states = new ArrayList<>();

		for(Condition condition : conditions) {
			states.add(condition.matches(context));
		}

		return states;
	}

	public State match(FilterContext context) {
		List<State> states = states(context);

		if(states.contains(State.ALLOW)) {
			return State.ALLOW;
		} else if(!states.contains(State.ALLOW) && !states.contains(State.DENY)) {
			return State.ABSTAIN;
		} else {
			return State.DENY;
		}
	}
}
