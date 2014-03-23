package io.sporkpgm.filter;

public interface Filter {

	String getName();

	State matches(FilterContext context);

}
