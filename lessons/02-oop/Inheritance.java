/**
 * 知识点 8：继承（Inheritance）
 *
 * 继承 = 子类"继承"父类的属性和方法，不用重复写。
 * 关键字：extends
 *
 * 规则：
 *   1. 一个类只能有一个直接父类（单继承），但可以多级继承（A→B→C）
 *   2. 子类可以重写（Override）父类的方法
 *   3. 子类构造器第一行会隐式调用父类无参构造器；调用有参父类构造器要写 super(...)
 *   4. private 成员不能被继承（外部/子类都访问不到）
 *   5. static 成员属于类，不参与"继承"的语义
 */
public class Inheritance {

    public static void main(String[] args) {

        // 创建子类对象，它会自动调用父类构造器
        Dog dog = new Dog("旺财", 3);
        dog.show();          // 父类的方法，子类可以直接用
        dog.bark();          // 子类自己的方法

        // 重写生效：调用的是子类版本的 describe()
        dog.describe();

        // 访问父类的 protected 成员（子类内部可以访问，见下方 Animal 类）
        System.out.println("种类：" + dog.species);

        // 静态成员用类名访问更规范
        System.out.println("动物总数：" + Animal.count);

        // 对比：Cat 也继承动物
        Cat cat = new Cat("小白", 2);
        cat.show();
        cat.meow();
        cat.describe();
    }
}

/**
 * 父类（基类）：动物
 */
class Animal {

    // protected：本类、子类、同包可以访问；外部类不能访问
    protected String name;
    protected int age;
    protected String species;      // 种类

    // 静态变量：所有动物共享
    public static int count = 0;

    /**
     * 父类构造器：子类 new 时会先执行它
     */
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
        this.species = "未知动物";
        count++;               // 每造一只动物，计数 +1
        System.out.println("[父类构造器] " + name + " 出生了");
    }

    // 普通方法：子类可以直接继承使用
    public void show() {
        System.out.println("我是 " + species + "，叫 " + name + "，今年 " + age + " 岁");
    }

    /**
     * 这个方法准备让子类"改写"，先给个通用实现
     */
    public void describe() {
        System.out.println("[父类版本] 我是一只动物");
    }
}

/**
 * 子类：狗 extends 动物
 */
class Dog extends Animal {

    // 子类可以有自己的额外字段
    private String breed;      // 品种

    /**
     * 子类构造器
     * 第一行 super(name, age) 调用父类的两参构造器（必须放在第一行）
     * 如果父类没有无参构造器，而你又不写 super(...)，编译直接报错
     */
    public Dog(String name, int age) {
        super(name, age);      // 调用父类构造器
        this.species = "狗";    // 改写父类的字段（protected 才能改）
        this.breed = "中华田园犬";
    }

    // 子类自己的方法
    public void bark() {
        System.out.println(name + "：汪汪汪！");
    }

    /**
     * 重写（Override）父类的 describe()
     * @Override 注解只是"检查"，写错方法名/参数时编译器会提醒你
     */
    @Override
    public void describe() {
        System.out.println("[子类重写版本] 我是一条" + breed + "，名字叫" + name);
    }
}

/**
 * 另一个子类：猫
 */
class Cat extends Animal {

    public Cat(String name, int age) {
        super(name, age);
        this.species = "猫";
    }

    public void meow() {
        System.out.println(name + "：喵喵喵～");
    }

    @Override
    public void describe() {
        System.out.println("[子类重写版本] 我是一只高冷的猫");
    }
}
