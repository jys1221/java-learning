import java.util.Scanner;

import static java.lang.System.in;

public class Test2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("请输入一个整数:");
        int i=in.nextInt();
        System.out.print("请输入一个小数:");
        double d=in.nextDouble();
        Scanner inStr =new Scanner(System.in);
        System.out.print("请输入一个字符串:");
        String s = inStr.nextLine ();
        System.out.print("i:"+i+"d:"+d);
        System.out.print("s:"+s+(i+d));
        System.out.println("程序结束");



    }
}
