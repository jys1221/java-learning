// 导入集合框架里的类。List、ArrayList、LinkedList 都在 java.util 包下
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 知识点 12：List —— 有序、可重复的集合
 *
 * 数组的缺点：长度固定，不能自动扩容，删除元素要手动挪位置。
 * List 解决了这些问题：长度可变，有大量现成方法。
 *
 * ArrayList：底层是数组，查询快（按下标 O(1)），中间插入删除慢
 * LinkedList：底层是双向链表，插入删除快，查询慢
 * 日常 90% 场景用 ArrayList 就够了。
 */
public class ListDemo {

    public static void main(String[] args) {

        // ---------- 一、ArrayList 基本操作 ----------

        // 泛型 <String> 表示"这个列表只能装字符串"，编译期就会检查类型
        List<String> names = new ArrayList<>();

        // add：追加到末尾
        names.add("佳良");
        names.add("小明");
        names.add("小红");
        System.out.println("初始列表：" + names);

        // size：元素个数。注意不是 length()，那是数组/字符串
        System.out.println("元素个数：" + names.size());

        // get：按下标取值（下标从 0 开始，越界会抛 IndexOutOfBoundsException）
        System.out.println("第 0 个：" + names.get(0));

        // set：修改指定下标的值
        names.set(1, "大明");
        System.out.println("修改后：" + names);

        // add(index, value)：插入到指定位置，后面的元素自动往后挪
        names.add(1, "插队的");
        System.out.println("插入后：" + names);

        // remove：两种重载要分清！
        names.remove(0);              // 传 int → 按"下标"删
        names.remove("大明");          // 传对象 → 按"内容"删（只删第一个匹配的）
        System.out.println("删除后：" + names);

        // contains：判断是否包含某元素（内部用 equals 比较内容）
        System.out.println("包含小红吗：" + names.contains("小红"));

        // indexOf：找第一次出现的下标，找不到返回 -1
        System.out.println("小红的下标：" + names.indexOf("小红"));

        // isEmpty / clear
        System.out.println("是空的吗：" + names.isEmpty());
        // names.clear();     // 清空所有元素

        // ---------- 二、遍历的三种方式 ----------

        List<String> fruits = new ArrayList<>(List.of("苹果", "香蕉", "橙子"));   // List.of 创建不可变列表，ArrayList 包一层变成可变

        // 方式 1：普通 for + 下标（需要下标时用）
        System.out.println("--- 方式1：下标遍历 ---");
        for (int i = 0; i < fruits.size(); i++) {
            System.out.println(i + " -> " + fruits.get(i));
        }

        // 方式 2：增强 for（最常用，只读时首选）
        System.out.println("--- 方式2：增强 for ---");
        for (String f : fruits) {
            System.out.println(f);
        }

        // 方式 3：forEach + Lambda（Java 8+）
        System.out.println("--- 方式3：forEach ---");
        fruits.forEach(f -> System.out.println("水果：" + f));

        // ---------- 三、ArrayList vs LinkedList ----------

        // ArrayList：下标访问快
        List<Integer> arrayList = new ArrayList<>();
        // LinkedList：增删快（头尾操作尤其快）
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 1; i <= 5; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        System.out.println("ArrayList：" + arrayList);
        System.out.println("LinkedList：" + linkedList);

        // 两者 API 完全一样（因为都实现了 List 接口），区别只在底层结构和性能
        // 小数据量时感受不到差别，不要过早优化

        // ---------- 四、List.of 创建不可变列表 ----------
        List<String> fixed = List.of("A", "B", "C");
        System.out.println("不可变列表：" + fixed);
        // fixed.add("D");   // 运行时抛 UnsupportedOperationException，因为 List.of 返回的是只读列表
    }
}
