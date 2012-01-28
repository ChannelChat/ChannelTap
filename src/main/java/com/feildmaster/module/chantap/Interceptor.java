package com.feildmaster.module.chantap;

import org.bukkit.event.*;
import com.feildmaster.channelchat.channel.Channel;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerQuitEvent;
import static com.feildmaster.channelchat.channel.ChannelManager.getManager;

public class Interceptor implements Listener {
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if(event.isCancelled()) return;

        Channel channel = getManager().getChannel(event);
        if(channel == null) return;

        String message = format(channel.getName(), String.format("%s: %s", event.getPlayer().getName(), ChatColor.stripColor(event.getMessage())));

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(event.getRecipients().contains(p)) continue;
            Switch s = Governor.map.get(p.getName());
            if(s == null) continue;
            if(s.isListening() && (s.isListening(channel) || s.isListening(event.getPlayer()))) {
                p.sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Governor.map.put(event.getPlayer().getName(), new Switch());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if(event.isCancelled()) return;
        removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    private void removePlayer(Player player) {
        Governor.map.remove(player.getName());
        for(Switch s : Governor.map.values()) {
            s.remove(player);
        }
    }

    public String format(String name, String message) {
        return String.format("[%s] %s", "Tap-"+name, message);
    }
}
