package cdk.commands.base;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public abstract class BaseSubCommand {
    private String name;

    protected BaseSubCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract String[] getAliases();

    public abstract boolean execute(CommandSender var1, String var2, String[] var3);

    public boolean hasPermission(CommandSender sender) {
        return sender.isOp();
    }

    public abstract CommandParameter[] getParameters();
}
