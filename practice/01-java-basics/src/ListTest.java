import java.util.ArrayList;
public class ListTest {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();//创建一个新数组

        names.add("乐乐");
        names.add("嘉良");
        names.add("咪咪");
        for(int i=0;i<names.size();i++){
            System.out.println(names.get(i));
        }


    }
}
