package io.sporkpgm.module.modules.difficulty;


import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleStage;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(documentable = false, stage = ModuleStage.LOAD)
public class DifficultyBuilder extends Builder {

    public DifficultyBuilder(Document document) {
        super(document);
    }

    @Override
    public List<Module> build() throws ModuleLoadException, InvalidRegionException {
        List<Module> modules = new ArrayList<>();
        Element root = getRoot();

        int difficulty = StringUtil.convertStringToInteger(root.elementText("difficulty"), 1);

        modules.add(new DifficultyModule(difficulty));
        return modules;
    }

}
