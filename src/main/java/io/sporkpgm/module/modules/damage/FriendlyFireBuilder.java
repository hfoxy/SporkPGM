package io.sporkpgm.module.modules.damage;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleLoadException;
import io.sporkpgm.region.exception.InvalidRegionException;
import io.sporkpgm.util.StringUtil;
import io.sporkpgm.util.XMLUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(documentable = true)
public class FriendlyFireBuilder extends Builder {

    public FriendlyFireBuilder (Document document) {
        super(document);
    }

    @Override
    public List<Module> build () throws ModuleLoadException, InvalidRegionException {
        List<Module> modules = new ArrayList<>();
        Element root = getRoot();
        boolean enabled = true;
        for(Element e : XMLUtil.getElements(root)) {
            if(e.getName().equals("friendlyfire")) {
                enabled = StringUtil.convertStringToBoolean(e.getText(), true);
            }
        }
        modules.add(new FriendlyFireModule(enabled));

        return modules;
    }
}
