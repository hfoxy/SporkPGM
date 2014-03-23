package io.sporkpgm.filter.types;

import com.google.common.collect.Lists;
import io.sporkpgm.filter.Condition;
import io.sporkpgm.filter.FilterContext;
import io.sporkpgm.filter.Modifier;
import io.sporkpgm.filter.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiCondition extends Condition {

	private Modifier modifier;
	private List<Condition> conditions;

	public MultiCondition(State state, Modifier modifier, Condition... conditions) {
		super(state);
		this.modifier = modifier;
		this.conditions = Lists.newArrayList(conditions);
	}

	private Map<State, Integer> usages(FilterContext context) {
		Map<State, Integer> usages = new HashMap<>();
		for(State state : State.values()) {
			usages.put(state, 0);
		}

		for(Condition condition : conditions) {
			State state = condition.matches(context);
			int value = usages.get(state);
			value++;
			usages.remove(state);
			usages.put(state, value);
		}

		return usages;
	}

	private List<State> states(FilterContext context) {
		List<State> states = new ArrayList<>();

		for(Condition condition : conditions) {
			states.add(condition.matches(context));
		}

		return states;
	}

	public State match(FilterContext context) {
		if(modifier == Modifier.ANY) {
			List<State> states = states(context);

			if(states.contains(State.ALLOW)) {
				return State.ALLOW;
			} else if(!states.contains(State.ALLOW) && !states.contains(State.DENY)) {
				return State.ABSTAIN;
			} else {
				return State.DENY;
			}
		} else if(modifier == Modifier.NOT) {
			return conditions.get(0).matches(context).reverse();
		} else if(modifier == Modifier.ONE) {
			Map<State, Integer> usages = usages(context);

			if(usages.get(State.ALLOW) == 1) {
				return State.ALLOW;
			} else if(usages.get(State.ALLOW) > 1 || usages.get(State.DENY) > 0) {
				return State.DENY;
			} else {
				return State.ABSTAIN;
			}
		} else if(modifier == Modifier.ALL) {
			Map<State, Integer> usages = usages(context);

			if(usages.get(State.ALLOW) == conditions.size()) {
				return State.ALLOW;
			} else if(usages.get(State.DENY) > 0) {
				return State.DENY;
			} else {
				return State.ABSTAIN;
			}
		}

		return State.ABSTAIN;
	}
}
