/**
 * 知识点 5：类与对象
 *
 * 类（class）    = 一张"设计图纸"，描述一类事物有哪些属性、能做什么
 * 对象（object） = 按图纸造出来的"实物"，用 new 关键字创建
 *
 * 类比：类 = 汽车的设计图纸；对象 = 按图纸生产出来的每一辆车
 */
public class ClassAndObject {

    public static void main(String[] args) {

        // new 一个对象：在堆内存中分配空间，调用构造器初始化，返回地址给变量
        Student s1 = new Student();
        s1.name = "佳良";
        s1.age = 20;
        s1.score = 85.5;

        // 再 new 一个：每个对象有自己独立的一份成员变量，互不影响
        Student s2 = new Student();
        s2.name = "小明";
        s2.age = 19;
        s2.score = 92.0;

        // 调用对象的方法（行为）
        s1.introduce();
        s2.introduce();
        s1.study("Java");

        // 判断两个变量是不是同一个对象：== 比较的是地址
        Student s3 = s1;                 // s3 和 s1 指向同一个对象
        System.out.println("s1 == s3 ? " + (s1 == s3));   // true
        System.out.println("s1 == s2 ? " + (s1 == s2));   // false

        // 修改 s3 会影响 s1，因为根本就是同一个对象
        s3.name = "佳良（改名）";
        System.out.println("s1 的名字也跟着变了：" + s1.name);

        // 访问静态成员：静态成员属于"类"本身，不属于某个对象，用类名调用
        System.out.println("全校人数：" + Student.totalCount);
        System.out.println("学校名称：" + Student.schoolName);

        // 对象为 null 时调用方法会抛 NullPointerException（空指针异常）
        Student nobody = null;
        // nobody.introduce();   // 取消注释会运行时崩溃：NullPointerException
        if (nobody != null) {
            nobody.introduce();
        } else {
            System.out.println("nobody 是 null，不能调用方法");
        }
    }
}

/**
 * 学生类：一个"模板"
 * 注意：这个类是包级私有的（没有 public），所以只能和主类写在同一个文件里
 */
class Student {

    // ---------- 成员变量（也叫字段、属性）：每个对象各有一份 ----------
    String name;      // 姓名
    int age;          // 年龄
    double score;     // 分数

    // ---------- 静态变量：属于类本身，所有对象共享同一份 ----------
    static int totalCount = 0;          // 统计一共创建了多少个学生
    static String schoolName = "某某大学";

    // ---------- 构造器：new 的时候自动执行，用来做初始化 ----------
    // 类里没写构造器时，Java 会自动送一个"无参构造器"；一旦自己写了，送的就没
    public Student() {
        totalCount++;   // 每 new 一个对象，计数 +1
    }

    // ---------- 成员方法：对象能做的事 ----------
    void introduce() {
        // this 代表"当前这个对象"，可以省略，写出来更清晰
        System.out.println("我叫 " + this.name + "，今年 " + this.age + " 岁，分数 " + this.score);
    }

    void study(String subject) {
        System.out.println(this.name + " 正在学习 " + subject);
    }
}
