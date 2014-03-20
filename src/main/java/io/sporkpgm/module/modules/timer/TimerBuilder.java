package io.sporkpgm.module.modules.timer;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class TimerBuilder extends Builder {

	public TimerBuilder(Document document) {
		super(document);
	}

	public TimerBuilder(SporkMap map) {
		super(map);
	}

	public List<Module> build() throws ModuleLoadException {
		List<Module> modules = new ArrayList<>();

		Element root = document.getRootElement();
		Element about = root.element("time");

		if(about != null) {
			Element time_limit = about.element("time-limit");

			int time = -1;
			if(time_limit != null) {
				time = StringUtil.convertStringToInteger(time_limit.getText(), -1);
			}

			modules.add(new TimerModule(time));
			return modules;
		}

		modules.add(new TimerModule(-1));
		return modules;
	}

}
