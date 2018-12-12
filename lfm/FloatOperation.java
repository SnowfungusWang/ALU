package ICS2018.lfm;

import ICS2018.a.ALU;

public class FloatOperation {
    /**
     * neg Not a Number
     * 1 1111... 11111....
     */
    private static String N_NAN_F(int eLength, int sLength) {
        return "1".concat(Util.signExt("1", eLength))
                .concat(Util.signExt("1", sLength));
    }

    /**
     * pos Not a Number
     * 0 1111... 11111....
     */
    private static String P_NAN_F(int eLength, int sLength) {
        return "0".concat(Util.signExt("1", eLength))
                .concat(Util.signExt("1", sLength));
    }

    /**
     * neg Inf
     * 1 1111... 0000...
     */
    private static String N_INF_F(int eLength, int sLength) {
        return "1".concat(Util.signExt("1", eLength))
                .concat(Util.zeroExt("0", sLength));
    }

    /**
     * pos Inf
     * 0 1111... 0000...
     */
    private static String P_INF_F(int eLength, int sLength) {
        return "0".concat(Util.signExt("1", eLength))
                .concat(Util.zeroExt("0", sLength));
    }

    /**
     * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
     * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
     *
     * @param operand1 二进制表示的被加数
     * @param operand2 二进制表示的加数
     * @param eLength  指数的长度，取值大于等于 4
     * @param sLength  尾数的长度，取值大于等于 4
     * @param gLength  保护位的长度
     * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
     */
    public static String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
        //拆分
        char sign1 = operand1.charAt(0);
        char sign2 = operand2.charAt(0);
        String exp1 = operand1.substring(1, 1 + eLength);//exp为移码值
        String exp2 = operand2.substring(1, 1 + eLength);
        String significant1 = operand1.substring(1 + eLength);
        String significant2 = operand2.substring(1 + eLength);
        //1.边界条件处理

        //1.1 零参与
        if (!operand1.substring(1).contains("1"))
            return "0".concat(operand2);
        if (!operand2.substring(1).contains("1"))
            return "0".concat(operand1);
        //1.2 NaN  只要有一个NaN存在，就返回N_NAN_F(全1)  其实，如果 N_NAN_F + P_NAN_F 结果为P_NAN_F
        //其它的情况结果均为N_NAN_F,简单起见,就直接返回全1
        if ((!exp1.contains("0") && (significant1.contains("1"))) ||
                (!exp2.contains("0") && (significant2.contains("1")))) {
            return "0".concat(N_NAN_F(eLength, sLength));
        }
        //1.3 Inf  只要有一个Inf,一个正常就返回Inf.
        //同号Inf 返回同号Inf
        //异号Inf 返回N_NAN_F
        if (operand1.substring(1).equals(P_INF_F(eLength, sLength).substring(1))) {//操作数1为Inf
            if (operand2.substring(1).equals(P_INF_F(eLength, sLength).substring(1))) {
                if (sign1 == sign2)
                    return "0".concat(N_NAN_F(eLength, sLength));
                else
                    return "0".concat(operand1);
            }
        }
        if (operand2.substring(1).equals(P_INF_F(eLength, sLength).substring(1))) {//操作数2为Inf,且已经排除两者都是Inf情况,直接返回
            return operand2;
        }
        //***正常计算
        //***预处理——非规格化补偿、尾数添加舍入位、对阶
        if (!exp1.contains("1")) {//非规格化
            exp1 = Adder.oneAdder(exp1).substring(1);//指数加1
            significant1 = "0".concat(significant1).concat(Util.zeroExt("0", gLength));//得到尾数
        } else {
            significant1 = "1".concat(significant1).concat(Util.zeroExt("0", gLength));
        }
        if (!exp2.contains("1")) {
            exp2 = Adder.oneAdder(exp2).substring(1);
            significant2 = "0".concat(significant2).concat(Util.zeroExt("0", gLength));
        } else {
            significant2 = "1".concat(significant2).concat(Util.zeroExt("0", gLength));
        }
        int newSigLen = 1 + sLength + gLength;//新的有效位长度
        //***对齐阶码
        String resultExp;
        ALU alu = new ALU();
        int shiftTimes = Integer.parseInt(alu.integerTrueValue(      //必须要4的倍数,fo了
                alu.integerSubtraction("0" + exp1, "0" + exp2,
                        eLength + 4).substring(1)));
        if (shiftTimes >= 0) {  //exp1 >= exp2, exp2进行靠拢,有效位进行逻辑右移
            resultExp = exp1;
            for (int i = 0; i < shiftTimes; i++) {
                if (!significant2.contains("1")) {//有效位全0,直接返回
                    return "0".concat(operand1);
                }
                significant2 = Shifter.logRightShift(significant2, 1);
            }
        } else {             //exp1 < exp2,  exp1进行靠拢
            shiftTimes = -shiftTimes;
            resultExp = exp2;
            for (int i = 0; i < shiftTimes; i++) {
                if (!significant1.contains("0")) {
                    return "0".concat(operand2);
                }
                significant1 = Shifter.logRightShift(significant1, 1);
            }
        }

