import java.lang.reflect.Method;

class StringReverser {

    static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}

class StringCapitalizer {

    static String capitalize(String input) {
        return input.toUpperCase();
    }
}

public class ReflectionExample {

    public static void main(String[] args) throws ReflectiveOperationException {
        if (args.length == 0) {
            System.err.println("Please provide a class name, a method name, and input for the method (for example: StringCapitalizer capitalize \"hello\")");
            return;
        }
        String className = args[0];
        String methodName = args[1];
        String input = args[2];

        Class<?> clazz = Class.forName(className);
        Method method = clazz.getDeclaredMethod(methodName, String.class);
        Object result = method.invoke(null, input);
        System.out.println(result);
    }
}
