/**
 * 知识点 9：多态（Polymorphism）
 *
 * 多态 = "同一个方法名，不同的对象调用时表现不同"。
 *
 * 三个前提：
 *   1. 有继承（父类 + 子类） 或 实现接口
 *   2. 子类重写了父类的方法
 *   3. 用"父类类型的引用"指向"子类类型的对象"
 *
 * 编译时看"引用的类型"，运行时看"实际对象的类型" —— 这叫动态绑定。
 */
public class Polymorphism {

    public static void main(String[] args) {

        // 父类引用指向子类对象：编译时认为它是 Payment，运行时实际是 AliPay
        Payment p1 = new AliPay();
        Payment p2 = new WeChatPay();
        Payment p3 = new BankCardPay();

        // 同一个方法名 pay()，三个对象执行出三种不同行为 —— 这就是多态
        p1.pay(100);
        p2.pay(100);
        p3.pay(100);

        // 把多个对象放进数组，用循环统一处理，不用写三遍 if-else
        Payment[] all = {p1, p2, p3};
        double total = 0;
        for (Payment p : all) {
            total += p.pay(50);     // 每个子类返回各自的手续费
        }
        System.out.println("总手续费：" + total);

        // 多态 + 抽象类：Shape 没有具体实现，必须由子类实现
        Shape circle = new Circle(5);
        Shape rect = new Rectangle(3, 4);
        System.out.println("圆面积：" + circle.area());
        System.out.println("矩形面积：" + rect.area());

        // instanceof：运行期判断对象的真实类型（Java 16+ 支持模式变量）
        if (circle instanceof Circle c) {
            System.out.println("circle 确实是圆，半径 = " + c.getRadius());
        }
    }
}

/**
 * 支付接口：只声明"能做什么"，不规定"怎么做"
 */
interface Payment {

    /**
     * 支付方法：返回手续费
     * 接口里的方法默认是 public abstract（可以省略这两个关键字）
     */
    double pay(double amount);
}

/**
 * 支付宝实现类：implements 实现接口，必须实现接口里的所有抽象方法
 */
class AliPay implements Payment {

    @Override
    public double pay(double amount) {
        double fee = amount * 0.006;      // 支付宝费率 0.6%
        System.out.println("支付宝支付 " + amount + " 元，手续费 " + fee);
        return fee;
    }
}

class WeChatPay implements Payment {

    @Override
    public double pay(double amount) {
        double fee = amount * 0.006;
        System.out.println("微信支付 " + amount + " 元，手续费 " + fee);
        return fee;
    }
}

class BankCardPay implements Payment {

    @Override
    public double pay(double amount) {
        System.out.println("银行卡支付 " + amount + " 元，免手续费");
        return 0;
    }
}

/**
 * 抽象类：abstract 修饰，不能直接 new，必须由子类实现所有抽象方法后才能 new
 * 和接口的区别：
 *   - 抽象类可以有"已实现的方法"和成员变量
 *   - 接口主要是"能力声明"，Java 8 后也可以有 default 方法
 *   - 一个类只能继承一个抽象类，但可以实现多个接口
 */
abstract class Shape {

    // 抽象方法：只有声明，没有方法体，交给子类实现
    public abstract double area();

    // 普通方法：可以有实现，子类可直接用
    public void describe() {
        System.out.println("我是一个图形，面积是 " + area());   // 调用抽象方法，由子类实现
    }
}

class Circle extends Shape {

    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {

    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}
