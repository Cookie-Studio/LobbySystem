package cn.cookiestudio.lobbysystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.utils.Config;

public class HubCommand extends Command {
    public HubCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.isPlayer()){
            commandSender.sendMessage("Must be a player!");
            return false;
        }else{
            Config config = LobbySystem.getInstance().getLobby().getLobbyConfig().getConfig();
            Player player = commandSender.asPlayer();
            LobbySystem.getInstance().getLobby().hub(player);
            player.sendTitle((String)config.get("hub-title"),(String)config.get("hub-subtitle"));
            player.sendActionBar((String)config.get("hub-actionbar"));
            player.sendMessage((String)config.get("hub-message"));
            if ((boolean)config.get("lighting-when-hub")){
                EntityLightning lightning = new EntityLightning(player.getChunk(),EntityLightning.getDefaultNBT(player));
                lightning.setEffect(false);
                lightning.spawnToAll();
            }
        }
        return true;
    }
}
