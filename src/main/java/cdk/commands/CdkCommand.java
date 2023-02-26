package cdk.commands;

import cdk.Cdk;
import cdk.DataTools;
import cdk.commands.base.BaseCommand;
import cdk.commands.sub.CreateSubCommand;
import cdk.commands.sub.DelSubCommand;
import cdk.commands.sub.GetSubCommand;
import cdk.commands.sub.GiveSubCommand;
import cdk.commands.sub.SetSubCommand;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.easysql.api.SqlEnable;
import java.util.Map;

public class CdkCommand extends BaseCommand {
    public CdkCommand(String name, String description) {
        super(name, description);
        this.setUsage("/cdk help");
        this.addSubCommand(new CreateSubCommand("create"));
        this.addSubCommand(new DelSubCommand("del"));
        this.addSubCommand(new GetSubCommand("get"));
        this.addSubCommand(new GiveSubCommand("give"));
        this.addSubCommand(new SetSubCommand("set"));
        this.loadCommandBase();
    }

    public boolean execute(CommandSender sender, String s, String[] args) {
        if (this.hasPermission(sender)) {
            boolean b = super.execute(sender, s, args);
            if (!b && args.length == 1) {
                if (!"help".equalsIgnoreCase(args[0]) && !"?".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Object object = Cdk.getCdk().getCDKConfig();
                        if (object instanceof Config) {
                            if (((Config)object).get(args[0]) != null) {
                                Object o = ((Config)object).get(args[0]);
                                String items;
                                if (o instanceof Map) {
                                    items = ((Map)o).get("item").toString();
                                } else {
                                    items = o.toString();
                                }

                                Config itemConfig = Cdk.getCdk().getItemConfig();
                                Config cdkConfig = (Config)object;
                                cdkConfig.remove(args[0]);
                                cdkConfig.save();
                                if (itemConfig.get(items) != null) {
                                    DataTools.addItems((Player)sender, items);
                                    sender.sendMessage(TextFormat.GREEN + "§c[§7CDK§c] §2成功兑换 CDK: " + TextFormat.GOLD + args[0]);
                                } else {
                                    sender.sendMessage(TextFormat.RED + "§c[§7CDK§c] §c 这条 CDK 并没有奖励哦");
                                }
                            } else {
                                sender.sendMessage(TextFormat.RED + "§c[§7CDK§c] §c 无效 CDK");
                            }
                        } else {
                            String item = DataTools.getCdkItemBySql((SqlEnable)object, args[0]);
                            DataTools.removeCdkItemBySql((SqlEnable)object, args[0]);
                            if (item != null) {
                                DataTools.addItems((Player)sender, item);
                                sender.sendMessage(TextFormat.GREEN + "§c[§7CDK§c] §2 成功兑换 CDK: " + TextFormat.GOLD + args[0]);
                            } else {
                                sender.sendMessage(TextFormat.RED + "§c[§7CDK§c] §c 无效 CDK");
                            }
                        }

                        return true;
                    } else {
                        sender.sendMessage("§c[§7CDK§c] §c请不要用控制台兑换CDK..");
                        return true;
                    }
                } else {
                    this.sendHelp(sender);
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void sendDefaultHelp(CommandSender sender) {
        sender.sendMessage("§c未知指令 请输入/cdk help查看");
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("cdk.player.permission");
    }

    public void sendHelp(CommandSender sender) {
        if (sender.isOp()) {
            sender.sendMessage("§c[§7CDK§c] §2/cdk help §7插件帮助信息");
            sender.sendMessage("§c[§7CDK§c] §2/cdk get §7获取所有CDK");
            sender.sendMessage("§c[§7CDK§c] §2/cdk give <玩家> <奖励代号> <数量(可不填)> §7给予玩家1条或多条 cdk");
            sender.sendMessage("§c[§7CDK§c] §2/cdk set <奖励代号> <数量(可不填)>§7添加一个CDK");
            sender.sendMessage("§c[§7CDK§c] §2/cdk create <奖励代号> §7创建一个奖励");
            sender.sendMessage("§c[§7CDK§c] §2/cdk del <奖励代号> §7删除cdk奖励");
        } else {
            sender.sendMessage("§c[§7CDK§c] §2/cdk <兑换码> §7使用兑换码兑换");
        }

    }
}
