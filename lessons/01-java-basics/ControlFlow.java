/**
 * 知识点 3：流程控制 —— 让程序"会做判断、会重复"
 *
 * 程序默认是从上往下一行行执行的。
 * 流程控制语句用来改变这个顺序：
 *   分支：if / else / switch
 *   循环：for / while / do-while / 增强 for
 *   跳转：break / continue / return
 */
public class ControlFlow {

    public static void main(String[] args) {

        // ============ 一、if / else if / else ============
        int score = 85;

        // 条件必须是 boolean 表达式，不能写 if (score) 这种（Java 不像 C 语言）
        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 80) {          // 上一个条件不满足时才会走到这里
            System.out.println("良好");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");   // 所有条件都不满足才执行
        }

        // 三元运算符：条件 ? 真时的值 : 假时的值。适合简单的二选一赋值
        String result = score >= 60 ? "通过" : "挂科";
        System.out.println("结果：" + result);

        // ============ 二、switch ============
        // 根据某个变量的值，跳到对应的 case 分支
        int day = 3;
        switch (day) {
            case 1:
                System.out.println("周一");
                break;      // 必须 break，否则会"穿透"继续执行下一个 case
            case 2:
                System.out.println("周二");
                break;
            case 3:
                System.out.println("周三");
                break;
            default:        // 都不匹配时执行
                System.out.println("其他日子");
        }

        // Java 14+ 的箭头写法（不用写 break，也不会穿透）
        String dayName = switch (day) {
            case 1 -> "周一";
            case 2 -> "周二";
            case 3 -> "周三";
            default -> "其他";
        };
        System.out.println("箭头 switch：" + dayName);

        // ============ 三、for 循环（最常用） ============
        // 格式：for (初始化; 条件; 每次循环后的动作)
        // 执行顺序：初始化 → 判断条件 → 循环体 → 更新 → 判断条件 → ……
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");     // 输出 1 2 3 4 5
        }
        System.out.println();

        // 倒序循环
        for (int i = 5; i >= 1; i--) {
            System.out.print(i + " ");     // 5 4 3 2 1
        }
        System.out.println();

        // 步长为 2
        for (int i = 0; i <= 10; i += 2) {
            System.out.print(i + " ");     // 0 2 4 6 8 10
        }
        System.out.println();

        // ============ 四、增强 for（遍历数组/集合） ============
        // 格式：for (元素类型 变量名 : 数组或集合)
        // 它没有下标，适合"只看每个元素，不关心第几个"
        String[] fruits = {"苹果", "香蕉", "橙子"};
        for (String fruit : fruits) {
            System.out.println("水果：" + fruit);
        }

        // ============ 五、while / do-while ============
        // while：先判断再执行，可能一次都不执行
        int n = 1;
        while (n <= 3) {
            System.out.println("while 第" + n + "次");
            n++;                 // 一定要让条件有机会变假，否则死循环
        }

        // do-while：先执行一次，再判断。至少执行一次
        int m = 1;
        do {
            System.out.println("do-while 第" + m + "次");
            m++;
        } while (m <= 3);

        // ============ 六、break / continue ============
        // break：立即跳出整个循环
        for (int i = 1; i <= 10; i++) {
            if (i == 6) {
                System.out.println("到 6 就停");
                break;
            }
        }

        // continue：跳过本次循环剩下的代码，直接进入下一次
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue;        // 偶数跳过，不打印
            }
            System.out.print(i + " ");   // 只打印奇数 1 3 5 7 9
        }
        System.out.println();

        // ============ 七、嵌套循环：九九乘法表 ============
        // 外层控制行，内层控制列
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.printf("%d×%d=%-3d", j, i, i * j);   // %-3d 左对齐占 3 位
            }
            System.out.println();
        }
    }
}
