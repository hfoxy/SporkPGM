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

    public DifficultyModule(int difficulty) { this.difficulty = difficulty; }

    @Override
    public void start () {
        SporkMap.getMap().getWorld().setDifficulty(Difficulty.getByValue(difficulty));
    }

    @Override
    public void stop () {}

    @Override
    public Class<? extends Builder> builder() { return DifficultyBuilder.class; }

}
