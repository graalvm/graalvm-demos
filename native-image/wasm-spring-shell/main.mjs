import { AnsiUp } from "./ansi_up.js";
const ansi_up = new AnsiUp();
const consolePrefix = "shell:>";

function moveCaretToEnd(el) {
  el.focus();
  const range = document.createRange();
  range.selectNodeContents(el);
  range.collapse(false);
  const sel = window.getSelection();
  sel.removeAllRanges();
  sel.addRange(range);
  el.scrollTop = el.scrollHeight;
}

const output = document.getElementById("output");
output.addEventListener("keydown", async (event) => {
  if (event.key === "Enter") {
    const lines = event.target.textContent.split("\n");
    const lastLine = lines[lines.length - 1];
    if (lastLine.startsWith(consolePrefix)) {
      disable();
      output.innerHTML += "\n";
      const line = lastLine.slice(consolePrefix.length);
      postLine(line);
    }
  }
});

function enable() {
  output.setAttribute("contenteditable", "true");
  output.focus();
  output.innerHTML += consolePrefix;
  moveCaretToEnd(output);
}

function disable() {
  output.setAttribute("contenteditable", "false");
}

window.appendOutput = (line) => {
  const html = ansi_up.ansi_to_html(line);
  output.innerHTML += html + "\n";
};

const worker = new Worker("./worker.js");

function postLine(str) {
  var bytes = new TextEncoder().encode(str);
  sharedI32[1] = bytes.length;
  new Uint8Array(sharedBuffer, 8).set(bytes);
  Atomics.store(sharedI32, 0, 1);
  Atomics.notify(sharedI32, 0, 1);
}

worker.onmessage = (e) => {
  const data = e.data;
  const type = data.type;

  console.log(`Got message: ${type}`);

  if (type === "sab") {
    globalThis.sharedBuffer = data.sab;
    globalThis.sharedI32 = new Int32Array(globalThis.sharedBuffer);
  } else if (type === "ready") {
    enable();
  } else if (type === "output") {
    appendOutput(data.message);
  } else if (type === "error") {
    appendOutput("Got error " + data.message);
  } else {
    appendOutput("Got unknown message: " + type);
  }
};
