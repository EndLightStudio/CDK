package cdk.commands.sub;

import cdk.Cdk;
import cdk.DataTools;
import cdk.commands.base.BaseSubCommand;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import java.util.ArrayList;

public class CreateSubCommand extends BaseSubCommand {
    public CreateSubCommand(String name) {
        super(name);
    }

    public String[] getAliases() {
        return new String[0];
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length < 2) {
                return false;
            }

            if (!DataTools.isEsistsItem(args[1])) {
                Config cn = Cdk.getCdk().getItemConfig();
                cn.set(args[1], new ArrayList<String>() {
                    {
                        this.add("give @p 264 1");
                    }
                });
                cn.save();
                sender.sendMessage("§c[§7CDK§c] §2奖励创建成功");
            } else {
                sender.sendMessage("§c[§7CDK§c] §4 此奖励代号已存在");
            }
        }

        return true;
    }

    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
