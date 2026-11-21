/**
 * 知识点 1：Hello World —— 认识 Java 程序的基本骨架
 *
 * 一个 Java 程序的最小结构包含四部分：
 *   1. 类（class）：Java 里所有代码都必须写在类里面，不能裸写
 *   2. 主方法（main）：程序的入口，JVM 启动后第一个执行的就是它
 *   3. 语句（statement）：每一行以分号结尾的指令
 *   4. 输出（System.out.println）：把内容打印到控制台
 *
 * 运行方法（在命令行里，确保已装 JDK）：
 *   javac HelloWorld.java     // 编译：生成 HelloWorld.class
 *   java HelloWorld           // 运行：JVM 加载 class 并执行 main
 */
public class HelloWorld {

    /**
     * main 方法：程序入口
     *
     * 为什么是这几个关键字？
     *   public      —— 公开的，JVM 从外面才能调用到它
     *   static      —— 静态的，不用 new 对象就能直接调用（JVM 启动时还没有对象）
     *   void        —— 没有返回值
     *   main        —— 方法名，固定叫 main，JVM 只认这个名字
     *   String[] args —— 接收命令行参数的字符串数组
     */
    public static void main(String[] args) {

        // println：打印一行，结尾自动换行
        System.out.println("Hello, World!");

        // print：打印但不换行，所以下面两句会连在同一行
        System.out.print("你好，");
        System.out.print("佳良");
        System.out.println();  // 单独调用 println() 只是输出一个换行

        // printf：格式化输出。%s 占位字符串，%d 占位整数，%.2f 占位保留两位小数的浮点数
        System.out.printf("我叫%s，今年%d岁，体重%.1f公斤%n", "佳良", 20, 92.5);

        // 一次打印多个内容：用 + 拼接（字符串 + 任何类型都会自动转成字符串）
        System.out.println("1 + 1 = " + (1 + 1));      // 先算加法，再拼字符串 → 1 + 1 = 2
        System.out.println("1 + 1 = " + 1 + 1);        // 从左到右拼，变成 "1 + 1 = 11"（易错点！）

        // 转义字符：\n 换行，\t 制表符（对齐用），\" 输出双引号本身，\\ 输出反斜杠
        System.out.println("第一行\n第二行");
        System.out.println("姓名\t年龄");
        System.out.println("他说：\"我要学好 Java\"");

        // 从命令行接收参数：运行 java HelloWorld 佳良 20 时，args[0]="佳良", args[1]="20"
        if (args.length > 0) {
            System.out.println("你传入的第一个参数是：" + args[0]);
        }
    }
}
