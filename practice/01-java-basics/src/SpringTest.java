public class SpringTest {
    public static void main(String[] args) {
        ApplicationException context=new ClassPathXMLApplicationContext("spring-config.xml");
        Cow c=(Cow) context.getBean("cow");
        c.eat();

    }
}
