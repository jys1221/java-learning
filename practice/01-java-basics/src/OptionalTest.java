import java.util.Optional;
public class OptionalTest {
    public static void main(String[] args) {
        String name = null;
       String result = Optional.ofNullable(name).orElse("无名");

        System.out.println("结果:"+result);
        String name2="张三";
        String result2 = Optional.ofNullable(name2).orElse("无名");
        System.out.println("结果:"+result2);
    }
}
