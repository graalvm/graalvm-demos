import java.util.function.BiFunction;
import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSNumber;

public class Adder {
    public static int add(int a, int b) {
        return a + b;
    }

    @JS(args = {"adder"}, value = "globalThis.adder = adder;")
    private static native void export(BiFunction<JSNumber, JSNumber, JSNumber> adder);

    public static void main(String[] args) {
        export((a, b) -> {
            return JSNumber.of(add(a.asInt(), b.asInt()));
        });
    }
}