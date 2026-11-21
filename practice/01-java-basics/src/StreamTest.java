import java.util.ArrayList;
public class StreamTest {
    public static void main(String[] args) {
        ArrayList<String> foods=new ArrayList<>();
        foods.add("大米饭");
        foods.add("大面条");
        foods.add("小米粥");
        foods.stream()
                .filter(food->food.length()>2)
                .forEach(food->System.out.println(food));
    }

}
