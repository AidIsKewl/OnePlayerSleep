package aid.me.ops.sleep;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import aid.me.ops.OpsPlugin;
import aid.me.ops.PluginMain;

public class SleepManager {
	
	//Variables
	private CraftPlayer bukkitPlayer;
	private int playerSleepTicks = 0;
	private PluginMain pl = OpsPlugin.getPlugin();
	
	private BukkitTask bukkitTask, taskTimer; 
	
	private boolean taskCompleted = false;
	
	//Getters
	public int getSleepTicks() {
		return this.playerSleepTicks;
	}
	
	public CraftPlayer getBukkitPlayer() {
		return this.bukkitPlayer;
	}
	
	public BukkitTask getBukkitTask() {
		return this.bukkitTask;
	}
	
	public BukkitTask getBukkitTimerTask() {
		return this.taskTimer;
	}
	
	public boolean taskIsCompleted() {
		return taskCompleted;
	}
	
	//Setters
	public void setSleepTicks(int ticks) {
		bukkitPlayer.getHandle().sleepTicks = ticks;
		return;
	}
	
	public void setPlayerAs(CraftPlayer player) {
		this.bukkitPlayer = player;
		return;
	}

	//Methods
	public void startSleep(boolean changesWeather, long duration) {
		
		if(this.bukkitPlayer == null) {
			return;
		}
		
		World world = bukkitPlayer.getWorld();
		this.taskCompleted = false;
		
		//Schedules a new Bukkit Task to be run after the duration in ticks
		this.bukkitTask = new BukkitRunnable() {
			
			public void run() {
				setSleepTicks(99);
				world.setTime(0);
				
				if(changesWeather) {
					world.setStorm(false);
					world.setThundering(false);
					world.setWeatherDuration(0);
				}
				
				OpsPlugin.getMessageManager().broadcastMessage("messages.success.slept");
				taskCompleted = true;
			}
			
		}.runTaskLater(this.pl, duration);
		
		//Starts the timer that keeps a player in bed until the previous task is complete
		this.taskTimer = new BukkitRunnable() {
			
			public void run() {
				setSleepTicks(0);
				if(taskCompleted) {
					this.cancel();
				}
			}
			
		}.runTaskTimer(this.pl, 0, 1);
		
		return;
	}
	
	//Attempts to interrupt the sleep cycle and stop its exection
	public void stopSleep() {
		
		try {
			
			this.bukkitTask.cancel();
			this.taskCompleted = true;
			
		}catch(IllegalStateException ex) {
			ex.printStackTrace();
		}
		
		return;
	}
	
}