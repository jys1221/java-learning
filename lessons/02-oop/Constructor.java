/**
 * 知识点 6：构造器（Constructor）
 *
 * 构造器的作用：对象创建时（new 的那一刻）自动执行，用来给成员变量赋初始值。
 *
 * 三条规则必须记住：
 *   1. 构造器名必须和类名完全一样（连大小写都要一致）
 *   2. 构造器没有返回值类型（连 void 都不能写）
 *   3. 你不写构造器，Java 送一个无参的；你写了一个有参的，无参的就不再送了
 */
public class Constructor {

    public static void main(String[] args) {

        // 用无参构造器创建，属性都是默认值（String 默认 null，int 默认 0）
        Person p1 = new Person();
        p1.show();     // 姓名=null，年龄=0

        // 用"两个参数"的构造器
        Person p2 = new Person("佳良", 20);
        p2.show();

        // 用"三个参数"的构造器（构造器重载）
        Person p3 = new Person("小明", 19, "合肥");
        p3.show();

        // 构造器里调用构造器（this(...)）的例子
        Person p4 = new Person("小红");
        p4.show();     // 年龄和城市会使用默认值

        // 演示：对象一旦创建，构造器就执行完了，不会再次执行
        p2.setAge(21);
        p2.show();
    }
}

class Person {

    String name;      // 姓名
    int age;          // 年龄
    String city;      // 城市

    /**
     * 无参构造器
     * 一旦手写了任何构造器，编译器就不再自动生成无参构造器，
     * 所以很多框架（Spring、MyBatis）要求类必须有"无参构造器"，通常要手动保留一个。
     */
    public Person() {
        System.out.println("[构造器] 无参构造器被调用");
    }

    /**
     * 有参构造器：创建对象时直接传入初始值，避免"先 new 再一个个赋值"
     */
    public Person(String name, int age) {
        // this.name 指成员变量，name 指参数。同名时必须用 this 区分
        this.name = name;
        this.age = age;
        System.out.println("[构造器] 两参构造器被调用：" + name);
    }

    /**
     * 三个参数的构造器（重载：名字一样、参数列表不同）
     */
    public Person(String name, int age, String city) {
        // 调用本类的另一个构造器，必须是第一行，且只能调一次
        this(name, age);
        this.city = city;
        System.out.println("[构造器] 三参构造器继续补充城市：" + city);
    }

    /**
     * 只传名字的构造器：其余字段用默认值
     */
    public Person(String name) {
        this.name = name;
        this.age = 18;            // 给个默认年龄
        this.city = "未知";
        System.out.println("[构造器] 单参构造器被调用");
    }

    // 普通方法：修改年龄
    void setAge(int age) {
        // 简单校验：年龄不能为负
        if (age < 0) {
            System.out.println("年龄不能为负数，忽略这次修改");
            return;
        }
        this.age = age;
    }

    // 打印对象信息
    void show() {
        System.out.println("姓名=" + name + "，年龄=" + age + "，城市=" + city);
    }
}
