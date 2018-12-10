package ICS2018.lfm;

public class Util {
//    public static int DATA_SIZE = 4;

    public static int getSign(int val, int dataSize) {
        return 0x01 & (val >> (dataSize - 1));
    }

    public static int getLower(int val, int dataSize) {
        return val & (0xFFFFFFFF >>> (32 - dataSize));
    }

    /**
     * Count from lower bit
     * end [31,0]
     */
    public static int getRangeVal(int val, int leftEnd, int rightEnd) {
        int maskBits = (0xFFFFFFFF >>> (31 - leftEnd)) & (0xFFFFFFFF << (rightEnd));
        val &= maskBits;
        return val >>> rightEnd;
    }

    public static String getLower(String val, int dataSize) {
        int len = val.length();
        return val.substring(len - dataSize);
    }

    /**
     * Count from lower bit
     * end [31,0]
     */
    public static String getRangeVal(String val, int leftEnd, int rightEnd) {
        int len = val.length();
        return val.substring(len - leftEnd - 1, len - rightEnd);
    }

    /**
     * @param val:   补码表示
     * @param length :需要符号扩展的位数
     * @return :最终length位的补码结果
     */
    public static String signExt(String val, int length) {
        if (val.length() >= length)
            return val.substring(val.length() - length);
        else {
            String sign = val.substring(0, 1);
            while (val.length() < length) {
                val = sign + val;
            }
            return val;
        }
    }

    /**
     * 零扩展
     */
    public static String zeroExt(String val, int length) {
        if (val.length() >= length)
            return val.substring(val.length() - length);
        else {
            while (val.length() < length) {
                val = "0" + val;
            }
            return val;
        }
    }

    /**
     * 各位取反
     */
    public static String not(String val) {
        return val.replaceAll("1", "-")
                .replaceAll("0", "1")
                .replaceAll("-", "0");
    }

    /**
     * 加1器
     * 本质为——从右边开始，遇到1就取反，遇到第一个0取反并停止
     * 如果全是1，那么返回0
     *
     * @param number :二进制数值,不管其存储属性
     */
    public static String addOne(String number) {
        int len = number.length();
        int zeroIndex = number.lastIndexOf("0");
        if (zeroIndex < 0)//全部都是1
            return Util.zeroExt("0", len);
        else {
            return number.substring(0, zeroIndex) +
                    Util.not(number.substring(zeroIndex));
        }
    }

    /**
     * 减1器
     * 本质为——从右边开始，遇到0就取反，遇到第一个1取反并停止
     * 如果全是0，那么返回全部1
     *
     * @param number :二进制数值,不管其存储属性
     */
    public static String subOne(String number) {
        int len = number.length();
        int oneIndex = number.lastIndexOf("1");
        if (oneIndex < 0)
            return Util.signExt("1", len);
        else
            return number.substring(0, oneIndex) +
                    Util.not(number.substring(oneIndex));
    }
}
