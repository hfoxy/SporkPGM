package io.sporkpgm.objective.monument;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import org.dom4j.Document;

import java.util.List;

public class MonumentBuilder extends Builder{
	public MonumentBuilder(Document document){
		super(document);
	}

	public MonumentBuilder(SporkMap map){
		super(map);
	}

	@Override
	public List<Module> build() throws ModuleLoadException, InvalidRegionException{
		return null;
	}
}
