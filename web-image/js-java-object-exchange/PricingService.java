import java.util.function.Function;

import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSNumber;
import org.graalvm.webimage.api.JSObject;
import org.graalvm.webimage.api.JSString;

public class PricingService {
    @JS(args = {"handler"}, value = "globalThis.pricingService = handler;")
    private static native void export(Function<JSObject, JSObject> handler);

    public static void main(String[] args) {
        // Single-method export: JavaScript calls this Java method
        export(PricingService::handleRequest);
    }

    private static JSObject handleRequest(JSObject request) {
        try {
            String operation = request.get("operation", String.class);
            int price = request.get("price", Integer.class);
            JSObject user = request.get("user", JSObject.class);
            boolean premium = user.get("premium", Boolean.class);

            int discount = 0;
            if ("discount".equals(operation)) {
                discount = premium ? 20 : 10;
            }

            int finalPrice = price - (price * discount / 100);

            JSObject response = JSObject.create();
            response.set("finalPrice", JSNumber.of(finalPrice));
            response.set("discountApplied", JSNumber.of(price - finalPrice));
            response.set("premium", Boolean.valueOf(premium));
            return response;
        } catch (Throwable t) {
            JSObject error = JSObject.create();
            error.set("error", JSString.of("PricingService failed: " + t));
            return error;
        }
    }
}
