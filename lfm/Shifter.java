package ICS2018.my;

public class Shifter {
    /**
     * 左移操作。<br/>
     * 例：leftShift("00001001", 2)
     *
     * @param operand 二进制表示的操作数
     * @param n       左移的位数
     * @return operand左移n位的结果
     */
    public static String leftShift(String operand, int n) {
        if (n >= operand.length()) {
            return Util.zeroExt("0", operand.length());
        } else {
            return operand.substring(n).concat(Util.zeroExt("0", n));
        }
    }

    /**
     * 逻辑右移操作。<br/>
     * 例：logRightShift("11110110", 2)
     *
     * @param operand 二进制表示的操作数
     * @param n       右移的位数
     * @return operand逻辑右移n位的结果
     */
    public static String logRightShift(String operand, int n) {
        if (n >= operand.length())
            return Util.zeroExt("0", operand.length());
        else {
            return Util.zeroExt("0", n)
                    .concat(operand.substring(0, operand.length() - n));
        }
    }

    /**
     * 算术右移操作。<br/>
     * 例：logRightShift("11110110", 2)
     *
     * @param operand 二进制表示的操作数
     * @param n       右移的位数
     * @return operand算术右移n位的结果
     */
    public static String ariRightShift(String operand, int n) {
        if (n >= operand.length())//如果超出，那么全为1 或者全为0
            return Util.zeroExt(operand.substring(0, 1), operand.length());
        int len = operand.length();
        operand = Util.signExt(operand, operand.length() + n);
        return operand.substring(0, len);
    }
}
