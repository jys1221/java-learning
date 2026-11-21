import java.util.HashMap;
public class MapTest {
    public static void main(String[] args) {
        HashMap<String,Integer> numbers= new HashMap<>();
        numbers.put("张三",123456);
        numbers.put("李四",12345);
        numbers.put("王五",12345678);
        for(String name:numbers.keySet()){
            int number = numbers.get(name);
            System.out.println(name+"手机号"+ number);
        }
    }
}
