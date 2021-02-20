package cn.cookiestudio.lobbysystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;

public class LobbyItem {

    private Item item;
    private String command;

    public LobbyItem(Item item,String command){
        this.item = item;
        this.command = command;
    }

    public Item getItem() {
        return item;
    }

    public String getCommand() {
        return command;
    }

    public boolean isItemMatched(Item item){
        return item.getId() == this.item.getId();
    }

    public void invokeCommand(Player player){
        Server.getInstance().getCommandMap().dispatch(player,command);
    }
}
