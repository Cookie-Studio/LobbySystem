package cn.cookiestudio.lobbysystem;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

public class LobbySystem extends PluginBase {

    private static LobbySystem instance;
    private Lobby lobby;

    @Override
    public void onLoad() {
        this.getLogger().info("plugin load!");
    }

    public static LobbySystem getInstance() {
        return instance;
    }

    public Lobby getLobby() {
        return this.lobby;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("plugin enable!");
        this.saveResource("config.yml");
        instance = this;
        lobby = new Lobby();
        this.registerCommand();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("plugin disable!");
    }

    private void registerCommand(){
        Server.getInstance().getCommandMap().register("",new HubCommand("hub"));
    }
}
