package hello;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Java Module: " + Main.class.getModule().getName());
    }
}
