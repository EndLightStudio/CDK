package cdk.commands.events;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import java.util.LinkedList;

public class PlayerGetCdkEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private LinkedList<String> cdkNumbers;

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlayerGetCdkEvent(Player player, LinkedList<String> cdkNumbers) {
        this.cdkNumbers = cdkNumbers;
        this.player = player;
    }

    public LinkedList<String> getCdkNumbers() {
        return this.cdkNumbers;
    }
}
