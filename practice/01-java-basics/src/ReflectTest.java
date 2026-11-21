import java.lang.reflect.Method;

public class ReflectTest {
    public static void main(String[] args) throws Exception{
        Class<?>cls=Class.forName("Dog");
        Object obj=cls.newInstance();
        Method m=cls.getMethod("eat");
        m.invoke(obj);

    }
}
