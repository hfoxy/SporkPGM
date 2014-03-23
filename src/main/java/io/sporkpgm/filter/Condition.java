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
		if(!state.toBoolean()) {
			match = match.reverse();
		}

		debug(match, context);
		return match;
	}

	private void debug(State state, FilterContext context) {
		Log.info(state.toBoolean() + ": " + state.name() + " for checking matches on '" + getName() + "' (" + toString() + ") with context " + context);
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
		return getClass().getSimpleName() +  "{state=" + state.toString() + "}";
	}

}
