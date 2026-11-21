/**
 * 知识点 11：final、static、抽象类与接口
 *
 * 这四个东西经常一起出现，也是最容易混淆的一组。
 *
 * final    —— "最终的"，不能改
 * static   —— "静态的"，属于类而不属于对象
 * abstract —— "抽象的"，只有声明没有实现
 * interface—— 接口，能力的声明
 */
public class FinalStaticAbstract {

    public static void main(String[] args) {

        // ---------- 一、final 的三种用法 ----------

        // 1. final 修饰变量：只能赋值一次
        final int MAX = 100;
        // MAX = 200;   // 取消注释会编译报错：不能给 final 变量重新赋值

        // 2. final 修饰方法：子类不能重写
        // 3. final 修饰类：不能被继承（比如 String、Integer 都是 final 类）

        // ---------- 二、static 的用法 ----------

        // 静态变量：用类名访问，所有对象共享一份
        Counter.total = 0;
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        c1.increment();
        c2.increment();
        System.out.println("共享计数：" + Counter.total);   // 2

        // 静态方法：不需要对象就能调用
        System.out.println("工具方法：" + MathUtil.max(3, 7));

        // 静态代码块：类第一次被加载时执行一次，常用于初始化静态数据
        System.out.println("数据库地址：" + DbConfig.url);

        // 静态内部类：不依赖外部类对象，可以直接 new
        Outer.Inner inner = new Outer.Inner();
        inner.hello();

        // ---------- 三、抽象类 vs 接口 ----------
        // 抽象类适合"有一半已经实现好的逻辑"
        // 接口适合"只声明能力，具体实现各不相同"

        AbstractLogger consoleLogger = new ConsoleLogger();
        consoleLogger.log("这是一条日志");   // 调用抽象类里的"已实现方法"，子类只补一小块

        Notifier email = new EmailNotifier();
        Notifier sms = new SmsNotifier();
        email.notify("系统异常");
        sms.notify("系统异常");

        // 接口的 default 方法：接口里也能写有实现的方法（Java 8+）
        email.hello();     // 调用接口的默认实现
    }
}

/**
 * 演示 static：计数器
 */
class Counter {

    // 静态变量：存在"类"上，所有对象看到的是同一个值
    static int total = 0;

    void increment() {
        total++;      // 每个对象调一次，共享的这个变量都 +1
    }
}

/**
 * 演示 static 方法：工具类
 * 工具类通常只放静态方法，不需要 new 对象，所以构造器设为 private 禁止 new
 */
class MathUtil {

    private MathUtil() {
    }   // 私有构造器：外部无法 new，只能调静态方法

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }
}

/**
 * 演示静态代码块：类加载时执行一次
 */
class DbConfig {

    static String url;

    // 静态代码块：JVM 第一次使用这个类时执行，且只执行一次
    static {
        url = "jdbc:mysql://localhost:3306/school";
        System.out.println("[静态代码块] 数据库配置已初始化");
    }
}

/**
 * 演示静态内部类
 */
class Outer {

    private String name = "外部类的字段";

    static class Inner {
        void hello() {
            System.out.println("我是静态内部类，可以直接 new，不需要外部类对象");
            // 注意：静态内部类不能直接访问外部类的非静态成员
        }
    }
}

/**
 * 抽象类：可以有已实现的方法 + 抽象方法
 */
abstract class AbstractLogger {

    // 抽象方法：只有声明，子类必须实现
    public abstract void write(String msg);

    // 已实现的方法：所有子类都能直接用（这是"模板方法"的雏形）
    public void log(String msg) {
        System.out.println("[开始记录]");
        write(msg);            // 调用子类实现的抽象方法
        System.out.println("[记录结束]");
    }
}

class ConsoleLogger extends AbstractLogger {

    @Override
    public void write(String msg) {
        System.out.println("控制台输出：" + msg);
    }
}

/**
 * 接口：只声明能力
 */
interface Notifier {

    // 抽象方法（public abstract 可以省略）
    void notify(String message);

    /**
     * default 方法：接口里也能有实现，实现类可以不重写它
     */
    default void hello() {
        System.out.println("[接口默认方法] 你好，我是通知器");
    }
}

class EmailNotifier implements Notifier {

    @Override
    public void notify(String message) {
        System.out.println("发送邮件：" + message);
    }
}

class SmsNotifier implements Notifier {

    @Override
    public void notify(String message) {
        System.out.println("发送短信：" + message);
    }
}
