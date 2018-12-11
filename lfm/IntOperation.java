package ICS2018.lfm;

import ICS2018.lfm.Util;

public class IntOperation {
    /**
     * 整数加法，要求调用{@link Adder.adder(String, String, char, int) adder}方法实现。<br/>
     * 例：integerAddition("0100", "0011", 8)
     *
     * @param operand1 二进制补码表示的被加数
     * @param operand2 二进制补码表示的加数
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
     * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
     */
    public static String integerAddition(String operand1, String operand2, int length) {
        String op1 = Util.signExt(operand1, length);
        String op2 = Util.signExt(operand2, length);
        return Adder.adder(op1, op2, '0', length);
    }

    /**
     * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
     * 例：integerSubtraction("0100", "0011", 8)
     *
     * @param operand1 二进制补码表示的被减数
     * @param operand2 二进制补码表示的减数
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
     * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
     */
    public static String integerSubtraction(String operand1, String operand2, int length) {
        String op1 = Util.signExt(operand1, length);
        String op2 = Util.signExt(operand2, length);
        return Adder.adder(op1, Adder.minusInteger(op2), '0', length);//减法就是加上补码非
    }

    /**
     * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
     * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
     * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
     * 例：signedAddition("1100", "1011", 8)
     *
     * @param operand1 二进制原码表示的被加数，其中第1位为符号位
     * @param operand2 二进制原码表示的加数，其中第1位为符号位
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
     * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
     */
    public static String signedAddition(String operand1, String operand2, int length) {
        char signA = operand1.charAt(0);
        char signB = operand2.charAt(0);
        String op1 = Util.zeroExt(operand1.substring(1), length);
        String op2 = Util.zeroExt(operand2.substring(1), length);
        if (signA == signB) { //相同符号
            String tmpResult = Adder.adder(op1, op2, '0', length);
            return tmpResult.substring(0, 1)
                    .concat(String.valueOf(signA))
                    .concat(tmpResult.substring(1));
        } else {
            op2 = Adder.minusInteger(op2); //取补
            String tmpResult = Adder.adder(op1, op2, '0', length); //做减法
            if (tmpResult.charAt(0) == '1') { //存在进位，那么正常
                return "0"
                        .concat(String.valueOf(signA))
                        .concat(tmpResult.substring(1));
            } else {                            //不存在进位，符号位-signA ，结果取补
                char sign = signA == '0' ? '1' : '0';
                return "0"
                        .concat(String.valueOf(sign))
                        .concat(Adder.minusInteger(tmpResult.substring(1)));
            }
        }
    }

    /**
     * 原码整数乘法，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
     * 例：integerMultiplication("0100", "0011", 8)
     *
     * @param operand1 二进制原码表示的被乘数
     * @param operand2 二进制原码表示的乘数
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位零扩展
     * @return 长度为length+2的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），第二位为符号位
     * 后length位是相乘结果
     * <p>
     * //此处结果应该是 2 * len + 1 吧。。。
     * <p>
     * len暂定为操作数的两倍
     */

    public static String normalMutiplication(String operand1, String operand2, int length) {
        char resultSign = Adder.xorChar(operand1.charAt(0), operand2.charAt(0)); //符号位取异或
        String op1 = Util.zeroExt(operand1.substring(1), length);
        String op2 = Util.zeroExt(operand2.substring(1), length);
//        String resultStr = Util.zeroExt(op1, 2 * length + 1);//被乘数载入低位
        String resultStr = Util.zeroExt(op1, length + 1); //结果寄存器，最高位为进位
//        String adder = Shifter.leftShift(op2, length); //乘数左移length位，用于之后的相加
        String adder = Shifter.leftShift(op2, length / 2);  //乘数左移len/2位
        for (int i = 0; i < length / 2; i++) {
            char X0 = resultStr.charAt(length);
            if (X0 == '1') {  //如果最右侧位为1，需要加乘数到部分积
                resultStr = Adder.adder(resultStr.substring(1),
                        adder, '0', length);
            }
            resultStr = Shifter.logRightShift(resultStr, 1);//整个结果逻辑右移
        }
        return String.valueOf(resultStr.charAt(0))
                .concat(String.valueOf(resultSign))
                .concat(resultStr.substring(1));
    }

    /**
     * 补码整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
     * 例：integerMultiplication("0100", "0011", 8)
     *
     * @param operand1 二进制补码表示的被乘数
     * @param operand2 二进制补码表示的乘数
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
     * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
     * <p>
     * 溢出判断 signA ^ signB ^ signResult
     * <p>
     * 此处的length定为操作数长度，结果长度设定为 1 + 2* Len
     */
    public static String integerMultiplication(String operand1, String operand2, int length) {
        char signA = operand1.charAt(0);
        char signB = operand2.charAt(0);
        char Y0 = '0';
        operand1 = Util.signExt(operand1, length);
        operand2 = Util.signExt(operand2, length);
        String result = Util.zeroExt(operand2, length * 2); //操作数Y进入低位  //注意这里是零扩展，因为我们已经不需要管数据内容
//        String result = Util.signExt(operand2, length * 2);
        String adder = Shifter.leftShift(Util.zeroExt(operand1, length * 2), length); //构造加数
        String subber = Shifter.leftShift(
                Util.zeroExt(Adder.minusInteger(operand1), length * 2),
                length);                                                             //构造减数
        for (int i = 0; i < length; i++) {
            char Y1 = result.charAt(length * 2 - 1);
            if (Y0 != Y1) {
                if (Y0 == '0') { //case Y1Y0 = 10 , we add the subber
                    result = Adder.adder(subber, result, '0', length * 2).substring(1);
                } else {         //case Y1Y0 = 01 , we add the adder
                    result = Adder.adder(adder, result, '0', length * 2).substring(1);
                }
            }
            Y0 = result.charAt(length * 2 - 1); //设置Y0
            result = Shifter.ariRightShift(result, 1);//算术右移一位
        }
        char overFlow = Adder.xorChar(signA, Adder.xorChar(signB, result.charAt(0)));//溢出判定
        return String.valueOf(overFlow)
                .concat(result);
    }

