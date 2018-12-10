package ICS2018.my;

public class Adder {
    /**
     * 各位取反
     */
    public static String neg(String number) {
        char[] chars = number.toCharArray();
        for (int i = 0; i < chars.length; i++)
            chars[i] = chars[i] == '0' ? '1' : '0';
        return new String(chars);
    }

    /**
     * 指定的ch1 ch2 进行异或操作
     */
    public static char xorChar(char ch1, char ch2) {
        switch (ch1) {
            case '1':
                switch (ch2) {
                    case '1':
                        return '0';
                    case '0':
                        return '1';
                    default:
                        return '*';
                }
            case '0':
                switch (ch2) {
                    case '1':
                        return '1';
                    case '0':
                        return '0';
                    default:
                        return '*';
                }
            default:
                return '*';
        }
    }

    /**
     * 指定ch1 ch2 进行与操作
     */
    public static char andChar(char ch1, char ch2) {
        switch (ch1) {
            case '1':
                switch (ch2) {
                    case '0':
                        return '0';
                    case '1':
                        return '1';
                    default:
                        return '*';
                }
            case '0':
                switch (ch2) {
                    case '1':
                        return '0';
                    case '0':
                        return '0';
                    default:
                        return '*';
                }
            default:
                return '*';
        }
    }

    /**
     * 指定ch1 ch2 进行或操作
     */
    public static char orChar(char ch1, char ch2) {
        switch (ch1) {
            case '1':
                switch (ch2) {
                    case '0':
                        return '1';
                    case '1':
                        return '1';
                    default:
                        return '*';
                }
            case '0':
                switch (ch2) {
                    case '0':
                        return '0';
                    case '1':
                        return '1';
                    default:
                        return '*';
                }
            default:
                return '*';
        }
    }

    /**
     * 全加器，对两位以及进位进行加法运算。<br/>
     * 例：fullAdder('1', '1', '0')
     *
     * @param x 被加数的某一位，取0或1
     * @param y 加数的某一位，取0或1
     * @param c 低位对当前位的进位，取0或1
     * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
     */
    public static String fullAdder(char x, char y, char c) {
        char s = xorChar(x, xorChar(y, c));
        char a1 = andChar(x, y);
        char a2 = andChar(x, c);
        char a3 = andChar(y, c);
        char c_ = orChar(a1, orChar(a2, a3));
        return String.valueOf(c_).concat(String.valueOf(s));
    }

    /**
     * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
     * 例：claAdder("1001", "0001", '1')
     *
     * @param operand1 4位二进制表示的被加数
     * @param operand2 4位二进制表示的加数
     * @param c        低位对当前位的进位，取0或1
     * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
     */
    public static String claAdder(String operand1, String operand2, char c) {
        char[] operand_A = operand1.toCharArray();//暂时假定输入字符串长度都是4
        char[] operand_B = operand2.toCharArray();
        char[] Gi = new char[4];//Gi = Xi AND Yi
        char[] Pi = new char[4];//Pi = Xi OR Yi
        char[] Ci = new char[5];//Ci = (Pi AND C(i-1)) OR (Gi)
        Ci[0] = c;
        char[] result = new char[5]; //result [ 0, 1 , 2 , 3 , 4] , 0 为最高位
        for (int i = 0; i < 4; i++) {
            char xi = operand_A[3 - i];
            char yi = operand_B[3 - i];
            Gi[i] = andChar(xi, yi);
            Pi[i] = orChar(xi, yi);
            Ci[i + 1] = orChar(Gi[i], andChar(Pi[i], Ci[i]));
        }
        for (int i = 4; i > 0; i--) {
            char a = operand_A[i - 1];
            char b = operand_B[i - 1];
            char c1 = Ci[4 - i]; //ci 是逆序装填的 index = 0  对应了结果的最后一位计算时的C
            result[i] = fullAdder(a, b, c1).charAt(1);
        }
        result[0] = Ci[4];
        return new String(result);
    }

    /**
     * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
     * 例：adder("0100", "0011", ‘0’, 8)
     *
     * @param operand1 二进制补码表示的被加数
     * @param operand2 二进制补码表示的加数
     * @param c        最低位进位
     * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
     * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
     */
    public static String adder(String operand1, String operand2, char c, int length) {
        operand1 = Util.signExt(operand1, length);
        operand2 = Util.signExt(operand2, length);//符号扩展
        String[] strings = new String[length / 4];
        char Ci = c;
        for (int i = 0; i < length / 4; i++) {
            int beginIndex = length - 4 * (i + 1);
            String op1 = operand1.substring(beginIndex);
            String op2 = operand2.substring(beginIndex);
            String tmpStr = claAdder(op1, op2, Ci);
            Ci = tmpStr.charAt(0);
            strings[length / 4 - 1 - i] = tmpStr.substring(1); //按照
        }
        String result = String.valueOf(Ci);
        for (int i = 0; i < length / 4; i++) {
            result = result.concat(strings[i]);//得到结果后连接字符串数组
        }
        return result;
    }

    /**
     * 加一器，实现操作数加1的运算。
     * 需要采用与门、或门、异或门等模拟，
     * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
     * {@link #claAdder(String, String, char) claAdder}、
     * {@link #adder(String, String, char, int) adder}、
     * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
     * 例：oneAdder("00001001")
     * <p>
     * <p>
     * 加一的本质：
     * 最右侧的那个0(包括0)开始，每一位取反即可
     *
     * @param operand 二进制补码表示的操作数
     * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
     */
    public static String oneAdder(String operand) {
        int len = operand.length();
        int zeroIndex = operand.lastIndexOf('0');
        char[] chars = operand.toCharArray();
        if (zeroIndex < 0) {
            return "1" + Util.zeroExt("0", len);
        }
        for (int i = zeroIndex; i < len; i++) {
            chars[i] = chars[i] == '0' ? '1' : '0';
        }
        return "0".concat(new String(chars));
    }

    /**
     * 返回二进制补码的补码非
     * 取反加一
     */
    public static String minusInteger(String number) {
        return oneAdder(neg(number));
    }

    /**
     * 取补码非，并且进行符号扩展
     */
    public static String minusInteger(String number, int len) {
        return Util.signExt(oneAdder(neg(number)), len);
    }
}
