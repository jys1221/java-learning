// Map 相关类都在 java.util 包
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 知识点 13：Map —— 键值对集合（key → value）
 *
 * 特点：key 唯一，value 可以重复。用 key 查 value 非常快。
 * 生活类比：字典，用"字"（key）查"解释"（value）。
 *
 * HashMap：最常用，无序，查询最快（O(1) 平均）
 * TreeMap：按 key 自动排序（要求 key 可比较）
 * LinkedHashMap：保持插入顺序
 */
public class MapDemo {

    public static void main(String[] args) {

        // ---------- 一、HashMap 基本操作 ----------

        // <String, Integer>：key 是字符串，value 是整数
        Map<String, Integer> scores = new HashMap<>();

        // put：存一对键值。key 已存在时会覆盖旧值，并返回旧值
        scores.put("佳良", 85);
        scores.put("小明", 92);
        scores.put("小红", 78);
        System.out.println("初始：" + scores);

        Integer old = scores.put("佳良", 90);     // 覆盖
        System.out.println("被覆盖的旧值：" + old);   // 85

        // get：按 key 取值，key 不存在返回 null
        System.out.println("小明的分数：" + scores.get("小明"));
        System.out.println("不存在的 key：" + scores.get("路人甲"));   // null

        // getOrDefault：key 不存在时返回指定的默认值（统计场景很常用）
        System.out.println("路人甲的分数：" + scores.getOrDefault("路人甲", 0));

        // containsKey / containsValue
        System.out.println("有佳良吗：" + scores.containsKey("佳良"));
        System.out.println("有 92 分吗：" + scores.containsValue(92));

        // remove：按 key 删除，返回被删掉的 value
        Integer removed = scores.remove("小红");
        System.out.println("删除小红，她的分数是：" + removed);

        // size / isEmpty
        System.out.println("一共几个人：" + scores.size());

        // ---------- 二、遍历 Map 的三种方式 ----------

        Map<String, String> cityMap = new HashMap<>();
        cityMap.put("合肥", "安徽");
        cityMap.put("南京", "江苏");
        cityMap.put("杭州", "浙江");

        // 方式 1：keySet 遍历所有 key，再 get 取 value（不推荐，两次查找）
        System.out.println("--- keySet ---");
        for (String city : cityMap.keySet()) {
            System.out.println(city + " 属于 " + cityMap.get(city));
        }

        // 方式 2：entrySet 遍历键值对（最常用、效率最高）
        System.out.println("--- entrySet ---");
        for (Map.Entry<String, String> entry : cityMap.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // 方式 3：forEach + Lambda（Java 8+）
        System.out.println("--- forEach ---");
        cityMap.forEach((k, v) -> System.out.println(k + " 是 " + v + " 的城市"));

        // ---------- 三、TreeMap：按 key 自动排序 ----------

        Map<Integer, String> ranking = new TreeMap<>();
        ranking.put(3, "第三名");
        ranking.put(1, "第一名");
        ranking.put(2, "第二名");

        // 输出顺序会按 key 从小到大自动排列，而不是插入顺序
        System.out.println("TreeMap 顺序：" + ranking);

        // ---------- 四、实战：统计每个单词出现的次数 ----------

        String text = "java spring spring mybatis java java";
        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : text.split(" ")) {
            // 经典写法：getOrDefault 拿到当前次数（没有就是 0），+1 再放回去
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            // 也可以用 merge：wordCount.merge(word, 1, Integer::sum);
        }
        System.out.println("词频统计：" + wordCount);

        // ---------- 五、必须知道的一个坑 ----------
        // HashMap 的 key 如果用自定义对象，必须正确重写 hashCode 和 equals，
        // 否则两个"内容相同"的对象会被当成不同的 key。
        // 用 String / Integer 这些 JDK 自带的类不用担心，它们已经重写好了。
    }
}
