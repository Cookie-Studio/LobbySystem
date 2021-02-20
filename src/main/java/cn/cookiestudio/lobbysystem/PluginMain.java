package cn.cookiestudio.lobbysystem;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

public class PluginMain extends PluginBase {

    private static PluginMain pluginMain;
    private Lobby lobby;

    @Override
    public void onLoad() {
        this.getLogger().info("plugin load!");
    }

    public static PluginMain getInstance() {
        return pluginMain;
    }

    public Lobby getLobby() {
        return this.lobby;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("plugin enable!");
        this.saveResource("config.yml");
        pluginMain = this;
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
