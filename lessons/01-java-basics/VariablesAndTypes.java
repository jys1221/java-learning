/**
 * 知识点 2：变量与数据类型
 *
 * 变量 = 一块有名字的内存空间，用来存数据。
 * 声明变量的格式：  数据类型 变量名 = 初始值;
 *
 * Java 的数据类型分两大类：
 *   基本类型（8 种，直接存数值）：byte short int long float double char boolean
 *   引用类型（存的是对象的地址）：String、数组、自己写的类、集合……
 */
public class VariablesAndTypes {

    public static void main(String[] args) {

        // ============ 一、8 种基本类型 ============

        // byte：1 字节，范围 -128 ~ 127。用得少，主要在文件/网络传输时用
        byte b = 100;

        // short：2 字节，范围 -3 万多 ~ 3 万多。也少用
        short s = 30000;

        // int：4 字节，范围约 -21 亿 ~ 21 亿。整数默认就是 int，最常用
        int age = 20;

        // long：8 字节，范围极大。写数字后面必须加 L（大小写都行），否则会被当 int
        long population = 8000000000L;   // 全球人口约 80 亿

        // float：4 字节小数，精度低。写数字后面必须加 f
        float weight = 92.5f;

        // double：8 字节小数，精度高。小数默认就是 double，最常用
        double height = 1.75;

        // char：2 字节，存单个字符，用单引号。本质是数字（Unicode 编码）
        char grade = 'A';
        char chinese = '佳';          // 中文也是单个 char

        // boolean：只有 true / false 两个值，不能写 1/0
        boolean isStudent = true;

        // 打印出来看看
        System.out.println("byte=" + b + ", short=" + s + ", int=" + age);
        System.out.println("long=" + population + ", float=" + weight + ", double=" + height);
        System.out.println("char=" + grade + ", boolean=" + isStudent);

        // ============ 二、String 是引用类型，不是基本类型 ============
        String name = "佳良";              // 双引号，首字母大写
        String empty = "";                 // 空字符串，长度 0
        String nullStr = null;             // null 表示"没有任何对象"，不是空字符串

        // 常用方法（注意：字符串本身不可变，所有方法都返回新字符串）
        System.out.println("长度：" + name.length());            // 2
        System.out.println("转大写：" + name.toUpperCase());      // 佳良（中文没有大小写）
        System.out.println("包含'佳'吗：" + name.contains("佳"));  // true
        System.out.println("截取前1个：" + name.substring(0, 1)); // 佳
        System.out.println("去空格：[" + "  hi  ".trim() + "]");  // [hi]
        System.out.println("替换：" + "a-b-c".replace("-", "+")); // a+b+c
        System.out.println("拼接：" + String.join("/", "2026", "11", "21")); // 2026/11/21

        // ============ 三、类型转换 ============

        // 自动转换（小范围 → 大范围，不会丢数据）
        int small = 100;
        long big = small;        // int 自动变 long
        double d = small;        // int 自动变 double
        System.out.println("自动转换：" + big + ", " + d);

        // 强制转换（大范围 → 小范围，可能丢精度，要自己写括号）
        double pi = 3.14159;
        int piInt = (int) pi;    // 小数部分被直接砍掉，不是四舍五入
        System.out.println("3.14159 强转 int = " + piInt);   // 3

        int bigNum = 300;
        byte overflow = (byte) bigNum;   // 300 超出 byte 范围，会发生"溢出回绕"
        System.out.println("300 强转 byte = " + overflow);   // 44（300 - 256）

        // 字符串 ↔ 数字
        String numStr = "123";
        int parsed = Integer.parseInt(numStr);       // 字符串转 int
        double parsedD = Double.parseDouble("3.14");  // 字符串转 double
        String back = String.valueOf(parsed);         // 数字转字符串（也可以用 "" + parsed）
        System.out.println("解析：" + parsed + ", " + parsedD + ", 回转：" + back);

        // ============ 四、常量 final ============
        // final 表示"只能赋值一次"，之后不能再改。常量名习惯全大写
        final double PI = 3.1415926;
        final int MAX_SCORE = 100;
        System.out.println("圆周率=" + PI + ", 满分=" + MAX_SCORE);
        // PI = 3;   // 取消注释会编译报错：cannot assign a value to final variable

        // ============ 五、var 类型推断（Java 10+） ============
        // 编译器根据右边的值自动推断类型，左边不用写类型
        var city = "合肥";            // 推断为 String
        var count = 10;               // 推断为 int
        var score = 95.5;             // 推断为 double
        System.out.println(city + " " + count + " " + score);
        // 注意：var 只能用于局部变量，不能用于成员变量/方法参数
    }
}
