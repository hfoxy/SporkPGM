package io.sporkpgm.filter;

import io.sporkpgm.filter.other.Context;
import io.sporkpgm.filter.other.State;

public abstract class Filter {

	public abstract State result(Context context);

}