    /**
     * 补码除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
     * 例：integerDivision("0100", "0011", 8)
     *
     * @param operand1 二进制补码表示的被除数
     * @param operand2 二进制补码表示的除数
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
     * @param addBack  是否是恢复余数
     * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
     */
    public static String integerDivision(String operand1, String operand2, int length, boolean addBack) {
        //恢复余数除法相对简单
        String op1 = Util.signExt(operand1, length);
        String op2 = Util.signExt(operand2, length);//首先符号扩展到length位
        // -----[溢出][商][余数]
        //特殊情况判定
        // 1. 0/ not 0 ---返回全0
        if (!op1.contains("1") && op2.contains("1")) {
            return Util.zeroExt("0", 2 * length + 1);
        }
        // 2. not 0 / 0 --- exception
        if (op1.contains("1") && !op2.contains("1")) {
            return "Zero divisor exception";
        }
        //3. 0 / 0  ---NaN
        if (!op1.contains("1") && !op2.contains("1")) {
            return "NaN";
        }
        //4. 溢出情况---
        // 由于是n位除以n位，故只存在n位的最小补码值/(-1)发生上溢出
        if (!op1.substring(1).contains("1") && !op2.contains("0")) {
            return "1".concat(Util.zeroExt("0", 2 * length));
        }

        //正常情况
        char signDividend = op1.charAt(0);//得到被除数的符号位
        char signDivisor = op2.charAt(0);//得到除数的符号位
        String result = Util.signExt(op1, length * 2);//被除数符号扩展到2*length位
        String adder = Shifter.leftShift(Util.zeroExt(op2, length * 2), length);//加除数单元
        String subber = Shifter.leftShift(             //减除数单元
                Util.zeroExt(Adder.minusInteger(op2), //得到除数补码值
                        length * 2), length);
        if (addBack) {//恢复余数除法
            for (int i = 0; i < length; i++) {
                result = Shifter.leftShift(result, 1);
                char signRest = result.charAt(0);
                String tmpVal;
                if (signRest != signDivisor) {  //余数和除数   异号相加
                    tmpVal = Adder.adder(result, adder, '0', 2 * length).substring(1);
                } else {
//                    tmpVal=Adder.adder(result,subber,2*length).substring(1);
                    tmpVal = Adder.adder(result, subber, '0', 2 * length).substring(1);
                }
                if (tmpVal.charAt(0) != signRest) {//余数符号改变  result不做任何操作
                } else {
                    result = tmpVal.substring(0, 2 * length - 1).concat("1"); //商补1
                }
            }
            String rest = result.substring(0, length);
            String quotient = result.substring(length);
            if (signDividend != signDivisor) {
                quotient = Adder.minusInteger(quotient);
            }
            return "0"
                    .concat("." + quotient)
                    .concat("." + rest);
        } else {     //不恢复余数除法
            char tmpQ;
            if (signDividend == signDivisor) {   //做减法
                result = Adder.adder(result, subber, '0', length * 2).substring(1);
            } else {                             //做加法
                result = Adder.adder(result, adder, '0', length * 2).substring(1);
            }
            tmpQ = result.charAt(0) == signDivisor ? '1' : '0';//上商
            System.out.println(tmpQ);
            for (int i = 0; i < length; i++) {
                //余数符号和除数符号一致,补1,否则补0
                result = Shifter.leftShift(result, 1)
                        .substring(0, length * 2 - 1)
                        .concat(String.valueOf(tmpQ));//先移位
                char signRest = result.charAt(0);
                if (signRest != signDivisor) {  //余数和除数   异号相加
//                    result=Adder.adder(result,adder,2*length).substring(1);
                    result = Adder.adder(result, adder, '0', 2 * length).substring(1);
                } else {
//                    result=Adder.adder(result,subber,2*length).substring(1);
                    result = Adder.adder(result, subber, '0', 2 * length).substring(1);
                }
                tmpQ = result.charAt(0) == signDivisor ? '1' : '0';//上商
            }
            String quotient = result.substring(length);
            String rest = result.substring(0, length);
            //修正商
            quotient = Shifter.leftShift(quotient, 1)
                    .substring(0, length - 1)
                    .concat(String.valueOf(tmpQ));
            if (signDividend != signDivisor) {
                quotient = Adder.oneAdder(quotient).substring(1); //加1返回的是len+1的字符串
            }
            //修正余数
            if (rest.charAt(0) != signDividend) {
                if (signDividend != signDivisor) {//余数减除数
                    rest = Adder.adder(rest, Adder.minusInteger(op2), '0', length).substring(1);
                } else {//余数加除数
                    rest = Adder.adder(rest, op2, '0', length).substring(1);
                }
            }
            return "0"
                    .concat("." + quotient)
                    .concat("." + rest);
        }
    }

}
