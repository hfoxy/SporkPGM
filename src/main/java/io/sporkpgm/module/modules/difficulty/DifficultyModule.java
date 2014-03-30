package io.sporkpgm.module.modules.difficulty;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.extras.InitModule;
import org.bukkit.Difficulty;

@ModuleInfo(name = "TimeLockModule", description = "The module that sets the world difficulty", listener = false)
public class DifficultyModule extends Module implements InitModule {

    int difficulty;

    @Override
    public Class<? extends Builder> builder() { return DifficultyBuilder.class; }

    @Override
    public void start () {
        SporkMap.getMap().getWorld().setDifficulty(Difficulty.getByValue(difficulty));
    }

    @Override
    public void stop () {}

    public DifficultyModule(int difficulty) { this.difficulty = difficulty; }

}