        //***有效位相加,需要带上符号位,直接进行原码加法计算
        String resultSignificant;
        String op1 = String.valueOf(sign1).concat(significant1);//拼接符号位
        String op2 = String.valueOf(sign2).concat(significant2);
        String tmpVal = IntOperation.signedAddition(op1, op2, op1.length()); //得到原码加法结果
        char carry = tmpVal.charAt(0);                          //是否为10.xxxx
        char resultSign = tmpVal.charAt(1);                     //最终的符号位
        resultSignificant = tmpVal.substring(2);                //结果的数值部分
        if (carry == '1') { //10.xxxxxxxxx,尾数右移,阶码++
            resultSignificant = Shifter.logRightShift("1" + resultSignificant, 1)
                    .substring(1);//需要把carry也移入

            String tmpExp = Adder.oneAdder(resultExp);
            if (tmpExp.charAt(0) == '1') {
                return "1".concat(P_INF_F(eLength, sLength));//阶值上溢
            }
            resultExp = tmpExp.substring(1);
        }
        //***规格化处理
        // 仅对于0.xxxxxxxxxx ,并且阶码移码值必须大于0
        while (resultSignificant.charAt(1) == '0' && resultExp.contains("1")) {
            resultSignificant = Shifter.leftShift(resultSignificant, 1);
            resultExp =
                    Adder.adder(
                            resultExp,
                            Util.signExt("1", resultExp.length()),//-1
                            resultExp.length()).substring(1); //exp--

        }
        if (!resultExp.contains("1")) { //产生非规格化数,阶码自动加1,需要尾数继续右移一次
            //----------------------!!!!!!!!!不过书上为什么要报告向下溢出啊？？？
            resultSignificant = Shifter.leftShift(resultSignificant, 1);
        }
        //***舍入  因为向0舍入,有效位直接截断
        resultSignificant = resultSignificant.substring(1, 1 + sLength);
        //***return
        return "0"
                .concat(String.valueOf(resultSign))
                .concat(resultExp)
                .concat(resultSignificant);
    }

    /**
     * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
     * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
     *
     * @param operand1 二进制表示的被减数
     * @param operand2 二进制表示的减数
     * @param eLength  指数的长度，取值大于等于 4
     * @param sLength  尾数的长度，取值大于等于 4
     * @param gLength  保护位的长度
     * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
     */
    public static String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
        if (operand2.charAt(0) == '1') {
            return floatAddition(operand1, "0".concat(operand2.substring(1)),
                    eLength, sLength, gLength);
        } else return floatAddition(operand1, "1".concat(operand2.substring(1)),
                eLength, sLength, gLength);
    }

    /**
     * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
     * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
     *
     * @param operand1 二进制表示的被乘数
     * @param operand2 二进制表示的乘数
     * @param eLength  指数的长度，取值大于等于 4
     * @param sLength  尾数的长度，取值大于等于 4
     * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
     */
    public static String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {

        return null;
    }

    /**
     * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
     * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
     *
     * @param operand1 二进制表示的被除数
     * @param operand2 二进制表示的除数
     * @param eLength  指数的长度，取值大于等于 4
     * @param sLength  尾数的长度，取值大于等于 4
     * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
     */
    public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
        return null;
    }
}
