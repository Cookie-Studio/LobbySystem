package cn.cookiestudio.lobbysystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.weather.EntityLightning;

public class HubCommand extends Command {
    public HubCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            commandSender.sendMessage("can't use this command in console!");
            return true;
        }else{
            Player player = (Player)commandSender;
            PluginMain.getInstance().getLobby().teleportPlayerToLobby(player);
            player.sendTitle((String)PluginMain.getInstance().getLobby().getLobbyConfig().getConfig().get("hub-title"),(String)PluginMain.getInstance().getLobby().getLobbyConfig().getConfig().get("hub-subtitle"));
            player.sendActionBar((String)PluginMain.getInstance().getLobby().getLobbyConfig().getConfig().get("hub-actionbar"));
            player.sendMessage((String)PluginMain.getInstance().getLobby().getLobbyConfig().getConfig().get("hub-message"));
            if ((boolean)PluginMain.getInstance().getLobby().getLobbyConfig().getConfig().get("lighting-when-hub")){
                EntityLightning lightning = new EntityLightning(player.getChunk(),EntityLightning.getDefaultNBT(player));
                lightning.setEffect(false);
                lightning.spawnToAll();
            }
        }
        return true;
    }
}
