 class student {
    public String  name;
    public int age;
    public void introuduceSelf(){
        System.out.println("姓名"+name+"年龄"+age);
    }
}
class TestStudent{
    public static void main(String[] args) {
        student student=new student();
        student.name="靳";
        student.age=19;
        student.introuduceSelf();

    }
}