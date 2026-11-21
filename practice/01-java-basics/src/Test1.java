import java.util.ArrayList;
import java.util.Collections;

public class Test1 {
    public static void main(String[] args) {
        ArrayList<Integer> scores=new ArrayList<>();
        scores.add(90);
        scores.add(80);
        scores.add(100);
        scores.add(70);
        Collections.sort(scores);//先排序
        int index = Collections.binarySearch(scores,90);//再查询;
        System.out.println("90在第"+index+"个位置");
    }
}
