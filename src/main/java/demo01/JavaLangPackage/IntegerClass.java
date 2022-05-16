package demo01.JavaLangPackage;
/*
 * 对java.lang包下的Integer类的阅读
 */
public class IntegerClass {
    /*Integer包拥有两个构造方法，一个是直接传入基本类型int的数值，一个是传入字符串数字->：
     问题1：假如在这个字符串中加入一个字母，会发生什么事---
     问题2：输入的长度最大是多长，最小又是如何
     问题3：负数又如何判断？---取字符串第一位
     */
    //构造方法，定义value值，然后传入
    private final int value;


    public IntegerClass(int value) {
        this.value = value;

    }


    /**
     * 以字符串作为参数传入的构造方法
     * @param s
     * @throws NumberFormatException 抛出了数字格式错误的异常
     */
    public IntegerClass(String s) throws NumberFormatException {
        //radix在此处是什么作用？---十进制，默认会将输入的字符串认为是十进制进行转换
        this.value = parseInt(s, 10);
    }

    /**
     *
     * @param s 需要转换成Integer类型的字符串
     * @param radix 进制数，表示输入的s是几进制的数字
     * @return
     * @throws NumberFormatException
     */
    public static int parseInt(String s, int radix) throws NumberFormatException {
        /*
         * WARNING: This method may be invoked early during VM initialization
         * before IntegerCache is initialized. Care must be taken to not use
         * the valueOf method.
         * 为什么此处提到在IntegerCache初始化前不要使用valueOf这个方法？
         */


        if (s == null) {
            throw new NumberFormatException("null");
        }

        /**
         *     此处意为输入的最小为2进制
         *     Character.MIN_RADIX=2;
         */
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix +
                    " less than Character.MIN_RADIX");
        }
        /**
         *   Character.MAX_RADIX= 36;
         */
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix +
                    " greater than Character.MAX_RADIX");
        }

        int result = 0;
        //判断正负
        boolean negative = false;
        int i = 0, len = s.length();

        //MAX_VALUE = 0x7fffffff---2^31-1
        int limit = -Integer.MAX_VALUE;


        //？
        int multmin;
        //数值？
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {
                // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    //如果是负数，下界则为最小值，MIN_VALUE = 0x80000000，-2^31
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+')
                    //如果第一位符号不是“+”，没有判断必要，直接抛出
                    throw new NumberFormatException("For input string: \"" + s + "\"");

                if (len == 1) // Cannot have lone "+" or "-"
                    throw new NumberFormatException("For input string: \"" + s + "\"");

                i++;
            }
            //此处判断的是能够预留给数值的位值
            // 假如limit=10，而radix也=10，那么，显而易见的当满十进一之后，就会造成溢出
            // 如果limit是11,12，除了以后代表还能再进行两次运算（此处大概能感受到思想，但是表达不出来）
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                /*
                    此处理解难点是digit里面的
                        CharacterData.of(codePoint).digit(codePoint, radix);
                    方法
                    在of方法里，进行了无符号位移的判断：
                        此处向右无符号位移8位是否为0，即一个数是否为小于或等于255
                        if (ch >>> 8 == 0)
                            return CharacterDataLatin1.instance;
                        一般来说，输入的内容不会超过Z的大小，而Z在此之内
                    返回的是一个单例模式下的实例对象，使用.instance构造，可以在需要时才构造，构造后使用对象中的digit方法：
                        int digit(int ch, int radix) {
                            int value = -1;
                            //---进制中的范围判断---
                            if (radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX) {
                                //---getProperties是从一个字符数组中寻到到ch位置所对应的值并转为int类型输出---
                                int val = getProperties(ch);
                                //---此处取了与11111的逻辑与，（搞不懂为什么要取）---
                                int kind = val & 0x1F;
                                //---DECIMAL_DIGIT_NUMBER=9---
                                if (kind == Character.DECIMAL_DIGIT_NUMBER) {
                                    //---ch与1111100000进行逻辑与后向右位移五位再与11111取逻辑与，最后再加上ch---
                                    value = ch + ((val & 0x3E0) >> 5) & 0x1F;
                                }
                                //---0xc00=110000000000
                                //---0x00000C00本质上还是c00，在前面添加0方便比较

                                else if ((val & 0xC00) == 0x00000C00) {
                                    // Java supradecimal digit
                                    value = (ch + ((val & 0x3E0) >> 5) & 0x1F) + 10;
                                }
                            }
                            return (value < radix) ? value : -1;
                        }
                 */
                digit = Character.digit(s.charAt(i++),radix);
                if (digit < 0) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                if (result < multmin) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                }
                result -= digit;
            }
        } else {
            throw new NumberFormatException("For input string: \"" + s + "\"");
        }
        return negative ? result : -result;
    }
}
