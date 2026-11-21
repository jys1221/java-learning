import java.util.ArrayList;
import java.util.Collections;

public class SortTest {
    public static void main(String[] args) {
        ArrayList<Integer> scores = new ArrayList<>();
        scores.add(85);
        scores.add(92);
        scores.add(78);
        scores.add(90);
        scores.add(65);

        // 一行代码排序
        Collections.sort(scores);

        System.out.println(scores);
    }
}
