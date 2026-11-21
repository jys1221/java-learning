import java.util.ArrayList;
import java.util.Collections;

public class BinarySearch {
    public static void main(String[] args) {
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(10);
        nums.add(20);
        nums.add(30);
        nums.add(40);
        nums.add(50);

        // 找30在哪个位置
        int index = Collections.binarySearch(nums, 30);

        System.out.println("30在第" + index + "个位置");
    }
}
