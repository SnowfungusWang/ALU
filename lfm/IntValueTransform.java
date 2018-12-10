package ICS2018.my;

import java.math.BigInteger;

/**
 * 该类用于十进制数转化为二进制数
 * 以及各种类型数之间的直接转化
 */
public class IntValueTransform {
    /**
     * 真值转补码
     * 生成十进制整数的二进制补码表示。<br/>
     * 例：integerRepresentation("9", 8)
     *
     * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
     * @param length 二进制补码表示的长度
     * @return number的二进制补码表示，长度为length
     */
    public static String true2Compliment(String number, int length) {
        if (number.charAt(0) == '-') {//如果是负数
            //在字符串表示中,2进制取补可以不采取取反加1
            number = new BigInteger(number.substring(1)).toString(2);
            number = Util.zeroExt(number, length); //需要先0扩展一下
            int lastOneIndex = number.lastIndexOf("1"); //然后得到那个1的位置
            number = Util.not(number.substring(0, lastOneIndex)) +
                    number.substring(lastOneIndex);
            return number;
        } else {
            number = new BigInteger(number).toString(2);
            return Util.zeroExt(number, length);
        }
    }


    /**
     * 真值转原码
     * 生成十进制整数的二进制原码表示
     *
     * @param number :真实数值，可以带有符号-
     * @param length :含符号位的长度
     *               <p>
     *               最终的数值长度len
     */
    public static String true2Origin(String number, int length) {
        if (number.charAt(0) == '-') {//如果是负数
            //在字符串表示中,2进制取补可以不采取取反加1
            number = new BigInteger(number.substring(1)).toString(2);
            return "1" + Util.zeroExt(number, length - 1);
        } else {
            number = new BigInteger(number).toString(2);
            return "0" + Util.zeroExt(number, length - 1);
        }
    }

    /**
     * 补码转移码 ,相当于减去偏移量
     * 偏移量为了迎合IEE754，定义为 2^(k-1) -1
     * 实质为最高位取反，然后末尾减一
     *
     * @param number:二进制补码数
     * @param length        :最终移码位数
     */
    public static String compliment2Move(String number, int length) {
        number = Util.signExt(number, length);//先进行符号扩展
        if (length == 1)//特殊情况，只有1位
            return number;
        if (number.charAt(0) == '0') {
            number = "1".concat(number.substring(1));//最高位取反
        } else {
            number = "0".concat(number.substring(1));
        }
        return Util.subOne(number);        //执行加1
    }

    /**
     * 移码转补码
     * 不需要给出长度，直接返回number的位数的补码值
     * 首位取反，然后末尾加一
     */
    public static String move2Compliment(String number) {
        int length = number.length();
        number = Util.signExt(number, length);//先进行符号扩展
        if (length == 1)//特殊情况，只有1位
            return number;
        if (number.charAt(0) == '0') {
            number = "1".concat(number.substring(1));//最高位取反
        } else {
            number = "0".concat(number.substring(1));
        }
        return Util.addOne(number);        //执行加1
    }


}
