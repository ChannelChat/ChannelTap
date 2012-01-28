package com.feildmaster.module.chantap;

import com.feildmaster.channelchat.channel.Channel;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class Switch {
    private boolean tapping = true;
    private final Set<String> channels = new HashSet<String>();
    private final Set<String> players = new HashSet<String>();

    /**
     * Check if player is currently listening to tap
     *
     * @return True if taps are "on"
     */
    public boolean isListening() {
        return tapping;
    }
    public void setListening(boolean bool) {
        tapping = bool;
    }

    public void add(Player player) {
        players.add(player.getName());
    }
    public void add(Channel channel) {
        channels.add(channel.getName());
    }

    public void remove(Player player) {
        players.remove(player.getName());
    }
    public void remove(Channel channel) {
        channels.remove(channel.getName());
    }

    public boolean isListening(Player player) {
        return players.contains(player.getName());
    }

    public boolean isListening(Channel channel) {
        return channels.contains(channel.getName());
    }
}
