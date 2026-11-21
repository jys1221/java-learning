/**
 * 知识点 10：重载（Overload） vs 重写（Override）
 *
 * 这两个词中文只差一个字，但完全是两件事，面试和笔试必考。
 *
 * 重载 Overload：
 *   - 发生在"同一个类"里
 *   - 方法名相同，参数列表不同（个数/类型/顺序不同）
 *   - 与返回值类型无关
 *   - 编译期就能确定调用哪个（静态绑定）
 *
 * 重写 Override：
 *   - 发生在"子类"里，覆盖父类的同名同参方法
 *   - 方法名、参数列表、返回值类型都必须相同（返回值可以是父类类型的子类，叫协变返回）
 *   - 访问权限不能比父类更小
 *   - 运行期根据实际对象类型决定调用哪个（动态绑定）
 */
public class OverloadVsOverride {

    public static void main(String[] args) {

        // ---------- 重载演示：同一个类里三个同名方法 ----------
        Calculator calc = new Calculator();

        // 编译器根据"实参的类型"选择对应版本
        System.out.println(calc.add(1, 2));                 // 调 int,int 版本 → 3
        System.out.println(calc.add(1.5, 2.5));             // 调 double,double 版本 → 4.0
        System.out.println(calc.add("Java", "学习"));       // 调 String,String 版本 → Java学习
        System.out.println(calc.add(1, 2, 3));              // 调三参版本 → 6

        // ---------- 重写演示 ----------
        Animal2 animal = new Animal2("动物");
        Dog2 dog = new Dog2("旺财");

        // 这里体现多态：引用类型是 Animal2，实际对象是 Dog2
        // 运行时会调用 Dog2 重写后的 sound()
        animal.sound();
        dog.sound();

        // 直接 new 父类对象，调的是父类原版
        new Animal2("野动物").sound();

        // 一个容易忽略的点：重写后想调父类原版，用 super.方法名()
        dog.callParentVersion();
    }
}

/**
 * 用来演示"重载"的类
 */
class Calculator {

    // 版本 1：两个整数
    public int add(int a, int b) {
        return a + b;
    }

    // 版本 2：两个小数（参数类型不同 → 构成重载）
    public double add(double a, double b) {
        return a + b;
    }

    // 版本 3：两个字符串（参数类型不同 → 构成重载）
    public String add(String a, String b) {
        return a + b;
    }

    // 版本 4：三个整数（参数个数不同 → 构成重载）
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // 注意：下面这种不算重载！只有返回值类型不同，参数一模一样 → 编译报错
    // public double add(int a, int b) { return a + b; }
}

/**
 * 父类：用来演示"重写"
 */
class Animal2 {

    protected String name;

    public Animal2(String name) {
        this.name = name;
    }

    public void sound() {
        System.out.println(name + "：……（父类的通用叫声）");
    }
}

/**
 * 子类：重写父类的 sound()
 */
class Dog2 extends Animal2 {

    public Dog2(String name) {
        super(name);
    }

    /**
     * @Override 是"校验注解"：如果方法签名和父类对不上，编译器立刻报错
     * 强烈建议所有重写都加上，防止手滑写错方法名（比如写成 sund()）
     */
    @Override
    public void sound() {
        System.out.println(name + "：汪汪汪！");
    }

    /**
     * 演示：重写之后仍然可以调用父类的原版实现
     */
    public void callParentVersion() {
        System.out.println("先来一句父类原版：");
        super.sound();      // super 指父类对象，调用父类原版
        System.out.println("再来一句子类版本：");
        this.sound();       // this 指自己，会调到重写后的版本
    }
}
