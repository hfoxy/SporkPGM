package io.sporkpgm.filter;

import io.sporkpgm.util.Log;

public abstract class Condition implements Filter {

	private String name;
	private State state;

	public Condition(String name, State state) {
		this.name = name;
		this.state = state;
	}

	public abstract State match(FilterContext context);

	@Override
	public State matches(FilterContext context) {
		State match = match(context);
		Log.info(match.toBoolean() + ": " + match.name() + " for checking matches on " + getClass().getSimpleName());

		Log.info(state.toBoolean() + ": " + state.name() + " for checking matches on " + getClass().getSimpleName());
		if(state.toBoolean()) {
			return match;
		} else {
			return match.reverse();
		}
	}

	public State getState() {
		return state;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Condition{state=" + state.toString() + "}";
	}

}
