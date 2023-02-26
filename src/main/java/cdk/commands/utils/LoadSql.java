package cdk.commands.utils;

import cdk.Cdk;
import com.smallaswater.easysql.api.SqlEnable;
import com.smallaswater.easysql.exceptions.MySqlLoginException;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;
import com.smallaswater.easysql.mysql.utils.UserData;

public class LoadSql {
    private SqlEnable enable = null;

    public boolean getLoadSql() {
        return Cdk.getCdk().getConfig().getBoolean("database.open", false) ? this.loadSql() : false;
    }

    public SqlEnable getEnable() {
        return this.enable;
    }

    private boolean loadSql() {
        String user = Cdk.getCdk().getConfig().getString("database.MySQL.username");
        int port = Cdk.getCdk().getConfig().getInt("database.MySQL.port");
        String url = Cdk.getCdk().getConfig().getString("database.MySQL.host");
        String passWorld = Cdk.getCdk().getConfig().getString("database.MySQL.password");
        String table = Cdk.getCdk().getConfig().getString("database.MySQL.database");
        UserData data = new UserData(user, passWorld, url, port, table);

        try {
            this.enable = new SqlEnable(Cdk.getCdk(), "CDK", data, new TableType[]{new TableType("user", Types.CHAR), new TableType("count", Types.DOUBLE)});
        } catch (MySqlLoginException var8) {
            Cdk.getCdk().getLogger().info(var8.getMessage());
            return false;
        }

        Cdk.getCdk().getLogger().info("成功启动数据库....");
        return true;
    }
}
