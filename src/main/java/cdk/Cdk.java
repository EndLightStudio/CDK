package cdk;

import cdk.commands.CdkCommand;
import cdk.commands.utils.LoadSql;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.easysql.api.SqlEnable;
import com.smallaswater.easysql.mysql.utils.UserData;

public class Cdk extends PluginBase {
    public static final String TABLE_NAME = "CDK";
    private int line;
    private static Cdk cdk;
    private UserData data;
    private Config cdkConfig;
    private Config itemConfig;
    private SqlEnable cdkEnable = null;


    public void onEnable() {
        cdk = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getServer().getCommandMap().register("cdkCommand", new CdkCommand("cdk", "cdk系统"));
        this.line = this.getConfig().getInt("生成CDK长度");
        this.getLogger().info(TextFormat.YELLOW + "插件加载成功,本插件作者若水");
        if (!this.getConfig().getBoolean("database.open", false)) {
            this.cdkConfig = new Config(this.getDataFolder() + "/cdk.yml", 2);
        } else {
            try {
                Class.forName("com.smallaswater.easysql.exceptions.MySqlLoginException");
                LoadSql loadSql = new LoadSql();
                if (loadSql.getLoadSql()) {
                    this.getLogger().info("正在启动数据库依赖...");
                    this.cdkEnable = loadSql.getEnable();
                }
            } catch (Exception var2) {
            }
        }

        if (this.cdkEnable == null && this.cdkConfig == null) {
            this.getLogger().info("数据库启动失败，，启用 Yaml存储");
            this.cdkConfig = new Config(this.getDataFolder() + "/cdk.yml", 2);
        }

        this.itemConfig = new Config(this.getDataFolder() + "/cdkItems.yml", 2);
    }

    void setCdkEnable(SqlEnable cdkEnable) {
        this.cdkEnable = cdkEnable;
    }

    SqlEnable getCdkEnable() {
        return this.cdkEnable;
    }

    UserData getData() {
        return this.data;
    }

    public int getLine() {
        return this.line;
    }

    public static Cdk getCdk() {
        return cdk;
    }

    public Object getCDKConfig() {
        return this.cdkConfig != null ? this.cdkConfig : this.cdkEnable;
    }

    public Config getItemConfig() {
        return this.itemConfig;
    }

    public Config getCdkConfig() {
        return this.cdkConfig;
    }

    public void onDisable() {
        if (this.cdkEnable != null) {
            this.cdkEnable.disable();
        }

    }
}