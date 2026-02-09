require("./adder.js");

GraalVM.run([]).then(() => {
  const a = 3;
  const b = 4;

  const result = globalThis.adder(a, b);
  console.log(`Result: ${result}`);

});