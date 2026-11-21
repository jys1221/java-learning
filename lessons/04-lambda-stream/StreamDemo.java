import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 知识点 16：Stream 流式处理
 *
 * Stream 不是集合，而是"一条流水线"：
 *   数据源 → 一系列中间操作 → 终止操作 → 结果
 *
 * 特点：
 *   1. 不修改原数据（每次操作返回新流）
 *   2. 惰性求值：中间操作不会立即执行，只有遇到终止操作才一起算
 *   3. 只能"消费"一次，用完就废
 *
 * 常用中间操作（返回流）：filter、map、sorted、distinct、limit、skip
 * 常用终止操作（返回结果）：collect、forEach、count、reduce、anyMatch、findFirst
 */
public class StreamDemo {

    public static void main(String[] args) {

        // 准备一批学生数据
        List<Student> students = new ArrayList<>(Arrays.asList(
                new Student("佳良", 20, 85, "计算机"),
                new Student("小明", 19, 92, "计算机"),
                new Student("小红", 21, 78, "英语"),
                new Student("小刚", 20, 95, "数学"),
                new Student("小丽", 19, 60, "英语")
        ));

        // ---------- 一、filter：过滤 ----------
        // 只留分数 >= 80 的
        List<Student> good = students.stream()
                .filter(s -> s.getScore() >= 80)
                .collect(Collectors.toList());
        System.out.println("80 分以上：" + good.size() + " 人");

        // ---------- 二、map：转换 ----------
        // 把"学生对象"转成"姓名字符串"
        List<String> names = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("所有姓名：" + names);

        // 把分数放大 10 倍（示例转换）
        List<Integer> scaled = students.stream()
                .map(s -> s.getScore() * 10)
                .collect(Collectors.toList());
        System.out.println("分数×10：" + scaled);

        // ---------- 三、sorted：排序 ----------
        // 按分数从高到低（降序要自己写比较器）
        List<Student> ranked = students.stream()
                .sorted((a, b) -> b.getScore() - a.getScore())
                .collect(Collectors.toList());
        System.out.println("第一名：" + ranked.get(0).getName() + " " + ranked.get(0).getScore());

        // 按年龄升序（Comparator.comparing 更简洁）
        List<Student> byAge = students.stream()
                .sorted(java.util.Comparator.comparingInt(Student::getAge))
                .collect(Collectors.toList());
        System.out.println("年龄最小：" + byAge.get(0).getName());

        // ---------- 四、distinct：去重 ----------
        List<String> majors = students.stream()
                .map(Student::getMajor)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("所有专业（去重）：" + majors);

        // ---------- 五、统计类操作 ----------

        // count：计数
        long count = students.stream().filter(s -> s.getScore() >= 80).count();
        System.out.println("80 分以上人数：" + count);

        // anyMatch / allMatch / noneMatch：判断
        boolean anyFail = students.stream().anyMatch(s -> s.getScore() < 60);
        boolean allPass = students.stream().allMatch(s -> s.getScore() >= 60);
        System.out.println("有人不及格吗：" + anyFail + "，全部及格吗：" + allPass);

        // findFirst：找第一个（返回 Optional，可能找不到）
        Optional<Student> first = students.stream()
                .filter(s -> s.getScore() >= 90)
                .findFirst();
        first.ifPresent(s -> System.out.println("第一个 90+ 的学生：" + s.getName()));

        // ---------- 六、reduce：把一堆值归约成一个 ----------
        // 求总分：初始值 0，每次把当前分数累加进去
        int totalScore = students.stream()
                .map(Student::getScore)
                .reduce(0, Integer::sum);
        System.out.println("总分：" + totalScore);

        // 求最高分（没有初始值，返回 Optional）
        Optional<Integer> maxScore = students.stream()
                .map(Student::getScore)
                .reduce(Integer::max);
        System.out.println("最高分：" + maxScore.orElse(0));

        // ---------- 七、collect：收集成各种结构 ----------

        // 收集成 List
        List<String> nameList = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        // 收集成 Map（key 是姓名，value 是分数）
        // toMap 要求 key 不能重复，重复会抛 IllegalStateException
        Map<String, Integer> scoreMap = students.stream()
                .collect(Collectors.toMap(Student::getName, Student::getScore));
        System.out.println("姓名→分数：" + scoreMap);

        // 分组：按专业分组，得到 Map<专业, 该专业的学生列表>
        Map<String, List<Student>> byMajor = students.stream()
                .collect(Collectors.groupingBy(Student::getMajor));
        byMajor.forEach((major, list) -> System.out.println(major + " 专业有 " + list.size() + " 人"));

        // 分组后统计平均分
        Map<String, Double> avgByMajor = students.stream()
                .collect(Collectors.groupingBy(Student::getMajor, Collectors.averagingInt(Student::getScore)));
        System.out.println("各专业平均分：" + avgByMajor);

        // 拼接成字符串
        String joined = students.stream()
                .map(Student::getName)
                .collect(Collectors.joining("、", "学生名单：", "。"));
        System.out.println(joined);

        // ---------- 八、一个完整的小需求 ----------
        // "找出计算机专业分数最高的两个人，输出他们的名字和分数"
        students.stream()
                .filter(s -> s.getMajor().equals("计算机"))
                .sorted((a, b) -> b.getScore() - a.getScore())
                .limit(2)
                .forEach(s -> System.out.println("[TOP] " + s.getName() + "：" + s.getScore()));
    }
}

/**
 * 学生类：配合 Stream 演示
 */
class Student {

    private String name;
    private int age;
    private int score;
    private String major;

    public Student(String name, int age, int score, String major) {
        this.name = name;
        this.age = age;
        this.score = score;
        this.major = major;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public int getScore() { return score; }
    public String getMajor() { return major; }

    @Override
    public String toString() {
        return name + "(" + score + ")";
    }
}
