package onionsss.it.onionsss.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：张琦 on 2016/5/17 13:09
 * 解析版本号
 */
public class VersionStream {
    public static String getJson(InputStream is){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024*8];
        int len = 0;
        try {
            while((len = is.read(b))!=-1){
                baos.write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
