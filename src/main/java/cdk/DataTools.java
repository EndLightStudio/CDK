package cdk;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Config;
import com.smallaswater.easysql.api.SqlEnable;
import com.smallaswater.easysql.mysql.data.SqlData;
import com.smallaswater.easysql.mysql.data.SqlDataList;
import com.smallaswater.easysql.mysql.data.SqlDataManager;
import com.smallaswater.easysql.mysql.manager.SqlManager;
import com.smallaswater.easysql.mysql.utils.ChunkSqlType;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;

import java.util.*;

public class DataTools {

    public static String getCdkItemBySql(SqlEnable enable, String cdk) {
        SqlDataManager manager = enable.getManager().getSqlManager();
        SqlDataList<SqlData> dataList = manager.selectExecute("item", (String)null, "cdkNumber = ?", new ChunkSqlType[]{new ChunkSqlType(1, cdk)});
        return dataList.get().getString("item");
    }

    private static boolean getCdkIsKeyBySql(SqlEnable enable, String cdk) {
        SqlDataManager manager = enable.getManager().getSqlManager();
        SqlDataList dataList = manager.selectExecute("isSell", (String)null, "cdkNumber = ?", new ChunkSqlType[]{new ChunkSqlType(1, cdk)});
        return dataList.get().getBoolean("isSell");
    }

    private static LinkedList<Object> getCdkItemByItem(SqlEnable enable, String cdkItem) {
        SqlDataManager manager = enable.getManager().getSqlManager();
        LinkedList<Object> dataList = manager.selectExecute("cdkNumber", "", "", new ChunkSqlType[0]).getValueList("cdkNumber");
        return dataList != null ? dataList : new LinkedList();
    }

    public static void addCdkItemBySql(SqlEnable enable, String cdk, String item) {
        try {
            if (getCdkItemBySql(enable, cdk) != null) {
                cdk = getRandomString(Cdk.getCdk().getLine());
                addCdkItemBySql(enable, cdk, item);
                return;
            }

            enable.getManager().getSqlManager().insertData((new SqlData("cdkNumber", cdk)).put("item", item).put("isSell", false));
        } catch (Exception var6) {
            if (Cdk.getCdk().getCdkEnable() == null) {
                return;
            }

            try {
                SqlEnable cdkEnable = Cdk.getCdk().getCdkEnable();
                cdkEnable.getManager().deleteField("CDK");
                cdkEnable = new SqlEnable(Cdk.getCdk(), "CDK", Cdk.getCdk().getData(), new TableType[]{new TableType("cdkNumber", Types.VARCHAR), new TableType("item", Types.VARCHAR), new TableType("isSell", Types.CHAR)});
                Cdk.getCdk().setCdkEnable(cdkEnable);
                Cdk.getCdk().getLogger().error("数据库异常，,已初始化cdk表");
            } catch (Exception var5) {
            }
        }

    }

    public static void removeCdk(final String cdkItem) {
        final Config itemConfig = Cdk.getCdk().getItemConfig();
        if (itemConfig != null) {
            itemConfig.remove(cdkItem);
            itemConfig.save();
        }

        Server.getInstance().getScheduler().scheduleAsyncTask(Cdk.getCdk(), new AsyncTask() {
            public void onRun() {
                if (itemConfig != null) {
                    Map<String, Object> all = itemConfig.getAll();
                    Iterator var3 = all.keySet().iterator();

                    while(var3.hasNext()) {
                        String cdkNumber = (String)var3.next();
                        Object o = all.get(cdkNumber);
                        if (o instanceof Map) {
                            if (((Map)o).get("item").toString().equalsIgnoreCase(cdkItem)) {
                                all.remove(cdkNumber);
                            }
                        } else if (all.get(cdkNumber).toString().equalsIgnoreCase(cdkItem)) {
                            all.remove(cdkNumber);
                        }
                    }

                    itemConfig.setAll(new LinkedHashMap(all));
                    itemConfig.save();
                } else {
                    SqlEnable cdkEnable = Cdk.getCdk().getCdkEnable();
                    if (cdkEnable != null) {
                        Iterator var6 = DataTools.getCdkItemByItem(cdkEnable, cdkItem).iterator();

                        while(var6.hasNext()) {
                            Object c = var6.next();
                            DataTools.removeCdkItemBySql(cdkEnable, c.toString());
                        }
                    }
                }

            }
        });
    }

