package cdk.commands.sub;

import cdk.DataTools;
import cdk.commands.base.BaseSubCommand;
import cdk.commands.events.PlayerGetCdkEvent;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class GiveSubCommand extends BaseSubCommand {
    public GiveSubCommand(String name) {
        super(name);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("cdk.admin.permission");
    }

    public String[] getAliases() {
        return new String[0];
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length <= 2) {
            return false;
        } else {
            String playerName = args[1];
            Player player = Server.getInstance().getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("§c[§7CDK§c] §4玩家 " + playerName + "不在线哦~");
                return true;
            } else {
                String item = args[2];
                int count = 1;
                if (args.length > 3) {
                    try {
                        count = Integer.parseInt(args[3]);
                    } catch (Exception var12) {
                    }
                }

                if (!DataTools.isEsistsItem(item)) {
                    sender.sendMessage("§c[§7CDK§c] §4不存在奖励" + item);
                    return true;
                } else {
                    Map<String, Object> cdks = DataTools.getCdksCountByItem(item, count);
                    if (cdks.size() != count) {
                        sender.sendMessage("§c[§7CDK§c] §4库存不足!! 当前可用CDK:" + cdks.size());
                        return true;
                    } else {
                        sender.sendMessage("§c[§7CDK§c] §2成功给予玩家§5" + player.getName() + " §7" + cdks.size() + "§2条 cdk");
                        player.sendMessage("§c[§7CDK§c] §2你获得如下CDK §c(请及时使用)\n");
                        player.sendMessage("§e---------------------------\n\n");
                        int i = 1;

                        for(Iterator var10 = cdks.keySet().iterator(); var10.hasNext(); ++i) {
                            String cdk = (String)var10.next();
                            player.sendMessage("  §2" + i + ": §r" + cdk);
                        }

                        player.sendMessage("\n§e---------------------------");
                        PlayerGetCdkEvent event = new PlayerGetCdkEvent(player, new LinkedList(cdks.keySet()));
                        Server.getInstance().getPluginManager().callEvent(event);
                        return true;
                    }
                }
            }
        }
    }

    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
