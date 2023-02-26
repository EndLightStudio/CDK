package cdk.commands.base;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseCommand extends Command {
    private ArrayList<BaseSubCommand> subCommand = new ArrayList();
    private final ConcurrentHashMap<String, Integer> subCommands = new ConcurrentHashMap();

    public BaseCommand(String name, String description) {
        super(name, description);
    }

    public abstract boolean hasPermission(CommandSender var1);

    public boolean execute(CommandSender sender, String s, String[] args) {
        if (this.hasPermission(sender)) {
            if (args.length > 0) {
                String subCommand = args[0].toLowerCase();
                if (!this.subCommands.containsKey(subCommand)) {
                    return false;
                }

                BaseSubCommand command = (BaseSubCommand)this.subCommand.get((Integer)this.subCommands.get(subCommand));
                boolean canUse = command.hasPermission(sender);
                if (canUse) {
                    return command.execute(sender, s, args);
                }

                if (sender instanceof Player) {
                    sender.sendMessage("§c你没有使用此指令权限");
                    return true;
                }

                sender.sendMessage("请不要在控制台执行此指令");
            } else {
                this.sendDefaultHelp(sender);
            }
        }

        return true;
    }

    public abstract void sendDefaultHelp(CommandSender var1);

    public abstract void sendHelp(CommandSender var1);

    protected void addSubCommand(BaseSubCommand cmd) {
        this.subCommand.add(cmd);
        int commandId = this.subCommand.size() - 1;
        this.subCommands.put(cmd.getName().toLowerCase(), commandId);
        String[] var3 = cmd.getAliases();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String alias = var3[var5];
            this.subCommands.put(alias.toLowerCase(), commandId);
        }

    }

    protected void loadCommandBase() {
        this.commandParameters.clear();
        Iterator var1 = this.subCommand.iterator();

        while(var1.hasNext()) {
            BaseSubCommand subCommand = (BaseSubCommand)var1.next();
            LinkedList<CommandParameter> parameters = new LinkedList();
            parameters.add(new CommandParameter(subCommand.getName(), new String[]{subCommand.getName()}));
            parameters.addAll(Arrays.asList(subCommand.getParameters()));
            this.commandParameters.put(subCommand.getName(), parameters.toArray(new CommandParameter[0]));
        }

    }
}