    public static Map<String, Object> getAllCdks() {
        Map<String, Object> map = new LinkedHashMap();
        Object config = Cdk.getCdk().getCDKConfig();
        Iterator var4;
        if (config instanceof Config) {
            Map<String, Object> stringObjectMap = ((Config)config).getAll();
            var4 = stringObjectMap.keySet().iterator();

            while(var4.hasNext()) {
                String cdk = (String)var4.next();
                Object map1 = stringObjectMap.get(cdk);
                if (map1 instanceof Map) {
                    map.put(cdk, ((Map)map1).get("item"));
                } else {
                    map.put(cdk, map1.toString());
                }
            }
        } else if (config instanceof SqlEnable) {
            SqlManager manager = ((SqlEnable)config).getManager();
            SqlDataList<SqlData> obj = manager.getSqlManager().selectExecute("*", "", "", new ChunkSqlType[0]);
            var4 = obj.getValueList("cdknumber").iterator();

            while(var4.hasNext()) {
                Object cdk = var4.next();
                String s = getCdkItemBySql((SqlEnable)config, cdk.toString());
                if (s != null) {
                    map.put(cdk.toString(), s);
                }
            }
        }

        return map;
    }

    public static void addItems(Player player, String cdk) {
        List<String> l = Cdk.getCdk().getItemConfig().getStringList(cdk);
        if (l != null && l.size() > 0) {
            Iterator var3 = l.iterator();

            while(var3.hasNext()) {
                String cmd = (String)var3.next();
                Cdk.getCdk().getServer().dispatchCommand(new ConsoleCommandSender(), cmd.replace("@p", player.getName()));
            }
        }

    }

    public static boolean isKeyCdk(String cdk) {
        Object cdkConfig = Cdk.getCdk().getCDKConfig();
        if (cdkConfig instanceof Config && ((Config)cdkConfig).get(cdk) != null) {
            Object map = ((Config)cdkConfig).get(cdk);
            if (map instanceof Map) {
                return Boolean.parseBoolean(((Map)map).get("isSell").toString());
            }
        }

        return cdkConfig instanceof SqlEnable ? getCdkIsKeyBySql((SqlEnable)cdkConfig, cdk) : false;
    }

    public static void removeCdkItemBySql(SqlEnable enable, String cdk) {
        if (enable.getManager().getSqlManager().isExists("cdkNumber", cdk)) {
            enable.getManager().getSqlManager().deleteData(new SqlData("cdkNumber", cdk));
        }

    }

    public static boolean isEsistsCdk(String cdk) {
        Object o = Cdk.getCdk().getCDKConfig();
        if (o instanceof Config) {
            return ((Config)o).get(cdk) != null;
        } else {
            return o instanceof SqlEnable ? ((SqlEnable)o).getManager().getSqlManager().isExists("cdkNumber", cdk) : false;
        }
    }

    public static Map<String, Object> getCdksCountByItem(String item, int count) {
        Map<String, Object> map = getAllCdks();
        Map<String, Object> cdks = new LinkedHashMap();
        int i = 0;
        Iterator var5 = map.keySet().iterator();

        while(var5.hasNext()) {
            String cdk = (String)var5.next();
            if (i < count && item.equalsIgnoreCase(map.get(cdk).toString()) && !isKeyCdk(cdk)) {
                keyCdkByItem(cdk);
                cdks.put(cdk, item);
                ++i;
            }
        }

        return cdks;
    }

    public static boolean isEsistsItem(String item) {
        return (new ArrayList(Cdk.getCdk().getItemConfig().getAll().keySet())).contains(item);
    }

    private static void keyCdkByItem(String cdk) {
        Object enable = Cdk.getCdk().getCDKConfig();
        if (enable instanceof Config) {
            Object map = ((Config)enable).get(cdk);
            LinkedHashMap<String, Object> map1 = new LinkedHashMap();
            if (map instanceof Map) {
                Iterator var4 = ((Map)map).keySet().iterator();

                while(var4.hasNext()) {
                    Object s = var4.next();
                    map1.put(s.toString(), ((Map)map).get(s));
                }
            } else {
                map1.put("item", map.toString());
            }

            map1.put("isSell", true);
            ((Config)enable).set(cdk, map1);
            ((Config)enable).save();
        } else if (enable instanceof SqlEnable && ((SqlEnable)enable).getManager().getSqlManager().isExists("cdkNumber", cdk)) {
            ((SqlEnable)enable).getManager().getSqlManager().setData(new SqlData("isSell", true), new SqlData("cdkNumber", cdk));
        }

    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            str.append(base.charAt(number));
        }

        return str.toString();
    }
}
