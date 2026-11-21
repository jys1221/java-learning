import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 知识点 22：反射（Reflection）
 *
 * 反射 = 程序运行时"照镜子看自己"：能拿到类的信息、创建对象、调用方法、改字段，
 * 即使这些成员是 private 的。
 *
 * 为什么需要反射？
 *   Spring 的依赖注入、MyBatis 把数据库行映射成对象、JSON 序列化……
 *   全都靠反射：框架事先不知道你的类长什么样，只能运行时动态分析。
 *
 * 代价：比直接调用慢，且编译期检查不到错误（写错方法名只有运行时才炸）。
 */
public class ReflectionDemo {

    public static void main(String[] args) throws Exception {

        // ---------- 一、拿到 Class 对象的三种方式 ----------
        // Class 对象是反射的入口，代表"某个类在内存里的元信息"
        Class<?> c1 = User.class;                         // 方式1：类名.class（编译期已知类型）
        User user = new User("佳良", 20);
        Class<?> c2 = user.getClass();                    // 方式2：对象.getClass()
        Class<?> c3 = Class.forName("User");              // 方式3：全限定类名（常用在配置文件里）

        System.out.println("三种方式拿到的类名：" + c1.getName() + " / " + c2.getName() + " / " + c3.getSimpleName());

        // ---------- 二、看类的结构 ----------
        System.out.println("简单类名：" + c1.getSimpleName());
        System.out.println("是否接口：" + c1.isInterface());
        System.out.println("父类：" + (c1.getSuperclass() == null ? "无" : c1.getSuperclass().getSimpleName()));

        // ---------- 三、看字段 ----------
        // getDeclaredFields：拿到本类声明的所有字段（包括 private）
        // getFields：只拿 public 的（包括继承来的）
        Field[] fields = c1.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("字段：" + f.getName()
                    + "，类型：" + f.getType().getSimpleName()
                    + "，修饰符：" + Modifier.toString(f.getModifiers()));
        }

        // 读 private 字段：默认不允许，要 setAccessible(true) 强行打开
        Field nameField = c1.getDeclaredField("name");
        nameField.setAccessible(true);
        Object nameValue = nameField.get(user);
        System.out.println("反射读到 name：" + nameValue);

        // 改 private 字段（这也是很多框架"偷偷赋值"的原理）
        nameField.set(user, "佳良（被反射改了）");
        System.out.println("改完后直接调用 getName：" + user.getName());

        // ---------- 四、看方法 ----------
        Method[] methods = c1.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("方法：" + m.getName()
                    + "，返回类型：" + m.getReturnType().getSimpleName()
                    + "，参数个数：" + m.getParameterCount());
        }

        // 调用方法：getMethod 拿 public 方法，invoke(对象, 参数...)
        Method hi = c1.getMethod("sayHi");
        Object result = hi.invoke(user);
        System.out.println("反射调用 sayHi 的返回值：" + result);

        // 调用带参数的方法
        Method setName = c1.getMethod("setName", String.class);
        setName.invoke(user, "新名字");
        System.out.println("反射改名字后：" + user.getName());

        // ---------- 五、动态创建对象 ----------
        // 方式1：无参构造器（最常用，Spring 默认用它）
        Constructor<?> noArg = c1.getDeclaredConstructor();
        User u2 = (User) noArg.newInstance();
        System.out.println("反射创建的对象：" + u2.getName() + "（默认值）");

        // 方式2：有参构造器
        Constructor<?> withArgs = c1.getDeclaredConstructor(String.class, int.class);
        User u3 = (User) withArgs.newInstance("小明", 19);
        System.out.println("反射创建并初始化：" + u3.getName() + "，" + u3.getAge());

        // ---------- 六、反射的实际用途示例 ----------
        // 模拟 Spring 的依赖注入：根据字段类型，自动 new 一个对象塞进去
        Service svc = new Service();
        Field daoField = Service.class.getDeclaredField("userDao");
        daoField.setAccessible(true);
        // 反射动态创建 UserDao 并注入，框架就是这么干的
        Object daoInstance = UserDao.class.getDeclaredConstructor().newInstance();
        daoField.set(svc, daoInstance);
        svc.doWork();       // 此时 userDao 已经不是 null 了
    }
}

/**
 * 演示用的实体类：字段都是 private，外部只能通过 getter/setter 访问
 * 但反射可以绕过这个限制
 */
class User {

    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }

    public String sayHi() {
        return "你好，我是 " + name;
    }
}

/**
 * 模拟一个"服务类"，它依赖 UserDao
 */
class Service {

    private UserDao userDao;    // 初始为 null，靠反射注入

    public void doWork() {
        if (userDao == null) {
            System.out.println("userDao 是 null，无法工作");
            return;
        }
        System.out.println("userDao 注入成功，开始干活");
    }
}

class UserDao {
    // 实际项目中这里是数据库操作
}
