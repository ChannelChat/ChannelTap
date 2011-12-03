package feildmaster.modules.chanchat.chantap;

import com.feildmaster.channelchat.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import static com.feildmaster.channelchat.channel.ChannelManager.getManager;

public class Governor extends JavaPlugin {
    protected static final Map<String, Switch> map = new HashMap<String, Switch>();

    public void onEnable() {
        Interceptor intercept = new Interceptor();
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, intercept, Event.Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, intercept, Event.Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, intercept, Event.Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, intercept, Event.Priority.Monitor, this);

        for(Player player : getServer().getOnlinePlayers())
            if(!map.containsKey(player.getName()))
                map.put(player.getName(), new Switch());

        getServer().getLogger().info(format(String.format("v%1$s Enabled!", getDescription().getVersion())));
    }

    public void onDisable() {
        getServer().getLogger().info(format("Disabled!"));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(format("Can only be sent by player"));
            return true;
        }

        if(!sender.hasPermission("Chan.Tap")) {
            sender.sendMessage(format("You do not have permission to tap."));
            return true;
        }

        Switch s = map.get(sender.getName());
        if(s == null) {
            sender.sendMessage(format("Error occured in Tap. (null switch)"));
            return true;
        }

        if(args.length != 1) {
            sender.sendMessage(format("Please include a [single] #channel or player."));
            return true;
        }

        if(args[0].startsWith("#")) {
            Channel channel = getManager().getChannel(args[0].substring(1));
            if(channel == null) {
                sender.sendMessage(format("Channel not found"));
                return true;
            }

            if(s.isListening(channel)) {
                s.remove(channel);
                sender.sendMessage(format("No longer listenening to: "+channel.getName()));
            } else {
                s.add(channel);
                sender.sendMessage(format("Now listenening to: "+channel.getName()));
            }
        } else {
            Player player = getServer().getPlayerExact(args[0]);
            if(player == null) {
                sender.sendMessage(format("Player not found"));
                return true;
            }

            if(s.isListening(player)) {
                s.remove(player);
                sender.sendMessage(format("No longer listenening to: "+player.getName()));
            } else {
                s.add(player);
                sender.sendMessage(format("Now listenening to: "+player.getName()));
            }
        }

        return true;
    }

    public String format(String message) {
        return String.format("[%1$s] %2$s", getDescription().getName(), message);
    }
}
