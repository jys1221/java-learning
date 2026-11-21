/**
 * 知识点 7：封装（Encapsulation）
 *
 * 封装 = 把数据藏起来，只留官方入口（方法）去读写。
 *
 * 为什么需要封装？
 *   如果字段是 public，谁都能直接改成非法值（年龄 = -5、价格 = 0），程序就乱了。
 *   封装后，所有修改都必须走 setter 方法，方法里可以先校验，不合法就拒绝。
 *
 * 标准做法：
 *   1. 字段用 private 修饰（外部无法直接访问）
 *   2. 提供 public 的 getXxx() / setXxx() 方法作为唯一入口
 *   3. 在 setter 里做合法性校验
 */
public class Encapsulation {

    public static void main(String[] args) {

        Account acc = new Account("佳良", 100.0);

        // 错误示范：如果 balance 是 public，下面这行就能直接把钱改成负数
        // acc.balance = -50;   // 编译报错：balance 是 private，外部访问不到

        // 正确方式：只能通过公开方法操作
        acc.show();

        acc.deposit(200);      // 存 200
        acc.show();

        boolean ok = acc.withdraw(250);   // 取 250
        System.out.println("取款是否成功：" + ok + "，余额：" + acc.getBalance());

        // 校验生效：取超过余额的钱会被拒绝
        boolean fail = acc.withdraw(1000);
        System.out.println("取 1000 是否成功：" + fail + "，余额：" + acc.getBalance());

        // 校验生效：存款传负数会被拒绝
        acc.deposit(-100);
        System.out.println("存 -100 后余额：" + acc.getBalance());

        // 只读字段：只提供 getter，不提供 setter，外部就改不了
        System.out.println("开户名：" + acc.getOwner());
    }
}

class Account {

    // 全部 private：外部看不到也改不了，只能通过下面的方法访问
    private String owner;      // 户名
    private double balance;    // 余额

    public Account(String owner, double balance) {
        this.owner = owner;
        // 构造器里也走一遍校验逻辑，避免绕过 setter
        if (balance < 0) {
            throw new IllegalArgumentException("初始余额不能为负");
        }
        this.balance = balance;
    }

    // getter：给外部"读"的窗口。只读字段就只给 getter
    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * 存款：方法内部可以做校验，这是封装最大的价值
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("存款金额必须大于 0，本次操作无效");
            return;     // 提前结束，后面的代码不执行
        }
        balance += amount;
    }

    /**
     * 取款：返回 boolean 告诉调用方成功还是失败
     * 余额不足时拒绝，并保持原余额不变
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("取款金额必须大于 0");
            return false;
        }
        if (amount > balance) {
            System.out.println("余额不足，当前只有 " + balance);
            return false;
        }
        balance -= amount;
        return true;
    }

    public void show() {
        System.out.println(owner + " 的余额：" + balance);
    }
}
