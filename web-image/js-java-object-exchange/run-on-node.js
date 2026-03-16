require("./pricingservice.js");

GraalVM.run([]).then(() => {
  const request = {
    operation: "discount",
    price: 120,
    user: {
      premium: true,
    },
  };

  console.log("Request:", JSON.stringify(request));

  const result = globalThis.pricingService(request);
  console.log("Result:", JSON.stringify(result));
});
