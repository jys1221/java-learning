import java.io.FileReader;
import java.io.IOException;

public class IOTest {
    public static void main(String[] args)throws IOException {
        FileReader reader =new FileReader("test.txt" );
        int data;
        while ((data=reader.read())!=-1) {
            System.out.print((char)data);

        }
        reader.close();


    }
}
