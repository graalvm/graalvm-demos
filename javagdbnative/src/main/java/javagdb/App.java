package javagdb;


public class App 
{
    static long fieldUsed = 1000;

    public static void main( String[] args )
    {
        if (args.length > 0){
            int n = -1;
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.out.println(args[0] + " is not a number!");
            }
            if (n < 0) {
                System.out.println(args[0] + " is negative.");
            }
            double f = factorial(n);
            System.out.println(n + "! = " + f);
        } 

        if (false)
            neverCalledMethod();

        StringBuilder text = new StringBuilder();
        text.append("Hello World from GraalVM native image and GDB in Java.\n");
        text.append("Native image debugging made easy!");
        System.out.println(text.toString());
    }

    static void neverCalledMethod(){
        System.out.println("This method will be never called and taken of by native-image.");
    }

    static double factorial(int n) {
        if (n == 0) {
            return 1;
        }
        if (n >= fieldUsed) {
            return Double.POSITIVE_INFINITY;
        }
        double f = 1;
        while (n > 1) {
            f *= n--;
        }
        return f;
    }
}
