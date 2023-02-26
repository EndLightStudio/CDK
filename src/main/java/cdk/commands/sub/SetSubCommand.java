package cdk.commands.sub;

import cdk.Cdk;
import cdk.DataTools;
import cdk.commands.base.BaseSubCommand;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.easysql.api.SqlEnable;
import java.util.LinkedHashMap;

public class SetSubCommand extends BaseSubCommand {
    public SetSubCommand(String name) {
        super(name);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("cdk.admin.permission");
    }

    public String[] getAliases() {
        return new String[0];
    }

    public boolean execute(final CommandSender sender, String label, final String[] args) {
        if (sender.isOp()) {
            if (args.length < 2) {
                return false;
            }

            sender.sendMessage("§c[§7CDK§c] §2正在生成 CDK");
            if (Cdk.getCdk().getItemConfig().get(args[1]) == null) {
                return true;
            }

            Server.getInstance().getScheduler().scheduleAsyncTask(Cdk.getCdk(), new AsyncTask() {
                public void onRun() {
                    int i = 1;
                    if (args.length > 2) {
                        i = Integer.parseInt(args[2]);
                    }

                    String cdk = "无..";
                    Object object = Cdk.getCdk().getCDKConfig();
                    if (object instanceof Config) {
                        Config config = (Config)object;

                        for(int ax = 0; ax < i; ++ax) {
                            cdk = DataTools.getRandomString(Cdk.getCdk().getLine());
                            if ("".equalsIgnoreCase(config.getString(cdk, ""))) {
                                config.set(cdk, new LinkedHashMap<String, Object>() {
                                    {
                                        this.put("item", args[1]);
                                        this.put("isSell", false);
                                    }
                                });
                            } else {
                                --ax;
                            }
                        }

                        config.save();
                    } else if (object instanceof SqlEnable) {
                        for(int a = 0; a < i; ++a) {
                            cdk = DataTools.getRandomString(Cdk.getCdk().getLine());
                            if (DataTools.getCdkItemBySql((SqlEnable)object, cdk) == null) {
                                DataTools.addCdkItemBySql((SqlEnable)object, cdk, args[1]);
                            } else {
                                --a;
                            }
                        }
                    }

                    if (i > 1) {
                        sender.sendMessage("§c[§7CDK§c] §2 成功生成" + i + "条CDK");
                    } else {
                        sender.sendMessage("§c[§7CDK§c] §2 成功生成CDK:" + TextFormat.DARK_BLUE + cdk);
                    }

                }
            });
        }

        return true;
    }

    public CommandParameter[] getParameters() {
        return new CommandParameter[0];
    }
}
