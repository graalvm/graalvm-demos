package hello;

public class JavaHello {
  public static String JavaHelloString = "Hello from Java!";

  public static String getJavaVersion() {
    return String.format("Running on: %s %s %s (%s)",
            System.getProperty("java.vm.name", ""),
            System.getProperty("java.vendor", ""),
            System.getProperty("java.vm.version", ""),
            System.getProperty("java.runtime.version", System.getProperty("java.version", "")));
  }

  public static String getHelloStringFromKotlin() {
    return KotlinHelloKt.getKotlinHelloString();
  }

  public static void main(String[] args) {
    System.out.println(getJavaVersion());
    System.out.println(getHelloStringFromKotlin());
    System.out.println(KotlinHelloKt.getHelloStringFromJava());
  }
}
