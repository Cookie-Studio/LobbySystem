package cn.cookiestudio.lobbysystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;

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
        }
        return true;
    }
}
