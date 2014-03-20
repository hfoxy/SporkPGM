package io.sporkpgm.module.modules.timer;

import io.sporkpgm.match.Match;
import io.sporkpgm.match.MatchPhase;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;

@ModuleInfo(name = "TimerModule", description = "Contains information about the time a match will last", listener = false, multiple = false)
public class TimerModule extends Module {

	Match match;
	int time = -1;

	public TimerModule(int time) {
		this.time = time;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public boolean isInfinite() {
		return time == -1;
	}

	public int getDuration() {
		return time;
	}

	public boolean isComplete() {
		MatchPhase phase = match.getPhase();
		if(phase != MatchPhase.PLAYING) {
			return true;
		}

		if(isInfinite()) {
			return false;
		} else {
			return getDuration() >= match.getDuration();
		}
	}

	public Class<? extends Builder> builder() {
		return TimerBuilder.class;
	}

}
