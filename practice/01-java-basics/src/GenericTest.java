import java.util.ArrayList;
public class GenericTest {
    public static void main(String[] args) {
        ArrayList<Integer>nums=new ArrayList<>();
        nums.add(10);
        nums.add(20);
        nums.add(30);

        for(int i=0;i<nums.size();i++){
            System.out.println("第"+(i+1)+"个数:"+nums.get(i));

        }

    }

}
