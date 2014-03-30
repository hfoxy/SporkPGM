package io.sporkpgm.module.modules.info;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.util.StringUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "InfoModule", description = "Contains information about the map", listener = false, multiple = false)
public class InfoModule extends Module {

	private String name;
	private String version;
	private String objective;

	private List<Contributor> authors;
	private List<Contributor> contributors;

	private boolean friendlyFire;

	public InfoModule(String name, String version, String objective) {
		this(name, version, objective, new ArrayList<Contributor>(), new ArrayList<Contributor>(), false);
	}

	public InfoModule(String name, String version, String objective, List<Contributor> authors,
					  List<Contributor> contributors, boolean friendlyFire) {
		this.name = name;
		this.version = version;
		this.objective = objective;
		this.authors = authors;
		this.contributors = contributors;
		this.friendlyFire = friendlyFire;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getObjective() {
		return objective;
	}

	public List<Contributor> getAuthors() {
		return authors;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public String getShortDescription() {
		String authorsString = StringUtil.listToEnglishCompound(authors, ChatColor.RED + "", ChatColor.DARK_PURPLE + "");
		return ChatColor.GOLD + name + ChatColor.DARK_PURPLE + " by " + authorsString;
	}

	public boolean friendlyFire() {
		return friendlyFire;
	}

	public Class<? extends Builder> builder() {
		return InfoBuilder.class;
	}

}
