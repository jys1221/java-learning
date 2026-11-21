public class Test {
    public static void main(String[] args) {
        Animal a1=new Dog();
        a1.name="嘉良";
        Animal a2=new Cat();
        a2.name="乐乐";
        animalEat(a1);
        animalEat(a2);
    }
    public static void animalEat(Animal a){
        a.eat();
    }

}



