package cn.cookiestudio.lobbysystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import java.util.ArrayList;
import java.util.HashMap;

public class Lobby {
    
    private LobbyConfig lobbyConfig;
    
    public Lobby(){
        this.lobbyConfig = new LobbyConfig();
        Server.getInstance().getPluginManager().registerEvents(new Lobby.Listener(),PluginMain.getInstance());
    }

    public LobbyConfig getLobbyConfig() {
        return lobbyConfig;
    }
    
    public ArrayList<Item> getLobbyItems(){
        ArrayList<Item> items = new ArrayList<>();
        for (LobbyItem lobbyItem : this.lobbyConfig.getLobbyItems())
            items.add(lobbyItem.getItem());
        return items;
    }
    
    public boolean isPositionInLobby(Position position){
        return this.getLobbyConfig().getLobbyPosition().getLevel() == position.getLevel();
    }
    
    public void teleportPlayerToLobby(Player player){
        player.getInventory().clearAll();
        player.teleport(this.lobbyConfig.getLobbyPosition());
        player.getInventory().addItem(this.getLobbyItems().toArray(new Item[0]));
    }
    
    private class Listener implements cn.nukkit.event.Listener {
        @EventHandler
        public void onBlockBreak(BlockBreakEvent event){
            if (Lobby.this.isPositionInLobby(event.getPlayer()) && event.getPlayer().getGamemode() != 1)
                event.setCancelled();
        }

        @EventHandler
        public void onBlockBreak(BlockPlaceEvent event){
            if (Lobby.this.isPositionInLobby(event.getPlayer()) && event.getPlayer().getGamemode() != 1)
                event.setCancelled();
        }

        @EventHandler
        public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
            if (event.getDamager() instanceof Player
                    && event.getEntity() instanceof Player
                    && Lobby.this.isPositionInLobby(event.getEntity()))
                event.setCancelled();
        }

        @EventHandler
        public void onBlockBurn(BlockBurnEvent event){
            if (Lobby.this.isPositionInLobby(event.getBlock().getLocation()))
                event.setCancelled();
        }

        @EventHandler
        public void onPlayerDropItem(PlayerDropItemEvent event){
            if (Lobby.this.isPositionInLobby(event.getPlayer()))
                event.setCancelled();
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event){
            if (Lobby.this.getLobbyConfig().isItemMatched(event.getItem()) && Lobby.this.isPositionInLobby(event.getPlayer())){
                Lobby.this.getLobbyConfig().getMatchedLobbyItem(event.getItem()).invokeCommand(event.getPlayer());
            }
        }

        @EventHandler
        public void onPlayerJoinPacket(DataPacketReceiveEvent event){
            if (event.getPacket().pid() == 113){
                Server.getInstance().getCommandMap().dispatch(event.getPlayer(),"hub");
            }
        }
    }

    public class LobbyConfig {

        private Config config;
        private Position lobbyPosition;
        private ArrayList<LobbyItem> lobbyItems = new ArrayList<>();

        public LobbyConfig(){
            PluginMain.getInstance().saveResource("config.yml");
            this.config = new Config(PluginMain.getInstance().getDataFolder() + "/config.yml");
            ArrayList tmp = (ArrayList) config.get("lobby-position");
            this.lobbyPosition = new Position((int)tmp.get(0),(int)tmp.get(1),(int)tmp.get(2), Server.getInstance().getLevelByName((String) tmp.get(3)));
            for (HashMap itemMap : (ArrayList<HashMap>)this.config.get("item"))
                this.lobbyItems.add(new LobbyItem(Item.get((Integer) itemMap.get("id")).setCustomName((String) itemMap.get("name")), (String) itemMap.get("command")));
        }

        public Config getConfig() {
            return config;
        }

        public Position getLobbyPosition() {
            return lobbyPosition;
        }

        public ArrayList<LobbyItem> getLobbyItems() {
            return lobbyItems;
        }

        public boolean isItemMatched(Item item){
            boolean isMatched = false;
            for (LobbyItem lobbyItem : this.lobbyItems){
                if (lobbyItem.isItemMatched(item))
                    isMatched = true;
            }
            return isMatched;
        }

        public LobbyItem getMatchedLobbyItem(Item item){
            LobbyItem rLobbyItem = null;
            for (LobbyItem lobbyItem : this.lobbyItems){
                if (lobbyItem.isItemMatched(item))
                    rLobbyItem = lobbyItem;
            }
            return rLobbyItem;
        }
    }

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
}
