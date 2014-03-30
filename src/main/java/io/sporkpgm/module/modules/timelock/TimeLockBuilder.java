package io.sporkpgm.module.modules.timelock;

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
public class TimeLockBuilder extends Builder {

    public TimeLockBuilder(Document document) {
        super(document);
    }

    @Override
    public List<Module> build() throws ModuleLoadException, InvalidRegionException {
        List<Module> modules = new ArrayList<>();
        Element root = getRoot();

        boolean enabled = StringUtil.convertStringToBoolean(root.elementText("timelock"), false);

        modules.add(new TimeLockModule(enabled));
        return modules;
    }

}
