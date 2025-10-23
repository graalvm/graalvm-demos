globalThis.sharedBuffer = new SharedArrayBuffer(65536);
globalThis.sharedI32 = new Int32Array(globalThis.sharedBuffer);
postMessage({
  type: "sab",
  sab: globalThis.sharedBuffer,
});
importScripts("./wasm-spring-shell.js");
const config = new GraalVM.Config();
config.wasm_path = "./wasm-spring-shell.js.wasm";
GraalVM.run([], config);
