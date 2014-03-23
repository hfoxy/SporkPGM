package io.sporkpgm.filter;

import io.sporkpgm.filter.types.*;
import io.sporkpgm.map.SporkMap;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {

	public static List<Filter> build(SporkMap map) {
		Document document = map.getDocument();
		Element root = document.getRootElement();
		List<Filter> filters = new ArrayList<>();
		filters.addAll(defaults());

		return filters;
	}

	private static List<Filter> defaults() {
		List<Filter> defaults = new ArrayList<>();

		AccessCondition allowPlayers = new AccessCondition("allow-players", State.DENY, null);
		defaults.add(allowPlayers);
		MultiCondition denyPlayers = new MultiCondition("deny-players", State.ALLOW, Modifier.NOT, allowPlayers);
		defaults.add(denyPlayers);

		WorldCondition allowBlocks = new WorldCondition("allow-blocks", State.DENY);
		defaults.add(allowBlocks);
		MultiCondition denyBlocks = new MultiCondition("deny-blocks", State.ALLOW, Modifier.NOT, allowBlocks);
		defaults.add(denyBlocks);

		BlockCondition allowWorld = new BlockCondition("allow-world", State.DENY, null);
		defaults.add(allowWorld);
		MultiCondition denyWorld = new MultiCondition("deny-world", State.ALLOW, Modifier.NOT, allowWorld);
		defaults.add(denyWorld);

		EntityCondition allowEntities = new EntityCondition("allow-entities", State.DENY, null);
		defaults.add(allowEntities);
		MultiCondition denyEntities = new MultiCondition("deny-entities", State.ALLOW, Modifier.NOT, allowEntities);
		defaults.add(denyEntities);

		/*
		EntityCondition allowMobs = new EntityCondition("allow-entities", State.DENY, EntityType);
		defaults.add(allowMobs);
		MultiCondition denyMobs = new MultiCondition("deny-entities", State.ALLOW, Modifier.NOT, allowEntities);
		defaults.add(denyMobs);
		*/

		return defaults;
	}

}
