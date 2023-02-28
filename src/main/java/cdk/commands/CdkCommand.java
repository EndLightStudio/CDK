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
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
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
            if(args.length == 0){
                if(sender instanceof Player){
                    if(this.hasOpPermission(sender)){
                        this.AdminMenu((Player) sender);
                    } else {
                        this.DefaultMenu((Player) sender);
                    }
                    return true;
                }
                else {
                    this.sendHelp(sender);
                    //EG: 如果控制台执行，那么发送Help
                    return false;
                }
            }
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
    public boolean hasOpPermission(CommandSender sender) {
        return sender.hasPermission("cdk.admin.permission");
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
    public void DefaultMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple("CDK","");
        simple.addButton(new ElementButton("兑换",new ElementButtonImageData("path","textures/ui/invite_base")));
        player.showFormWindow(simple,DefaultMenu);
    }

    public void AdminMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple("CDK","");
        simple.addButton(new ElementButton("兑换",new ElementButtonImageData("path","textures/ui/invite_base")));
        /*
         simple.addButton(new ElementButton("创建",new ElementButtonImageData("path","textures/ui/invite_base")));
         simple.addButton(new ElementButton("设置",new ElementButtonImageData("path","textures/ui/invite_base")));
         simple.addButton(new ElementButton("删除",new ElementButtonImageData("path","textures/ui/invite_base")));
         simple.addButton(new ElementButton("给予",new ElementButtonImageData("path","textures/ui/invite_base")));
         TODO: 下个版本实现
        */
        player.showFormWindow(simple,AdminMenu);
    }

    public static void RedeemMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom("CDK--兑换");
        custom.addElement(new ElementInput("","请输入CDK"));
        player.showFormWindow(custom,RedeemMenu);
    }

    public static int DefaultMenu = 0x20230228;
    public static int AdminMenu = 0x20230229;
    public static int RedeemMenu = 0x20230230;

}
