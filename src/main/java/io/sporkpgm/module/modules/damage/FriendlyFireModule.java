package io.sporkpgm.module.modules.damage;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.player.SporkPlayer;
import io.sporkpgm.team.SporkTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@ModuleInfo(name = "FriendlyFireModule", description = "The module that toggles friendly fire", listener = true)
public class FriendlyFireModule extends Module {

    boolean enabled;

    public FriendlyFireModule(boolean enabled) { this.enabled = enabled; }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamageEvent(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player && !enabled) {
            SporkTeam attackerTeam = SporkPlayer.getPlayer((Player) e.getDamager()).getTeam();
            SporkTeam victimTeam = SporkPlayer.getPlayer((Player) e.getEntity()).getTeam();
            if(victimTeam == attackerTeam) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public Class<? extends Builder> builder () {
        return FriendlyFireBuilder.class;
    }
}
