package me.HeyAwesomePeople.kitpvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.HeyAwesomePeople.kitpvp.managers.ClassManager;
import me.HeyAwesomePeople.kitpvp.managers.OUIManager;
import me.HeyAwesomePeople.kitpvp.maps.MMapManager;
import me.HeyAwesomePeople.kitpvp.playerdata.PlayerData;

import org.bukkit.entity.Player;


public class VariableManager {

	public Boolean gameStarted = false;

	/************* Player Data ****************/
	public HashMap<Player, PlayerData> pData = new HashMap<Player, PlayerData>();
	public List<PlayerData> playerDataClasses = new ArrayList<PlayerData>();
	public List<UUID> playersInGame = new ArrayList<UUID>();
	public List<UUID> playersInSpectate = new ArrayList<UUID>();

	/************ PlayerCommands *******************/
	public boolean playerCmdsEnabled = true;

	/************ MapManager *******************/
	public HashMap<Integer, MMapManager> mapList = new HashMap<Integer, MMapManager>();
	public MMapManager cMap = null;

	/************* Classes ************/
	public List<ClassManager> classList = new ArrayList<ClassManager>();
	public HashMap<String, ClassManager> classes = new HashMap<String, ClassManager>();
	
	/************** OUIs **************/
	public List<OUIManager> ouilist = new ArrayList<OUIManager>();
	
	
	public VariableManager() {
	}

	public void cleanup() {
		this.pData.clear();
		this.playerDataClasses.clear();
		this.playersInGame.clear();
		this.playersInSpectate.clear();
		this.mapList.clear();
		this.classList.clear();
		this.classes.clear();
	}

}
