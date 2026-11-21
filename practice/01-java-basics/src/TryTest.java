public class TryTest {
    public static void main(String[] args) {
        try {
            int a = 10 / 0;
            System.out.println("这句话跑不到了");
        } catch (Exception e) {
            System.out.println("报错了:" + e.getMessage());
        }
        System.out.println("程序没崩继续下去");

    }
}
