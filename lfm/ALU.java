package ICS2018.my;

public class ALU {
    public static void main(String[] args) {
//        String s = new ALU().integerRepresentation("-1", 16);
        System.out.println(Util.signExt("11011", 4));
    }
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
    public String floatRepresentation(String number, int eLength, int sLength) {
        String tmp = number;
        char sign = tmp.charAt(0);
        return number;
    }

    /**
     * 将无符号十进制小数转换为二进制字符串并返回的方法。<br/>
     *
     * @param number  十进制小数字符串
     * @param sLength 长度要求之一，当不满足使用 eLength 的要求时，返回的字符串的长度要求在第一个"1"出现之后再加上 sLength 位
     * @return 二进制字符串，可能带有小数点，可能为"NaN"
     */
    private String getBinaryString(String number, int sLength) {
        return number;
    }

    /**
     * 移码的拓展的方法（保留最高位的符号拓展）
     *
     * @param number  需要拓展的二进制移码
     * @param eLength 需要拓展达到的长度
     * @return 拓展后的长度为eLength的二进制移码
     */
    private String extendShiftCode(String number, int eLength) {
        return number;
    }

    /**
     * 计算二进制补码表示的整数的真值。<br/>
     * 例：integerTrueValue("00001001")
     *
     * @param operand 二进制补码表示的操作数
     * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
     */
    public String integerTrueValue(String operand) {
        return operand;
    }

    /**
     * 计算二进制原码表示的浮点数的真值。<br/>
     * 例：floatTrueValue("01000001001101100000", 8, 11)
     *
     * @param operand 二进制表示的操作数
     * @param eLength 指数的长度，取值大于等于 4
     * @param sLength 尾数的长度，取值大于等于 4
     * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
     */
    public String floatTrueValue(String operand, int eLength, int sLength) {
        return operand;
    }

}

