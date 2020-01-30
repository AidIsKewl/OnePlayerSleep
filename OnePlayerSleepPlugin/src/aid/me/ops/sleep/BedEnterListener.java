package aid.me.ops.sleep;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import aid.me.ops.OpsPlugin;
import aid.me.ops.util.MessageManager;

public class BedEnterListener implements Listener{
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent e) {
		
		CraftPlayer player = (CraftPlayer) e.getPlayer();
		SleepManager mang = OpsPlugin.getSleepManager();
		FileConfiguration data = OpsPlugin.getDataConfig();
		MessageManager MSG = OpsPlugin.getMessageManager();
		
		//Find a way to store these variables elsewhere
		//Possibly a Configuration Handler class
		boolean isEnabled = data.getBoolean("enabled");
		boolean changesWeather = data.getBoolean("changes_weather");
		long duration = data.getLong("sleep_duration");
		
		//Ensure the player is set to the current player
		mang.setPlayerAs(player);
		OpsPlugin.getCommandManager().setCurrentSender(player);
		
		//Check to see if the plugin is enabled, and the BedResult is OK
		if(!isEnabled || !e.getBedEnterResult().equals(BedEnterResult.OK)) {
			return;
		}
		
		//Loop through existing players to see if anyone is already sleeping
		for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			if(onlinePlayer.isSleeping() && !(onlinePlayer.equals(player))) {
				e.setCancelled(true);
				MSG.sendMessage("messages.error.alreadyasleep", player);
				return;
			}
		}
		
		//Execute sleep cycle code
		mang.startSleep(changesWeather, duration);
		return;
	}
	
}