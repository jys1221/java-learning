import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 知识点 15：Lambda 表达式
 *
 * Lambda = "一段可以传递的代码"，本质是"函数式接口"的简写。
 *
 * 什么是函数式接口？
 *   只有一个抽象方法的接口。比如 Runnable、Comparator、Consumer、Function……
 *
 * 语法： (参数) -> { 方法体 }
 *   参数类型可以省略（编译器能推断）
 *   只有一个参数时括号可以省略
 *   只有一行代码时大括号和 return 也可以省略
 *
 * 为什么需要 Lambda？
 *   以前为了"传一小段逻辑"，要写一整个匿名内部类，又臭又长。
 */
public class LambdaDemo {

    public static void main(String[] args) {

        // ---------- 一、最经典的例子：Runnable ----------

        // 老写法：匿名内部类
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("老写法：跑起来了");
            }
        };
        oldWay.run();

        // Lambda 写法：同样的事情，一行搞定
        Runnable newWay = () -> System.out.println("Lambda：跑起来了");
        newWay.run();

        // ---------- 二、带参数的 Lambda ----------

        // Comparator：比较器。参数是 (a, b)，返回负数表示 a 排前面
        // 这里演示"按字符串长度排序"
        java.util.Comparator<String> byLength = (a, b) -> a.length() - b.length();

        java.util.List<String> words = new java.util.ArrayList<>(java.util.List.of("Java", "Spring", "A", "MyBatis"));
        words.sort(byLength);
        System.out.println("按长度排序：" + words);

        // 写全一点（带类型、带大括号）的等价写法：
        java.util.Comparator<String> same = (String x, String y) -> {
            return x.length() - y.length();
        };

        // ---------- 三、四个最重要的内置函数式接口 ----------

        // 1. Predicate<T>：接一个 T，返回 boolean。用于"判断/过滤"
        Predicate<Integer> isAdult = age -> age >= 18;
        System.out.println("20 是成年吗：" + isAdult.test(20));
        System.out.println("10 是成年吗：" + isAdult.test(10));

        // Predicate 还能组合：and / or / negate
        Predicate<Integer> isOld = age -> age >= 60;
        Predicate<Integer> adultAndOld = isAdult.and(isOld);
        System.out.println("70 既成年又老年：" + adultAndOld.test(70));

        // 2. Consumer<T>：接一个 T，不返回值。用于"消费/打印/保存"
        Consumer<String> printer = msg -> System.out.println("[Consumer] " + msg);
        printer.accept("你好");

        // 3. Function<T, R>：接 T，返回 R。用于"转换"
        Function<String, Integer> lengthOf = s -> s.length();
        System.out.println("'Java' 长度：" + lengthOf.apply("Java"));

        // 4. Supplier<T>：不接参数，返回一个 T。用于"提供/生成"
        Supplier<String> greeting = () -> "早上好";
        System.out.println("Supplier 提供：" + greeting.get());

        // ---------- 四、方法引用：Lambda 的进一步简写 ----------

        // 当 Lambda 的方法体"只是调用一个现成的方法"时，可以简化成 类名::方法名
        // 规则：参数怎么传进去，就怎么传给那个方法

        // s -> s.length()  等价于  String::length
        Function<String, Integer> f1 = String::length;
        System.out.println("方法引用长度：" + f1.apply("Lambda"));

        // s -> System.out.println(s)  等价于  System.out::println
        Consumer<String> c1 = System.out::println;
        c1.accept("方法引用打印");

        // (a, b) -> a + b  等价于  Integer::sum（静态方法引用）
        BiFunction<Integer, Integer, Integer> add = Integer::sum;
        System.out.println("方法引用求和：" + add.apply(3, 5));

        // ---------- 五、实战：用 Lambda 替换一堆 if-else ----------

        // 场景：根据操作类型执行不同逻辑。传统写法是一长串 if-else 或 switch
        // 用"函数式接口 + Map"可以更灵活
        java.util.Map<String, Function<Integer, Integer>> ops = new java.util.HashMap<>();
        ops.put("double", x -> x * 2);
        ops.put("square", x -> x * x);
        ops.put("negate", x -> -x);

        System.out.println("double(5) = " + ops.get("double").apply(5));
        System.out.println("square(5) = " + ops.get("square").apply(5));
        System.out.println("negate(5) = " + ops.get("negate").apply(5));

        // 注意：Lambda 里引用的外部局部变量必须"事实上的 final"（不能再被改），
        // 否则编译报错：Variable used in lambda expression should be final or effectively final
    }
}
