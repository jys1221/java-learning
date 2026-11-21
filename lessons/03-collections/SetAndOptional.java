import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * 知识点 14：Set 去重 + Optional 防空指针
 *
 * Set 特点：元素不重复（自动去重），无序（TreeSet/LinkedHashSet 例外）。
 *
 * HashSet：最快，无序
 * TreeSet：按大小自动排序
 * LinkedHashSet：保持插入顺序
 *
 * Optional：Java 8 引入的"容器"，用来优雅地表示"可能没有值"，
 * 减少 NullPointerException。
 */
public class SetAndOptional {

    public static void main(String[] args) {

        // ---------- 一、HashSet：自动去重 ----------

        Set<String> tags = new HashSet<>();

        // add 返回值很有用：成功加入返回 true，已存在返回 false（不重复加）
        System.out.println("加 Java：" + tags.add("Java"));      // true
        System.out.println("加 Spring：" + tags.add("Spring"));  // true
        System.out.println("再加 Java：" + tags.add("Java"));    // false，已存在
        System.out.println("集合内容：" + tags);

        System.out.println("size：" + tags.size());              // 2，不是 3
        System.out.println("包含 Java：" + tags.contains("Java"));

        // 实战：用 Set 对数组去重
        int[] nums = {1, 2, 2, 3, 3, 3, 4};
        Set<Integer> unique = new HashSet<>();
        for (int n : nums) {
            unique.add(n);
        }
        System.out.println("去重后：" + unique);

        // ---------- 二、TreeSet：自动排序 ----------

        Set<Integer> sorted = new TreeSet<>();
        sorted.add(5);
        sorted.add(1);
        sorted.add(3);
        sorted.add(1);      // 重复，加不进去
        // 输出会按 1, 3, 5 排序
        System.out.println("TreeSet 排序：" + sorted);

        // ---------- 三、LinkedHashSet：保持插入顺序 ----------

        Set<String> ordered = new LinkedHashSet<>();
        ordered.add("第三");
        ordered.add("第一");
        ordered.add("第二");
        // 输出顺序和插入顺序一致，不会像 HashSet 那样乱序
        System.out.println("LinkedHashSet 顺序：" + ordered);

        // ---------- 四、Optional：优雅处理"可能为 null" ----------
        // 没有 Optional 时，我们只能这样写：
        //   if (name != null) { name.length(); }
        // 有了 Optional，可以链式调用，忘记判空也不会直接崩

        // ofNullable：值可能为 null 时用这个（of 用于确定不为 null）
        Optional<String> opt1 = Optional.ofNullable("佳良");
        Optional<String> opt2 = Optional.ofNullable(null);

        // isPresent：有没有值
        System.out.println("opt1 有值吗：" + opt1.isPresent());   // true
        System.out.println("opt2 有值吗：" + opt2.isPresent());   // false

        // orElse：有值就返回，没有就返回兜底值
        System.out.println("opt1 的值：" + opt1.orElse("默认名字"));
        System.out.println("opt2 的值：" + opt2.orElse("默认名字"));

        // map：有值时才执行转换，没值就跳过，不会抛空指针
        Optional<Integer> len = opt1.map(String::length);
        System.out.println("名字长度：" + len.orElse(0));

        // ifPresent：有值才执行代码
        opt1.ifPresent(n -> System.out.println("你好，" + n));

        // ---------- 五、实战：从 Map 里取值避免空指针 ----------

        // 假设从数据库查出来一个可能为 null 的分数
        Integer score = null;

        // 传统写法（容易忘判空）
        int safeScore1 = (score == null) ? 0 : score;

        // Optional 写法
        int safeScore2 = Optional.ofNullable(score).orElse(0);

        System.out.println("传统：" + safeScore1 + "，Optional：" + safeScore2);

        // 记住：Optional 主要用于"方法返回值"，不要用它当字段类型或方法参数
    }
}
