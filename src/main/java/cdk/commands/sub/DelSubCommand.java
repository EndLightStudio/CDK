package cdk.commands.sub;

import cdk.DataTools;
import cdk.commands.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public class DelSubCommand extends BaseSubCommand {
    public DelSubCommand(String name) {
        super(name);
    }

    public String[] getAliases() {
        return new String[0];
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length > 1) {
            String cdkId = args[1];
            if (!DataTools.isEsistsItem(cdkId)) {
                sender.sendMessage("§c[§7CDK§c] §4正在删除 " + cdkId + "相关CDK信息");
                DataTools.removeCdk(args[1]);
                sender.sendMessage("§c[§7CDK§c] §2 奖励" + cdkId + "删除成功");
            } else {
                sender.sendMessage("§c[§7CDK§c] §4 不存在奖励代号 " + cdkId);
            }
        } else {
            sender.sendMessage("§c[§7CDK§c] §4 请输入奖励代号");
        }

        return true;
    }

    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
