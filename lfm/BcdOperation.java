package ICS2018.lfm;

public class BcdOperation {
    /**
     * 十进制数转BCD码
     *
     * @param val 十进制整数
     * @return 1 +  4*(整数位数) BCD码
     */
    public static String getBCDString(int val) {
        if (val == 0) {
            return Util.zeroExt("0", 5); // 如果是0,就返回5位0
        }
        char sign = val < 0 ? '1' : '0'; //得到符号位
        String result = "";
        val = Math.abs(val);
        while (val > 0) {
            int tmpVal = val % 10;
            result = getBCDString_4(tmpVal).concat(result);
            val = val / 10;
        }
        return String.valueOf(sign)
                .concat(result);
    }

    /**
     * BCD码转十进制整数
     *
     * @param bcd : BCD 码
     * @return BCD码真值
     */
    public static int getTrueVal(String bcd) {
        char sign = bcd.charAt(0);  //得到符号位
        String val = bcd.substring(1);
        if (val.length() % 4 != 0) { //如果BCD码数值位长度不是4的倍数,需要零扩展
            int len = val.length() / 4 + 1;
            val = Util.zeroExt(val, len * 4);
        }
        int result = 0;
        for (int i = 0; i < val.length() / 4; i++) {
            result = result * 10;
            String sub = val.substring(i * 4, i * 4 + 4);
            result += getTrueVal_4(sub);
        }
        return sign == '1' ? -result : result;
    }

    /**
     * 得到4位二进制字符串的十进制真值
     *
     * @param val 二进制字符串，长度为4
     * @return 十进制真值
     */
    private static int getTrueVal_4(String val) {
        int result = 0;
        for (int index = 0; index < 4; index++) {
            result = result * 2;
            if (val.charAt(index) == '1')
                result += 1;
        }
        return result;
    }

    /**
     * @param val 0-9的整数
     * @return 4位二进制数 [0000 - 1001]
     */
    private static String getBCDString_4(int val) {
        String result = "";
        for (int i = 0; i < 4; i++, val = val / 2) {
            if (val % 2 == 1)
                result = "1".concat(result);
            else
                result = "0".concat(result);
        }
        return result;
    }

    /**
     * 得到BCD码的补码形式
     * <p>
     * 取反——取反加10 / 加6取反
     * 然后末尾加1
     *
     * @param operand 长度为4的倍数,如果不是4的倍数，高位补0
     * @return operand的补码值, 字符串长度不变
     */
    private static String getBCDMinis(String operand) {
        if (operand.length() % 4 != 0) {//如果不是4的倍数，高位补0
            int len = operand.length() / 4 + 1;
            operand = Util.zeroExt(operand, len * 4);
        }
        String result = "";
        for (int i = 0; i < operand.length() / 4; i++) {
            String tmpStr = operand.substring(i * 4, (i + 1) * 4);
            tmpStr = Adder.adder(Adder.neg(tmpStr), "1010", 4).substring(1);//取反加10,丢弃进位
            result = result.concat(tmpStr);
        }
        result = Adder.oneAdder(result).substring(1);//加1,丢弃进位
        return result;
    }

    /**
     * BCD码数值部分加法
     *
     * @param op1 :长度为length * 4的二进制BCD码
     * @param op2 :长度为length * 4的二进制BCD码
     * @return 长度为 1 + length * 4 的二进制和,多余的一位指示进位
     */
    private static String BCDadder(String op1, String op2, int length) {
        char carry = '0';
        String result = "";
        for (int i = 0; i < length; i++) {
            int indexBegin = (length - i - 1) * 4;//子字符串开始的索引
            String tmpVal = BCDadder_4(op1.substring(indexBegin, indexBegin + 4),
                    op2.substring(indexBegin, indexBegin + 4), carry);
            carry = tmpVal.charAt(0);               //得到进位
            result = tmpVal.substring(1)            //tmpVal为相对于result的高4位数值
                    .concat(result);
        }
        return String.valueOf(carry)
                .concat(result);
    }

    /**
     * BCD 4位加法器
     * 如果相加之后，和大于 9 , 那么需要再加上6, 进位一定设为1
     *
     * @param op1   4位二进制BCD
     * @param op2   4位二进制BCD
     * @param carry 进位,前一位算术的进位
     * @return 1 + 4 位结果，第一位为进位标识
     */
    private static String BCDadder_4(String op1, String op2, char carry) {
        String tmpVal = Adder.adder(op1, op2, carry, 4);
        if (getTrueVal_4(tmpVal.substring(1)) > 9 || tmpVal.charAt(0) == '1') {
            return "1"
                    .concat(Adder.adder(tmpVal.substring(1),
                            "0110", 4).substring(1));
        } else {
            return "0"
                    .concat(tmpVal.substring(1));
        }
    }

    /**
     * BCD加法运算
     *
     * @param operand1 操作数1，为BCD二进制字符串形式
     * @param operand2 操作数2，为BCD二进制字符串形式
     * @param length   十进制计数位数;操作数的位数为 1 + 4*length;如果数值部分不满4*length位,直接返回null
     * @return 结果的BCD表示形式, 字符串长度 2 + 4*length , 第一位表示进位，第二位表示符号位
     */
    public static String BCD_add(String operand1, String operand2, int length) {
        char sign1 = operand1.charAt(0);
        char sign2 = operand2.charAt(0);
        operand1 = operand1.substring(1);
        operand2 = operand2.substring(1);
        if (operand1.length() < length * 4 || operand2.length() < length * 4)//不满足长度规定
            return null;
        if (sign1 == sign2) {                 //同符号，做加法. 可能会溢出
            String addResult = BCDadder(operand1, operand2, length);
            if (addResult.charAt(0) == '1') { //溢出
                return "1"
                        .concat(String.valueOf(sign1))
                        .concat(addResult.substring(1));
            } else                            //不发生溢出
                return "0"
                        .concat(String.valueOf(sign1))
                        .concat(addResult.substring(1));
        } else {                              //不同符号，做减法. 一定不会溢出
            operand2 = getBCDMinis(operand2); //op2 取补
            String addResult = BCDadder(operand1, operand2, length);
            if (addResult.charAt(0) == '0') {     //没有产生进位,需要结果再次取补，然后符号位为-sign1
                addResult = getBCDMinis(addResult.substring(1));
                char sign = sign1 == '1' ? '0' : '1';
                return "0"
                        .concat(String.valueOf(sign))
                        .concat(addResult);//这里长度已经满足，不需要subString
            } else {                           //产生进位，则进位可以直接丢弃
                return "0"
                        .concat(String.valueOf(sign1))
                        .concat(addResult.substring(1));
            }
        }
    }

    public static String BCD_sub(String operand1, String operand2, int length) {
        if (operand2.charAt(0) == '1')
            return BCD_add(operand1, "0".concat(operand2.substring(1)), length);
        else
            return BCD_add(operand1, "1".concat(operand2.substring(1)), length);
    }

    public static void main(String[] args) {
//        System.out.println(BCDadder("000100100011","100100100011",3));
//        System.out.println(BCDadder_4("1001", "0111", '0'));
        System.out.println(BCD_sub("000110000","000000111",2));
    }
}
