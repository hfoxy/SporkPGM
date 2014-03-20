package io.sporkpgm.objective.scored;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.team.SporkTeam;
import io.sporkpgm.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class ScoredBuilder extends Builder {

	public ScoredBuilder(Document document) {
		super(document);
	}

	public ScoredBuilder(SporkMap map) {
		super(map);
	}

	public List<Module> build() throws ModuleLoadException {
		Element root = map.getDocument().getRootElement();
		List<Module> objectives = new ArrayList<>();
		Element scoreElement = root.element("score");

		if(scoreElement != null) {
			int limit = StringUtil.convertStringToInteger(scoreElement.attributeValue("limit"), ScoredObjective.NO_LIMIT);
			for(SporkTeam team : map.getTeams()) {
				objectives.add(new ScoredObjective(team, limit));
			}
		}

		return objectives;
	}

}
