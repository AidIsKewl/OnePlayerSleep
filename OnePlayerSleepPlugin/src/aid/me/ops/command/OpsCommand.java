package aid.me.ops.command;

import org.bukkit.command.CommandSender;

import aid.me.ops.OpsPlugin;
import aid.me.ops.util.MessageManager;

public abstract class OpsCommand {
	
	protected MessageManager msgMang = OpsPlugin.getMessageManager();
	
	public abstract void onCommand(CommandSender sender, String[] args);
	
	public abstract String getName();
	
	public abstract String getPermission();
	
	public abstract int maxAllowedArgs();
}