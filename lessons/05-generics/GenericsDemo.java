import java.util.ArrayList;
import java.util.List;

/**
 * 知识点 17：泛型（Generics）
 *
 * 泛型 = "类型参数化"，让一个类/方法可以适配多种类型，同时保证类型安全。
 *
 * 没有泛型时，集合里什么都装，取出来全是 Object，要手动强转，还容易转错崩溃。
 * 有泛型后：<String> 就只能装 String，编译期就检查，取出来不用强转。
 *
 * 语法位置：
 *   泛型类：class Box<T> { }
 *   泛型方法：public <T> T method(T arg)
 *   通配符：<?>、<? extends Number>、<? super Integer>
 */
public class GenericsDemo {

    public static void main(String[] args) {

        // ---------- 一、泛型类：一个"盒子"可以装任意类型 ----------

        // 装字符串
        Box<String> stringBox = new Box<>();
        stringBox.set("佳良");
        String s = stringBox.get();     // 不用强转，编译器已经知道是 String
        System.out.println("字符串盒子：" + s);

        // 装整数
        Box<Integer> intBox = new Box<>(123);
        Integer i = intBox.get();
        System.out.println("整数盒子：" + i);

        // 类型安全：下面这行编译直接报错，无法把 String 放进 Integer 盒子
        // intBox.set("类型不对");

        // ---------- 二、泛型方法 ----------

        // 静态泛型方法：<T> 写在返回值前面，表示"这个方法自己定义了一个类型参数"
        String[] arr = {"a", "b", "c"};
        String first = firstElement(arr);
        System.out.println("第一个元素：" + first);

        Integer[] nums = {10, 20, 30};
        Integer firstNum = firstElement(nums);
        System.out.println("第一个数字：" + firstNum);

        // 泛型方法 + 泛型类组合
        Pair<String, Integer> pair = Pair.of("年龄", 20);
        System.out.println(pair.getKey() + " = " + pair.getValue());

        // ---------- 三、通配符 ----------

        // List<?>：可以接收任何类型的 List，但只能读不能写（因为不知道具体类型）
        List<String> strList = new ArrayList<>(List.of("A", "B"));
        List<Integer> intList = new ArrayList<>(List.of(1, 2, 3));
        printAll(strList);
        printAll(intList);

        // <? extends Number>：上界通配符，只能接收 Number 及其子类（Integer、Double…）
        // 适合"只读"场景
        List<Integer> integers = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.5, 2.5);
        System.out.println("整数求和：" + sumAll(integers));
        System.out.println("小数求和：" + sumAll(doubles));

        // <? super Integer>：下界通配符，只能接收 Integer 及其父类
        // 适合"只写"场景（往集合里添加元素）

        // ---------- 四、泛型的边界 ----------
        // 泛型不能是基本类型！List<int> 是错的，必须写 List<Integer>
        // 原因：泛型在编译后会"类型擦除"，最终都变成 Object，基本类型装不进 Object

        // ---------- 五、类型擦除：一个必须知道的概念 ----------
        // Java 的泛型只在编译期有效，运行期被"擦除"成原始类型（Object）
        // 所以：
        //   - 不能 new T()
        //   - 不能 new T[10]
        //   - 不能用 instanceof T
        //   - 静态成员不能用类的泛型参数 T
    }

    /**
     * 泛型方法：求数组第一个元素
     * 语法：在返回值类型前加 <T>
     */
    public static <T> T firstElement(T[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        return arr[0];
    }

    /**
     * 通配符方法：能接收任意类型的 List，只读
     */
    public static void printAll(List<?> list) {
        for (Object item : list) {
            System.out.println("元素：" + item);
        }
    }

    /**
     * 上界通配符：只接收 Number 家族（Integer、Double、Long…）
     * 好处：可以对元素调用 Number 的方法（doubleValue 等）
     */
    public static double sumAll(List<? extends Number> list) {
        double total = 0;
        for (Number n : list) {
            total += n.doubleValue();   // Number 有 doubleValue 方法，可以安全调用
        }
        return total;
    }
}

/**
 * 泛型类：一个能装任意类型值的盒子
 * T 是"类型参数"，可以叫 T、E、K、V，只是约定：
 *   T = Type，E = Element，K = Key，V = Value
 */
class Box<T> {

    private T value;

    public Box() {
    }

    public Box(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

/**
 * 泛型类：存一对键值，两个类型都可以自定义
 */
class Pair<K, V> {

    private K key;
    private V value;

    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 静态泛型工厂方法：因为上面构造器是 private，外部通过这个方法创建
     * 注意：静态方法不能使用类的泛型参数 K、V，所以要自己声明 <K, V>
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}
