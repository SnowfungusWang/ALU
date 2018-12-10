package ICS2018.lfm;

/**
 * 该类用于十进制小数转化为[二进制定点小数]/[二进制浮点表示]
 */
public class FloatValueTransform {
    /**
     * 生成十进制浮点数的二进制表示。
     * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
     * 舍入策略为向0舍入。<br/>
     * 例：floatRepresentation("11.375", 8, 11)
     *
     * @param number  十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
     * @param eLength 指数的长度，取值大于等于 4
     * @param sLength 尾数的长度，取值大于等于 4
     * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
     */
    public static String dec2Float(String number, int eLength, int sLength) {
        return number;
    }

    /**
     * 将无符号十进制小数转换为二进制字符串并返回的方法。<br/>
     *
     * @param number  十进制小数字符串
     * @param sLength 长度要求之一，当不满足使用 eLength 的要求时
     *                返回的字符串的长度要求在第一个"1"出现之后再加上 sLength 位(有效位长度)
     * @return 二进制字符串，可能带有小数点，可能为"NaN"
     */
    public static String dec2FixFloat(String number, int sLength) {
        return number;
    }

    /**
     * 将二进制浮点数转化为十进制小数
     */
    public static String Float2Dec(String number, int elength, int slength) {
        return number;
    }
}
