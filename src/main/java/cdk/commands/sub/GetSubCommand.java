package cdk.commands.sub;

import cdk.Cdk;
import cdk.DataTools;
import cdk.commands.base.BaseSubCommand;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import java.util.Iterator;
import java.util.Map;

public class GetSubCommand extends BaseSubCommand {
    public GetSubCommand(String name) {
        super(name);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("cdk.admin.permission");
    }

    public String[] getAliases() {
        return new String[0];
    }

    public boolean execute(final CommandSender sender, String label, String[] args) {
        sender.sendMessage("正在获取CDK 数据");
        Server.getInstance().getScheduler().scheduleAsyncTask(Cdk.getCdk(), new AsyncTask() {
            public void onRun() {
                Map<String, Object> map = DataTools.getAllCdks();
                Iterator var2 = map.keySet().iterator();

                while(var2.hasNext()) {
                    String key = (String)var2.next();
                    String val = (String)map.get(key);
                    sender.sendMessage("§c[§7CDK§c] §2CDK: §r" + key + TextFormat.GREEN + "  兑换:" + val + " 状态: " + (DataTools.isKeyCdk(key) ? "§c不可用" : "§a可用"));
                }

                if (map.size() > 0) {
                    sender.sendMessage("§c[§7CDK§c] §2查询到 " + map.size() + "条CDK信息");
                } else {
                    sender.sendMessage("§c[§7CDK§c] §4当前无可用CDK ");
                }

            }
        });
        return true;
    }

    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
