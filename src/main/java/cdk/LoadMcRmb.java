package cdk;

import cn.nukkit.utils.ConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;

public class LoadMcRmb {
    private final int sid;
    private final String key;
    private static final String URL = "http://api.mcrmb.com/Api/{api}?{value}";

    public LoadMcRmb(int sid, String key) {
        this.sid = sid;
        this.key = key;
    }

    private String md5Value(String wname, GetValue... value) {
        StringBuilder v = new StringBuilder();
        StringBuilder v1 = new StringBuilder();
        GetValue[] var5 = value;
        int var6 = value.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            GetValue va = var5[var7];
            v.append(va.value);
            v1.append("&").append(va.name).append("=").append(va.value);
        }

        return "sign=" + getMd5(this.sid + wname + v.toString() + this.key) + "&sid=" + this.sid + "&wname=" + wname + v1.toString();
    }

    public static String getMd5(String plainText) {
        String md5Str = null;

        try {
            StringBuilder buf = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            byte[] var6 = b;
            int var7 = b.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                byte value = var6[var8];
                int i = value;
                if (value < 0) {
                    i = value + 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            md5Str = buf.toString();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return md5Str;
    }

    private static ConfigSection send(String api, String value) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return new ConfigSection((LinkedHashMap)gson.fromJson(loadJson(api, value), (new TypeToken<LinkedHashMap<String, Object>>() {
        }).getType()));
    }

    private static String loadJson(String api, String value) {
        StringBuilder json = new StringBuilder();

        try {
            java.net.URL urlObject = new URL("http://api.mcrmb.com/Api/{api}?{value}".replace("{api}", api).replace("{value}", value));
            HttpURLConnection uc = (HttpURLConnection)urlObject.openConnection();
            uc.addRequestProperty(".USER_AGENT", "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 java");
            uc.setRequestMethod("GET");
            uc.setConnectTimeout(15000);
            uc.setReadTimeout(15000);
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }

            in.close();
            return json.toString();
        } catch (Exception var7) {
            return null;
        }
    }

    private static class GetValue {
        private final String name;
        private final String value;

        public GetValue(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
