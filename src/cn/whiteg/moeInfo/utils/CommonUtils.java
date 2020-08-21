package cn.whiteg.moeInfo.utils;

public class CommonUtils {
    //将时间的事件转换为24小时制字符串
    public static String getWorldTime(long time) {
        int h = ((int) (time / 1000));
        //UTC + 6 时区
        h = (h + 6) % 24;
        int m = (int) ((time % 1000) * 0.06);
        return new StringBuilder(asNumber(h,2)).append(":").append(asNumber(m,2)).toString();
    }

    //将数字转换为指定长度的字符串，如果长度不够前面补全数字0
    public static String asNumber(int ver,int length) {
        String str = String.valueOf(ver);
        int i = length - str.length();
        if (i > 0){
            StringBuilder sb = new StringBuilder();
            for (; i > 0; i--) {
                sb.append("0");
            }
            str = sb.append(str).toString();
        }
        return str;
    }
}
