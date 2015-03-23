package me.HeyAwesomePeople.kitpvp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;

public class Logs {

	private KitPVP plugin = KitPVP.instance;

	private File logsDir = new File(plugin.getDataFolder(), "Logs");

	public Logs() {
		makeDirs();
		makeMiniLog();
		makeStackLog();

		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				removeOldLogs();
			}
		}, 0L, 20 * 60 * 60L);

	}

	public void makeDirs() {
		if (!logsDir.exists()) {
			logsDir.mkdir();
		}
	}

	public void makeMiniLog() {
		String date = getDateAsString();
		if (!new File(logsDir, "minilog-" + date + ".txt").exists()) {
			File todayFile = new File(logsDir, "minilog-" + date + ".txt");
			try {
				todayFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void makeStackLog() {
		String date = getDateAsString();
		if (!new File(logsDir, "stacklog-" + date + ".txt").exists()) {
			File todayFile = new File(logsDir, "stacklog-" + date + ".txt");
			try {
				todayFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void logToMiniLog(String logMsg, String type) {
		makeMiniLog();
		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logsDir + File.separator + "minilog-" + getDateAsString() + ".txt"), true));

			writer.append("[" + getTimeAsString() + "]" + "[" + type + "] " + logMsg);
			writer.newLine();
			writer.flush();
			writer.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void logToStackLog(Object object, String type) {
		makeStackLog();
		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(logsDir + File.separator + "stacklog-" + getDateAsString() + ".txt"), true));

			writer.append("[" + getTimeAsString() + "]" + "[" + type + "] " + object);
			writer.newLine();
			writer.flush();
			writer.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void removeOldLogs() {
		for (File f : logsDir.listFiles()) {
			Date fileDate = new Date(f.lastModified());
			Date now = new Date();
			long diff = now.getTime() - fileDate.getTime();
			if (diff / (24 * 60 * 60 * 1000) > 10) {
				f.delete();
			}
		}
	}

	public String getTimeAsString() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getDateAsString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
