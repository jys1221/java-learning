/**
 * 知识点 18：异常处理
 *
 * 异常 = 程序运行时出现的不正常情况（除零、空指针、数组越界、文件不存在……）。
 *
 * 继承关系：
 *   Throwable
 *     ├── Error（严重错误，程序基本救不回来，比如内存溢出，一般不捕获）
 *     └── Exception
 *           ├── RuntimeException（运行时异常，不强制处理：空指针、越界、除零）
 *           └── 受检异常（编译期强制要求处理：IOException、SQLException、InterruptedException）
 *
 * 五个关键字：
 *   try      —— 包住可能出事的代码
 *   catch    —— 出事了怎么办
 *   finally  —— 不管出没出事都要执行的收尾代码
 *   throw    —— 手动抛出一个异常
 *   throws   —— 在方法签名上声明"我这个方法可能抛出什么异常"
 */
public class ExceptionDemo {

    public static void main(String[] args) {

        // ---------- 一、捕获异常 ----------
        System.out.println("--- 场景1：除零 ---");
        try {
            int result = 10 / 0;          // 会抛出 ArithmeticException
            System.out.println("这行不会执行：" + result);
        } catch (ArithmeticException e) {
            // e 是异常对象，里面有异常信息
            System.out.println("捕获到：" + e.getMessage());
        } finally {
            // 无论是否异常都会执行，通常用来释放资源
            System.out.println("finally 一定会执行");
        }

        // ---------- 二、多重 catch ----------
        System.out.println("--- 场景2：数组越界 ---");
        int[] arr = {1, 2, 3};
        try {
            System.out.println(arr[5]);   // 下标 5 越界 → ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界了：" + e.getMessage());
        } catch (RuntimeException e) {
            // 注意：catch 从上往下匹配，范围大的要放后面，否则前面永远匹配不到
            System.out.println("其他运行时异常：" + e.getMessage());
        }

        // ---------- 三、Java 7+ 的多异常合并写法 ----------
        try {
            int a = 10 / 0;
            int[] b = new int[0];
            System.out.println(b[0]);
        } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
            System.out.println("两种异常统一处理：" + e.getClass().getSimpleName());
        }

        // ---------- 四、throw：手动抛异常 ----------
        System.out.println("--- 场景3：参数校验 ---");
        try {
            checkAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("校验失败：" + e.getMessage());
        }

        // ---------- 五、throws：声明方法可能抛异常 ----------
        // readFile 声明了 throws IOException，调用处必须 try-catch 或者继续往上抛
        try {
            readFile("不存在的文件.txt");
        } catch (java.io.IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }

        // ---------- 六、自定义异常 ----------
        System.out.println("--- 场景4：自定义业务异常 ---");
        try {
            withdraw(500);
        } catch (BalanceNotEnoughException e) {
            System.out.println("业务异常：" + e.getMessage() + "，还差 " + e.getShortage() + " 元");
        }

        // ---------- 七、try-with-resources：自动关闭资源 ----------
        // 传统写法要在 finally 里手动 close，麻烦且容易漏
        // Java 7+ 的写法：把资源写在 try(...) 的括号里，会自动调用 close()
        System.out.println("--- 场景5：自动关闭资源 ---");
        try (java.io.StringWriter writer = new java.io.StringWriter()) {
            writer.write("用 try-with-resources 写的内容");
            System.out.println(writer.toString());
        } catch (java.io.IOException e) {
            System.out.println("写失败：" + e.getMessage());
        }

        // ---------- 八、异常处理的最佳实践 ----------
        // 1. 不要空 catch：至少打印日志，否则出问题根本查不到
        // 2. 不要用异常做流程控制（性能差、可读性差）
        // 3. 捕获后要么处理、要么往上抛，不要吞掉
        // 4. 自定义异常要继承 RuntimeException 或 Exception
    }

    /**
     * 手动抛异常：throw new 异常类型(消息)
     * IllegalArgumentException 表示"参数不合法"
     */
    public static void checkAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("年龄不合法：" + age);
        }
        System.out.println("年龄合法：" + age);
    }

    /**
     * throws 声明：告诉调用者"我这里可能抛 IOException，你自己处理"
     * IOException 是"受检异常"，不声明就编译不过
     */
    public static void readFile(String path) throws java.io.IOException {
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            throw new java.io.IOException("文件不存在：" + path);
        }
    }

    /**
     * 演示抛自定义异常
     */
    public static void withdraw(double amount) {
        double balance = 100;
        if (amount > balance) {
            // 抛业务异常，并把"差多少钱"作为附加信息带出去
            throw new BalanceNotEnoughException("余额不足", amount - balance);
        }
        System.out.println("取款成功");
    }
}

/**
 * 自定义异常：余额不足
 * 继承 RuntimeException（运行时异常，调用处不强制 catch）
 * 如果要强制调用者处理，就继承 Exception
 */
class BalanceNotEnoughException extends RuntimeException {

    private final double shortage;    // 差额

    public BalanceNotEnoughException(String message, double shortage) {
        super(message);               // 调父类构造器，把消息存进去
        this.shortage = shortage;
    }

    public double getShortage() {
        return shortage;
    }
}
