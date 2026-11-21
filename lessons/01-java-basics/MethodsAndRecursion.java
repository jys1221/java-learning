/**
 * 知识点 4：方法与递归
 *
 * 方法 = 把一段代码打包起来，起个名字，以后多次调用。
 * 好处：代码复用、逻辑清晰、方便修改。
 *
 * 方法定义格式：
 *   修饰符 返回值类型 方法名(参数列表) { 方法体 }
 */
public class MethodsAndRecursion {

    public static void main(String[] args) {

        // 调用带返回值的方法：把结果接住
        int sum = add(3, 5);
        System.out.println("3 + 5 = " + sum);

        // 调用 void 方法：只做事，不返回结果
        sayHello("佳良");

        // 调用"参数个数不同"的重载版本
        System.out.println("三数之和：" + add(1, 2, 3));

        // 调用可变参数方法：参数个数不固定
        System.out.println("可变参数求和：" + sumAll(1, 2, 3, 4, 5));

        // 递归：方法自己调用自己
        System.out.println("5 的阶乘 = " + factorial(5));
        System.out.println("第 10 个斐波那契数 = " + fib(10));

        // 值传递演示：Java 只有值传递，传基本类型时方法内改了也不影响外面
        int x = 10;
        changeValue(x);
        System.out.println("调用后外面的 x 还是：" + x);   // 10，没变

        // 传数组（引用）时，方法内改的是同一个数组对象
        int[] arr = {1, 2, 3};
        changeArray(arr);
        System.out.println("数组第一个元素变成：" + arr[0]);   // 100，变了
    }

    /**
     * 带返回值的方法：两个 int 相加，返回 int
     * 参数：a、b 是形参（占位），调用时传实参
     */
    public static int add(int a, int b) {
        return a + b;      // return 把结果交回给调用者，同时结束方法
    }

    /**
     * 方法重载（Overload）：方法名相同，但参数列表不同
     * 编译器会根据你传入的参数个数/类型，自动选择对应的方法
     */
    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    /**
     * 可变参数：类型... 表示参数个数不限，内部当成数组用
     */
    public static int sumAll(int... nums) {
        int total = 0;
        for (int n : nums) {
            total += n;
        }
        return total;
    }

    /**
     * void 方法：没有返回值，只是执行动作
     */
    public static void sayHello(String name) {
        System.out.println("你好，" + name + "！");
    }

    /**
     * 递归：求 n 的阶乘 n! = n * (n-1)!
     *
     * 递归必须有两个部分：
     *   1. 终止条件（基线条件）：n <= 1 时直接返回 1，否则会无限调用下去直到栈溢出
     *   2. 递归调用：把问题规模缩小一点，再交给自己处理
     */
    public static long factorial(int n) {
        if (n <= 1) {
            return 1;              // 终止条件
        }
        return n * factorial(n - 1);   // 递归：n 乘以 (n-1) 的阶乘
    }

    /**
     * 递归：斐波那契数列 1, 1, 2, 3, 5, 8, 13...
     * 规律：f(n) = f(n-1) + f(n-2)
     * 注意：这种写法有大量重复计算，n 大了会很慢（后面学"动态规划"可以优化）
     */
    public static int fib(int n) {
        if (n <= 2) {
            return 1;
        }
        return fib(n - 1) + fib(n - 2);
    }

    /**
     * 基本类型是"值传递"：方法拿到的是副本，改副本不影响外面
     */
    public static void changeValue(int num) {
        num = 999;     // 只改了方法内部的副本
        System.out.println("方法内部的 num：" + num);
    }

    /**
     * 数组是引用类型：方法拿到的是同一个数组的地址，改元素会影响外面
     */
    public static void changeArray(int[] arr) {
        arr[0] = 100;   // 直接改原数组的第 0 个元素
    }
}
