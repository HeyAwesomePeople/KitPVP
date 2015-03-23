package me.HeyAwesomePeople.kitpvp.methods;

import me.HeyAwesomePeople.kitpvp.KitPVP;
import me.HeyAwesomePeople.kitpvp.playerdata.PlayerData;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {
	private KitPVP plugin = KitPVP.instance;

	public void setScoreboard(Player p) {
		Scoreboard b = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = b.registerNewObjective("KitPVP", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(plugin.kitpvp);
		PlayerData data = plugin.manager.pData.get(p);

		Score kills = obj.getScore(ChatColor.BLUE + "Kills: " + ChatColor.RED + data.getKills());
		Score deaths = obj.getScore(ChatColor.BLUE + "Deaths: " + ChatColor.RED + data.getDeaths());
		Score credits = obj.getScore(ChatColor.BLUE + "Credits: " + ChatColor.RED + plugin.econ.getBalance(p));
		Score space = obj.getScore("");
		if (data.getCurrentClassName() != null) {
			Score clazz = obj.getScore(ChatColor.BLUE + "Current Class: " + ChatColor.RED + data.getCurrentClassName());
			Score prestige = obj.getScore(ChatColor.BLUE + "Prestige: " + ChatColor.RED + data.getCCPrestige());
			Score rank = obj.getScore(ChatColor.BLUE + "Rank: " + ChatColor.RED + data.getCCRank());
			Score exp = obj.getScore(ChatColor.BLUE + "Exp: " + ChatColor.RED + data.getCCXp());
			clazz.setScore(4);
			exp.setScore(1);
			rank.setScore(2);
			prestige.setScore(3);
		} else {
			Score clazz = obj.getScore(ChatColor.BLUE + "Current Class: " + ChatColor.RED + "none");
			clazz.setScore(1);
			space.setScore(2);
			credits.setScore(3);
			deaths.setScore(4);
			kills.setScore(5);

			p.setScoreboard(b);
		}

		space.setScore(5);
		credits.setScore(6);
		deaths.setScore(7);
		kills.setScore(8);

		p.setScoreboard(b);
	}
}
