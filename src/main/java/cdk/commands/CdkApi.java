package cdk.commands;

import cdk.Cdk;
import cdk.DataTools;
import cn.nukkit.utils.Config;
import com.smallaswater.easysql.api.SqlEnable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class CdkApi {

    public static LinkedList<String> getCdkByItem(String itemId, int count) {
        return getCdkByItem(itemId, count, true, false);
    }

    public static LinkedList<String> getCdkByItem(String itemId, int count, boolean key, boolean isGetKey) {
        if (!DataTools.isEsistsItem(itemId)) {
            return new LinkedList();
        } else if (key) {
            return new LinkedList(DataTools.getCdksCountByItem(itemId, count).keySet());
        } else {
            LinkedList<String> cdks = new LinkedList();
            int i = 0;

            for(Iterator var6 = DataTools.getAllCdks().keySet().iterator(); var6.hasNext(); ++i) {
                String cdk = (String)var6.next();
                if (i < count && itemId.equalsIgnoreCase(DataTools.getAllCdks().get(itemId).toString())) {
                    if (!isGetKey) {
                        if (!DataTools.isKeyCdk(cdk)) {
                            cdks.add(cdk);
                        }
                    } else if (DataTools.isKeyCdk(cdk)) {
                        cdks.add(cdk);
                    }
                }
            }

            return cdks;
        }
    }

    public static boolean useCdk(String cdk) {
        if (DataTools.isEsistsCdk(cdk)) {
            Object o = Cdk.getCdk().getCDKConfig();
            if (o instanceof SqlEnable) {
                DataTools.removeCdkItemBySql((SqlEnable)o, cdk);
                return true;
            }

            if (o instanceof Config) {
                ((Config)o).remove(cdk);
                ((Config)o).save();
                return true;
            }
        }

        return false;
    }

    public static String getCommandByCdk(String cdk) {
        if (DataTools.isEsistsCdk(cdk)) {
            Object o = Cdk.getCdk().getCDKConfig();
            if (o instanceof SqlEnable) {
                return DataTools.getCdkItemBySql((SqlEnable)o, cdk);
            }

            if (o instanceof Config) {
                Object m = ((Config)o).get(cdk);
                if (o instanceof Map) {
                    return ((Map)m).get("item").toString();
                }

                return m.toString();
            }
        }

        return null;
    }

    public static LinkedList<String> getCommandsbyItemId(String itemId) {
        return new LinkedList(Cdk.getCdk().getItemConfig().getStringList(itemId));
    }
}
