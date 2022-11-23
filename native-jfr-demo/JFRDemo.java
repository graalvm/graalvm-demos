import jdk.jfr.Event;
import jdk.jfr.Description;
import jdk.jfr.Label;

 public class JFRDemo {

   @Label("Hello World")
   @Description("Build and run a native executable with JFR.")
   static class HelloWorldEvent extends Event {
       @Label("Message")
       String message;
   }

   public static void main(String... args) {
       HelloWorldEvent event = new HelloWorldEvent();
       event.message = "Hello, World!";
       event.commit();
   }
 }