package io.sporkpgm.module.modules.timelock;

import io.sporkpgm.Spork;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.extras.TaskedModule;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@ModuleInfo(name = "TimeLockModule", description = "The module that locks the map time", listener = false)
public class TimeLockModule extends Module implements TaskedModule {

    boolean enabled;

    public TimeLockModule(boolean enabled) { this.enabled = enabled; }

    @Override
    public Class<? extends Builder> builder() { return TimeLockBuilder.class; }

    @Override
    public void setTasks (boolean tasks) {
        final long TIME = SporkMap.getMap().getWorld().getTime();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Spork.get(), new BukkitRunnable() {
            @Override
            public void run() {
                SporkMap.getMap().getWorld().setTime(TIME);
            }
        }, 0L, 1L);
    }
}
