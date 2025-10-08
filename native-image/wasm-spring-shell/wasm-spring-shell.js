'use strict';


/**
 * @suppress {checkVars,checkTypes,duplicate}
 * @nocollapse
*/
var GraalVM = {};
(function() {(function() {(function() {

/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Represents a potential feature of the JS runtime.
 *
 * During construction, the detection callback is called to detect whether the runtime supports the feature.
 * The callback returns whether the feature was detected and may store some data
 * into the 'data' member for future use.
 */
class Feature {
    constructor(descr, detection_callback) {
        this.description = descr;
        this.data = {};
        this.detected = detection_callback(this.data);
    }
}

/**
 * Specialized feature to detect global variables.
 */
class GlobalVariableFeature extends Feature {
    constructor(descr, var_name) {
        super(descr, GlobalVariableFeature.detection_callback.bind(null, var_name));
    }

    /**
     * Function to detect a global variable.
     *
     * Uses the 'globalThis' object which should represent the global scope in
     * modern runtimes.
     */
    static detection_callback(var_name, data) {
        if (var_name in globalThis) {
            data.global = globalThis[var_name];
            return true;
        }

        return false;
    }

    /**
     * Returns the global detected by this feature.
     */
    get() {
        return this.data.global;
    }
}

/**
 * Specialized feature to detect the presence of Node.js modules.
 */
class RequireFeature extends Feature {
    constructor(descr, module_name) {
        super(descr, RequireFeature.detection_callback.bind(null, module_name));
    }

    /**
     * Function to detect a Node.js module.
     */
    static detection_callback(module_name, data) {
        if (typeof require != "function") {
            return false;
        }

        try {
            data.module = require(module_name);
            return true;
        } catch (e) {
            return false;
        }
    }

    /**
     * Returns the module detected by this feature.
     */
    get() {
        return this.data.module;
    }
}

/**
 * Collection of features needed for runtime functions.
 */
let features = {
    // https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
    fetch: new GlobalVariableFeature("Presence of the Fetch API", "fetch"),

    // https://nodejs.org/api/fs.html#promises-api
    node_fs: new RequireFeature("Presence of Node.js fs promises module", "fs/promises"),

    // https://nodejs.org/api/https.html
    node_https: new RequireFeature("Presence of Node.js https module", "https"),

    // https://nodejs.org/api/process.html
    node_process: new RequireFeature("Presence of Node.js process module", "process"),

    /**
     * Technically, '__filename' is not a global variable, it is a variable in the module scope.
     * https://nodejs.org/api/globals.html#__filename
     */
    filename: new Feature("Presence of __filename global", (d) => {
        if (typeof __filename != "undefined") {
            d.filename = __filename;
            return true;
        }

        return false;
    }),

    // https://developer.mozilla.org/en-US/docs/Web/API/Document/currentScript
    currentScript: new Feature("Presence of document.currentScript global", (d) => {
        if (
            typeof document != "undefined" &&
            "currentScript" in document &&
            document.currentScript != null &&
            "src" in document.currentScript
        ) {
            d.currentScript = document.currentScript;
            return true;
        }

        return false;
    }),

    // https://developer.mozilla.org/en-US/docs/Web/API/WorkerGlobalScope/location
    location: new Feature("Presence of Web worker location", (d) => {
        if (typeof self != "undefined") {
            d.location = self.location;
            return true;
        }
        return false;
    }),
};


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
function charArrayToString(arr) {
    let res = [];

    const len = 512;

    for (let i = 0; i < arr.length; i += len) {
        res.push(String.fromCharCode(...arr.slice(i, i + len)));
    }
    return res.join("");
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
function llog(p) {
    if (p instanceof Error) {
        console.log(p);
    } else if (p instanceof Object) {
        console.log(p.toString());
    } else {
        console.log(p);
    }
}

/**
 * A writer emulating stdout and stderr using console.log and console.error
 *
 * Since those functions cannot print without newline, lines are buffered but
 * without a max buffer size.
 */
class ConsoleWriter {
    constructor(logger) {
        this.line = "";
        this.newline = "\n".charCodeAt(0);
        this.closed = false;
        this.logger = logger;
    }

    printChars(chars) {
        let index = chars.lastIndexOf(this.newline);

        if (index >= 0) {
            this.line += charArrayToString(chars.slice(0, index));
            this.writeLine();
            chars = chars.slice(index + 1);
        }

        this.line += charArrayToString(chars);
    }

    writeLine() {
        this.logger(this.line);
        this.line = "";
    }

    flush() {
        if (this.line.length > 0) {
            // In JS we cannot print without newline, so flushing will always produce one
            this.writeLine();
        }
    }

    close() {
        if (this.closed) {
            return;
        }
        this.closed = true;

        this.flush();
    }
}

var stdoutWriter = new ConsoleWriter(console.log);
var stderrWriter = new ConsoleWriter(console.error);

/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Class that holds the configuration of the VM.
 */
class Config {
    constructor() {
        this.libraries = {};
        this.currentWorkingDirectory = "/root";
    }
}

/**
 * Class that holds the data required to start the VM.
 */
class Data {
    constructor(config) {
        /**
         * User-specified configuration object.
         *
         * @type Config
         */
        this.config = config;

        /**
         * Optionally holds a binary support file.
         */
        this.binaryImageHeap = null;

        /**
         * Maps the library names to prefetched content during VM bootup.
         * After the VM is initialized, the keys are retained, but values are set to null.
         */
        this.libraries = {};
    }
}

/**
 * Dummy object that exists only to avoid warnings in IDEs for the `$t[name]` expressions (used in JavaScript support files).
 */
const $t = {};

/**
 * For a given JavaScript class, returns an object that can lookup its properties.
 */
function cprops(cls) {
    return cls.prototype;
}

/**
 * Placeholder for lazy-value initializers.
 */
class LazyValueThunk {
    constructor(initializer) {
        this.initializer = initializer;
    }
}

/**
 * Creates a lazy property on the specified object.
 */
function lazy(obj, name, initializer) {
    let state = new LazyValueThunk(initializer);
    Object.defineProperty(obj, name, {
        configurable: false,
        enumerable: true,
        get: () => {
            if (state instanceof LazyValueThunk) {
                state = state.initializer();
            }
            return state;
        },
        set: () => {
            throw new Error("Property is not writable.");
        },
    });
}

/**
 * Placeholder for a method and its signature.
 */
class MethodMetadata {
    constructor(method, isStatic, returnHub, ...paramHubs) {
        this.method = method;
        this.isStatic = isStatic;
        this.returnHub = returnHub;
        this.paramHubs = paramHubs;
    }
}

/**
 * Create MethodMetadata object for an instance method.
 */
function mmeta(method, returnHub, ...paramHubs) {
    return new MethodMetadata(method, false, returnHub, ...paramHubs);
}

/**
 * Create MethodMetadata object for a static method.
 */
function smmeta(method, returnHub, ...paramHubs) {
    return new MethodMetadata(method, true, returnHub, ...paramHubs);
}

/**
 * Describes extra class metadata used by the runtime.
 */
class ClassMetadata {
    /**
     * Constructs the class metadata.
     *
     * @param ft Field table
     * @param singleAbstractMethod Method metadata for the single abstract method, when the class implements exactly one functional interface
     * @param methodTable Dictionary mapping each method name to the list of overloaded signatures
     */
    constructor(ft, singleAbstractMethod = undefined, methodTable = undefined) {
        this.ft = ft;
        this.singleAbstractMethod = singleAbstractMethod;
        this.methodTable = methodTable;
    }
}

/**
 * Class for the various runtime utilities.
 */
class Runtime {
    constructor() {
        this.isLittleEndian = false;
        /**
         * Dictionary of all initializer functions.
         */
        this.jsResourceInits = {};
        /**
         * The data object of the current VM, which contains the configuration settings,
         * optionally a binary-encoded image heap, and other resources.
         *
         * The initial data value is present in the enclosing scope.
         *
         * @type Data
         */
        this.data = null;
        /**
         * Map from full Java class names to corresponding Java hubs, for classes that are accessible outside of the image.
         */
        this.hubs = {};
        /**
         * The table of native functions that can be invoked via indirect calls.
         *
         * The index in this table represents the address of the function.
         * The zero-th entry is always set to null.
         */
        this.funtab = [null];
        /**
         * Map of internal symbols that are used during execution.
         */
        Object.defineProperty(this, "symbol", {
            writable: false,
            configurable: false,
            value: {
                /**
                 * Symbol used to symbolically get the to-JavaScript-native coercion object on the Java proxy.
                 *
                 * This symbol is available as a property on Java proxies, and will return a special object
                 * that can coerce the Java proxy to various native JavaScript values.
                 *
                 * See the ProxyHandler class for more details.
                 */
                javaScriptCoerceAs: Symbol("__javascript_coerce_as__"),

                /**
                 * Key used by Web Image to store the corresponding JS native value as a property of JSValue objects.
                 *
                 * Used by the JS annotation.
                 *
                 * Use conversion.setJavaScriptNative and conversion.extractJavaScriptNative to access that property
                 * instead of using this symbol directly.
                 */
                javaScriptNative: Symbol("__javascript_native__"),

                /**
                 * Key used to store a property value (inside a JavaScript object) that contains the Java-native object.
                 *
                 * Used by the JS annotation.
                 */
                javaNative: Symbol("__java_native__"),

                /**
                 * Key used to store the runtime-generated proxy handler inside the Java class.
                 *
                 * The handler is created lazily the first time that the corresponding class is added
                 *
                 * Use getOrCreateProxyHandler to retrieve the proxy handler instead of using this symbol directly.
                 */
                javaProxyHandler: Symbol("__java_proxy_handler__"),

                /**
                 * Key used to store the extra class metadata when emitting Java classes.
                 */
                classMeta: Symbol("__class_metadata__"),

                /**
                 * Key for the hub-object property that points to the corresponding generated JavaScript class.
                 */
                jsClass: Symbol("__js_class__"),

                /**
                 * Key for the property on primitive hubs, which points to the corresponding boxed hub.
                 */
                boxedHub: Symbol("__boxed_hub__"),

                /**
                 * Key for the property on primitive hubs, which holds the boxing function.
                 */
                box: Symbol("__box__"),

                /**
                 * Key for the property on primitive hubs, which holds the unboxing function.
                 */
                unbox: Symbol("__unbox__"),

                /**
                 * Key for the constructor-overload list that is stored in the class metadata.
                 */
                ctor: Symbol("__ctor__"),

                /**
                 * Internal value passed to JavaScript mirror-class constructors
                 * to denote that the mirrored class was instantiated from Java.
                 *
                 * This is used when a JSObject subclass gets constructed from Java.
                 */
                skipJavaCtor: Symbol("__skip_java_ctor__"),

                /**
                 * Symbol for the Java toString method.
                 */
                toString: Symbol("__toString__"),
            },
        });

        // Conversion-related functions and values.
        // The following values are set or used by the jsconversion module.

        /**
         * The holder of JavaScript mirror class for JSObject subclasses.
         */
        this.mirrors = {};
        /**
         * Reference to the hub of the java.lang.Class class.
         */
        this.classHub = null;
        /**
         * Function that retrieves the hub of the specified Java object.
         */
        this.hubOf = null;
        /**
         * Function that checks if the first argument hub is the supertype or the same as the second argument hub.
         */
        this.isSupertype = null;
        /**
         * Mapping from JavaScript classes that were imported to the list of internal Java classes
         * under which the corresponding JavaScript class was imported.
         * See JS.Import annotation.
         */
        this.importMap = new Map();
    }

    /**
     * Use the build-time endianness at run-time.
     *
     * Unsafe operations that write values to byte arrays at build-time assumes the
     * endianness of the build machine. Therefore, unsafe read and write operations
     * at run-time need to assume the same endianness.
     */
    setEndianness(isLittleEndian) {
        runtime.isLittleEndian = isLittleEndian;
    }

    /**
     * Ensures that there is a Set entry for the given JavaScript class, and returns it.
     */
    ensureFacadeSetFor(cls) {
        let facades = this.importMap.get(cls);
        if (facades === undefined) {
            facades = new Set();
            this.importMap.set(cls, facades);
        }
        return facades;
    }

    /**
     * Finds the set of Java facade classes for the given JavaScript class, or an empty set if there are none.
     */
    findFacadesFor(cls) {
        let facades = this.importMap.get(cls);
        if (facades === undefined) {
            facades = new Set();
        }
        return facades;
    }

    _ensurePackage(container, name) {
        const elements = name === "" ? [] : name.split(".");
        let current = container;
        for (let i = 0; i < elements.length; i++) {
            const element = elements[i];
            current = element in current ? current[element] : (current[element] = {});
        }
        return current;
    }

    /**
     * Get or create the specified exported JavaScript mirror-class export package on the VM object.
     *
     * @param name Full Java name of the package
     */
    ensureExportPackage(name) {
        return this._ensurePackage(vm.exports, name);
    }

    /**
     * Get or create the specified exported JavaScript mirror-class package on the Runtime object.
     *
     * @param name Full Java name of the package
     */
    ensureVmPackage(name) {
        return this._ensurePackage(runtime.mirrors, name);
    }

    /**
     * Get an existing exported JavaScript mirror class on the Runtime object.
     *
     * @param className Full Java name of the class
     */
    vmClass(className) {
        const elements = className.split(".");
        let current = runtime.mirrors;
        for (let i = 0; i < elements.length; i++) {
            const element = elements[i];
            if (element in current) {
                current = current[element];
            } else {
                return null;
            }
        }
        return current;
    }

    /**
     * Returns the array with all the prefetched library names.
     */
    prefetchedLibraryNames() {
        const names = [];
        for (const name in this.data.libraries) {
            names.push(name);
        }
        return names;
    }

    /**
     * Adds a function to the function table.
     *
     * @param f The function to add
     * @returns {number} The address of the newly added function
     */
    addToFuntab(f) {
        this.funtab.push(f);
        return runtime.funtab.length - 1;
    }

    /**
     * Fetches binary data from the given url.
     *
     * @param url
     * @returns {!Promise<!ArrayBuffer>}
     */
    fetchData(url) {
        return Promise.reject(new Error("fetchData is not supported"));
    }

    /**
     * Fetches UTF8 text from the given url.
     *
     * @param url
     * @returns {!Promise<!String>}
     */
    fetchText(url) {
        return Promise.reject(new Error("fetchText is not supported"));
    }

    /**
     * Sets the exit code for the VM.
     */
    setExitCode(c) {
        vm.exitCode = c;
    }

    /**
     * Returns the absolute path of the JS file WebImage is running in.
     *
     * Depending on the runtime, this may be a URL or an absolute filesystem path.
     * @returns {!String}
     */
    getCurrentFile() {
        throw new Error("getCurrentFile is not supported");
    }
}

/**
 * Instance of the internal runtime state of the VM.
 */
const runtime = new Runtime();

/**
 * VM state that is exposed, and which represents the VM API accessible to external users.
 */
class VM {
    constructor() {
        this.exitCode = 0;
        this.exports = {};
        this.symbol = {};
        /**
         * The to-JavaScript-native coercion symbol in the external API.
         */
        Object.defineProperty(this.symbol, "as", {
            configurable: false,
            enumerable: true,
            writable: false,
            value: runtime.symbol.javaScriptCoerceAs,
        });
        /**
         * The symbol for the Java toString method in the external API.
         */
        Object.defineProperty(this.symbol, "toString", {
            configurable: false,
            enumerable: true,
            writable: false,
            value: runtime.symbol.toString,
        });
    }

    /**
     * Coerce the specified JavaScript value to the specified Java type.
     *
     * For precise summary of the coercion rules, please see the JS annotation JavaDoc.
     *
     * The implementation for this function is injected later.
     *
     * @param javaScriptValue The JavaScript value to coerce
     * @param type The name of the Java class to coerce to, or a Java Proxy representing the target class.
     * @returns {*} The closest corresponding Java Proxy value
     */
    as(javaScriptValue, type) {
        throw new Error("VM.as is not supported in this backend or it was called too early");
    }
}

/**
 * Instance of the class that represents the public VM API.
 */
const vm = new VM();

if (features.node_fs.detected && features.node_https.detected) {
    runtime.fetchText = (url) => {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return new Promise((fulfill, reject) => {
                let content = [];
                features.node_https
                    .get()
                    .get(url, (r) => {
                        r.on("data", (data) => {
                            content.push(data);
                        });
                        r.on("end", () => {
                            fulfill(content.join(""));
                        });
                    })
                    .on("error", (e) => {
                        reject(e);
                    });
            });
        } else {
            return features.node_fs.get().readFile(url, "utf8");
        }
    };
    runtime.fetchData = (url) => {
        return features.node_fs
            .get()
            .readFile(url)
            .then((d) => d.buffer);
    };
} else if (features.fetch.detected) {
    runtime.fetchText = (url) =>
        features.fetch
            .get()(url)
            .then((r) => r.text());
    runtime.fetchData = (url) =>
        features.fetch
            .get()(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Failed to load data at '${url}': ${response.status} ${response.statusText}`);
                }
                return response.arrayBuffer();
            });
}

if (features.node_process.detected) {
    // Extend the setExitCode function to also set the exit code of the runtime.
    let oldFun = runtime.setExitCode;
    runtime.setExitCode = (exitCode) => {
        oldFun(exitCode);
        features.node_process.get().exitCode = exitCode;
    };
}

if (features.filename.detected) {
    runtime.getCurrentFile = () => features.filename.data.filename;
} else if (features.currentScript.detected) {
    runtime.getCurrentFile = () => features.currentScript.data.currentScript.src;
} else if (features.location.detected) {
    runtime.getCurrentFile = () => features.location.data.location.href;
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Imports object passed to the WASM module during instantiation.
 *
 * @see WasmImports
 */
const wasmImports = {};

/**
 * Imports for operations that cannot be performed (or be easily emulated) in WASM.
 */
wasmImports.compat = {
    f64rem: (x, y) => x % y,
    f64log: Math.log,
    f64log10: Math.log10,
    f64sin: Math.sin,
    f64cos: Math.cos,
    f64tan: Math.tan,
    f64tanh: Math.tanh,
    f64exp: Math.exp,
    f64pow: Math.pow,
    f64cbrt: Math.cbrt,
    f32rem: (x, y) => x % y,
};

/**
 * Imports relating to I/O.
 */
wasmImports.io = {};

/**
 * Loads and instantiates the appropriate WebAssembly module.
 *
 * The module path is given by config.wasm_path, if specified, otherwise it is loaded relative to the current script file.
 */
async function wasmInstantiate(config, args) {
    const wasmPath = config.wasm_path || runtime.getCurrentFile() + ".wasm";
    const file = await runtime.fetchData(wasmPath);
    const result = await WebAssembly.instantiate(file, wasmImports);
    return {
        instance: result.instance,
        memory: result.instance.exports.memory,
    };
}

/**
 * Runs the main entry point of the given WebAssembly module.
 */
function wasmRun(args) {
    try {
        doRun(args);
    } catch (e) {
        console.log("Uncaught internal error:");
        console.log(e);
        runtime.setExitCode(1);
    }
}

function getExports() {
    return runtime.data.wasm.instance.exports;
}

function getExport(name) {
    return getExports()[name];
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Constructs Java string from JavaScript string.
 */
function toJavaString(jsStr) {
    const length = jsStr.length;
    const charArray = getExport("array.char.create")(length);

    for (let i = 0; i < length; i++) {
        getExport("array.char.write")(charArray, i, jsStr.charCodeAt(i));
    }

    return getExport("string.fromchars")(charArray);
}

/**
 * Constructs a Java string array (String[]) from a JavaScript array of
 * JavaScript strings.
 */
function toJavaStringArray(jsStrings) {
    const length = jsStrings.length;
    const stringArray = getExport("array.string.create")(length);

    for (let i = 0; i < length; i++) {
        getExport("array.object.write")(stringArray, i, toJavaString(jsStrings[i]));
    }

    return stringArray;
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Checks if the given string is an array index.
 *
 * Proxied accesses always get a string property (even for indexed accesses) so
 * we need to check if the property access is an indexed access.
 *
 * A property is an index if the numeric index it is refering to has the same
 * string representation as the original property.
 * E.g the string '010' is a property while '10' is an index.
 */
function isArrayIndex(property) {
    try {
        return Number(property).toString() === property;
    } catch (e) {
        // Catch clause because not all property keys (e.g. symbols) can be
        // converted to a number.
        return false;
    }
}

/**
 * Proxy handler that proxies all array element and length accesses to a Wasm
 * array and everything else to the underlying array.
 *
 * See `proxyArray` function for more information.
 */
class ArrayProxyHandler {
    /**
     * Reference to the Wasm-world object.
     *
     * In this case, this will be a Wasm struct containing a Wasm array.
     */
    #wasmObject;

    /**
     * Immutable length of the array, determined during construction.
     */
    #length;

    /**
     * A callable (usually an exported function) that accepts the Wasm object
     * and an index and returns the element at that location.
     */
    #reader;

    constructor(wasmObject, reader) {
        this.#wasmObject = wasmObject;
        this.#length = getExport("array.length")(wasmObject);
        this.#reader = reader;
    }

    #isInBounds(idx) {
        return idx >= 0 && idx < this.#length;
    }

    #getElement(idx) {
        /*
         * We need an additional bounds check here because Wasm will trap,
         * while JS expects an undefined value.
         */
        if (this.#isInBounds(idx)) {
            return this.#reader(this.#wasmObject, idx);
        } else {
            return undefined;
        }
    }

    defineProperty() {
        throw new TypeError("This array is immutable. Attempted to call defineProperty");
    }

    deleteProperty() {
        throw new TypeError("This array is immutable. Attempted to call deleteProperty");
    }

    /**
     * Indexed accesses and the `length` property are serviced from the Wasm
     * object, everything else goes to the underlying object.
     */
    get(target, property, receiver) {
        if (isArrayIndex(property)) {
            return this.#getElement(property);
        } else if (property == "length") {
            return this.#length;
        } else {
            return Reflect.get(target, property, receiver);
        }
    }

    getOwnPropertyDescriptor(target, property) {
        if (isArrayIndex(property)) {
            return {
                value: this.#getElement(property),
                writable: false,
                enumerable: true,
                configurable: false,
            };
        } else {
            return Reflect.getOwnPropertyDescriptor(target, property);
        }
    }

    has(target, property) {
        if (isArrayIndex(property)) {
            return this.#isInBounds(Number(property));
        } else {
            return Reflect.has(target, property);
        }
    }

    isExtensible() {
        return false;
    }

    /**
     * Returns the array's own enumerable string-keyed property names.
     *
     * For arrays this is simply an array of all indices in string form.
     */
    ownKeys() {
        return Object.keys(Array.from({ length: this.#length }, (x, i) => i));
    }

    preventExtensions() {
        // Do nothing this object is already not extensible
    }

    set() {
        throw new TypeError("This array is immutable. Attempted to call set");
    }

    setPrototypeOf() {
        throw new TypeError("This array is immutable. Attempted to call setPrototypeOf");
    }
}

/**
 * Creates a read-only view on a Wasm array that looks like a JavaScript Array.
 *
 * The proxy is backed by an Array instance (albeit an empty one) and any
 * accesses except for `length` and indexed accesses (see `isArrayIndex`) are
 * proxied to the original object.
 * Because the `Array.prototype` methods are all generic and only access
 * `length` and the indices, this works. For example if `indexOf` is called on
 * the proxy, `Array.prototype.indexOf` is called with the proxy bound to
 * `this`, thus the `indexOf` implementation goes through the proxy when
 * accessing elements or determining the array length.
 */
function proxyArray(a, reader) {
    return new Proxy(new Array(), new ArrayProxyHandler(a, reader));
}

function proxyCharArray(a) {
    return proxyArray(a, getExport("array.char.read"));
}

wasmImports.convert = {};
wasmImports.convert.proxyCharArray = proxyCharArray;


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

function doRun(args) {
    getExport("main")(toJavaStringArray(args));
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
const STACK_TRACE_MARKER = "NATIVE-IMAGE-MARKER";

/**
 * Create JavaScript Error object, which is used to fill in the Throwable.backtrace object.
 */
function genBacktrace() {
    return new Error(STACK_TRACE_MARKER);
}

/**
 * Extract a Java string from the given backtrace object, which is supposed to be a JavaScript Error object.
 */
function formatStackTrace(backtrace) {
    let trace;

    if (backtrace.stack) {
        let lines = backtrace.stack.split("\n");

        /*
         * Since Error.prototype.stack is non-standard, different runtimes set
         * it differently.
         * We try to remove the preamble that contains the error name and
         * message to just get the stack trace.
         */
        if (lines.length > 0 && lines[0].includes(STACK_TRACE_MARKER)) {
            lines = lines.splice(1);
        }

        trace = lines.join("\n");
    } else {
        trace = "This JavaScript runtime does not expose stack trace information.";
    }

    return toJavaString(trace);
}

function gen_call_stack() {
    return formatStackTrace(genBacktrace());
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Provides the current working directory from the Config instance to Java code.
 */
function getCurrentWorkingDirectory() {
    return toJavaString(runtime.data.config.currentWorkingDirectory);
}
runtime.setEndianness(1);
wasmImports.interop = {
	'Date.now' : (...args) => Date.now(...args),
	'Math.atan2' : (...args) => Math.atan2(...args),
	'Math.cbrt' : (...args) => Math.cbrt(...args),
	'formatStackTrace' : (...args) => formatStackTrace(...args),
	'genBacktrace' : (...args) => genBacktrace(...args),
	'getCurrentWorkingDirectory' : (...args) => getCurrentWorkingDirectory(...args),
	'llog' : (...args) => llog(...args),
	'performance.now' : (...args) => performance.now(...args),
	'runtime.setExitCode' : (...args) => runtime.setExitCode(...args),
	'stderrWriter.close' : (...args) => stderrWriter.close(...args),
	'stderrWriter.flush' : (...args) => stderrWriter.flush(...args),
	'stderrWriter.printChars' : (...args) => stderrWriter.printChars(...args),
	'stdoutWriter.close' : (...args) => stdoutWriter.close(...args),
	'stdoutWriter.flush' : (...args) => stdoutWriter.flush(...args),
	'stdoutWriter.printChars' : (...args) => stdoutWriter.printChars(...args),
}
;
wasmImports.jsbody = {
	'_JSBigInt.javaString___String' : (...args) => (function(){
		try{
			return conversion.toProxy(toJavaString(this.toString()));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSBoolean.javaBoolean___Boolean' : (...args) => (function(){
		try{
			return conversion.toProxy(conversion.createJavaBoolean(this));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.asJavaObjectOrString___Object_Object' : (...args) => (function(obj){
		try{
			return conversion.isInternalJavaObject(obj) ? obj : toJavaString(obj.toString());
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.extractJavaScriptProxy___Object_Object' : (...args) => (function(self){
		try{
			return conversion.toProxy(self);
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.extractJavaScriptString___String_Object' : (...args) => (function(s){
		try{
			return conversion.extractJavaScriptString(s);
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.javaScriptToJava___Object_Object' : (...args) => (function(x){
		try{
			return conversion.javaScriptToJava(x);
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.javaScriptUndefined___Object' : (...args) => (function(){
		try{
			return undefined;
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSConversion.unproxy___Object_Object' : (...args) => (function(proxy){
		try{
			const javaNative = proxy[runtime.symbol.javaNative]; return javaNative === undefined ? null : javaNative;
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSNumber.javaDouble___Double' : (...args) => (function(){
		try{
			return conversion.toProxy(conversion.createJavaDouble(this));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSObject.get___Object_Object' : (...args) => (function(key){
		try{
			return this[key];
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSObject.stringValue___String' : (...args) => (function(){
		try{
			return conversion.toProxy(toJavaString(this.toString()));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSObject.typeofString___JSString' : (...args) => (function(){
		try{
			return typeof this;
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSString.javaString___String' : (...args) => (function(){
		try{
			return conversion.toProxy(toJavaString(this));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSString.of___String_JSString' : (...args) => (function(s){
		try{
			return conversion.extractJavaScriptString(s[runtime.symbol.javaNative]);
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSSymbol.javaString___String' : (...args) => (function(){
		try{
			return conversion.toProxy(toJavaString(this.toString()));
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_JSSymbol.referenceEquals___JSSymbol_JSSymbol_JSBoolean' : (...args) => (function(sym0,sym1){
		try{
			return sym0 === sym1;
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_WebImageNativeLibrarySupport.loadPrefetchedJSLibrary___JSString_JSObject' : (...args) => (function(content){
		try{
			return loadPrefetchedJSLibrary(content);
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
	'_WebImageUtil.random___D' : (...args) => (function(){
		try{
			return Math.random();
		}catch( e ) {
			conversion.handleJSError(e);}}).call(...args),
}
;


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Code dealing with moving values between the Java and JavaScript world.
 *
 * This code handles Java values:
 * - All Java values except for objects and longs are represented as JS Number values.
 * - Object and long representation depends on the backend used.
 * Variables and arguments representing Java values are usually marked explicitly
 * (e.g. by being named something like javaObject or jlstring, which stands for
 * java.lang.String).
 *
 * Java values can be used to call Java methods directly without additional
 * conversions, which is the basis of the functionality this class provides.
 * It facilitates calling Java methods from JavaScript by first performing the
 * necessary conversions or coercions before the Java call is executed. In the
 * reverse direction, it helps Java code execute JS code (e.g. through the @JS
 * annotation) by converting or coercing Java values into appropriate JS values.
 */
class Conversion {
    /**
     * Associates the given Java object with the given JS value.
     */
    setJavaScriptNative(javaObject, jsNative) {
        throw new Error("Unimplemented: Conversion.javaScriptNative");
    }

    /**
     * Returns the JS value associated with the given Java object or null if there is no associated value.
     */
    extractJavaScriptNative(javaObject) {
        throw new Error("Unimplemented: Conversion.extractJavaScriptNative");
    }

    // Java-to-JavaScript conversions

    /**
     * Given a Java boxed Double, creates the corresponding JavaScript number value.
     *
     * @param jldouble The java.lang.Double object
     * @return {*} A JavaScript Number value
     */
    extractJavaScriptNumber(jldouble) {
        throw new Error("Unimplemented: Conversion.extractJavaScriptNumber");
    }

    /**
     * Given a Java String, creates the corresponding JavaScript string value.
     *
     * Note: the Java method called in this implementation will return (in the generated code)
     * an actual primitive Java string.
     *
     * @param jlstring The java.lang.String object
     * @return {*} A JavaScript String value
     */
    extractJavaScriptString(jlstring) {
        throw new Error("Unimplemented: Conversion.extractJavaScriptString");
    }

    /**
     * Converts a Java array to a JavaScript array that contains JavaScript values
     * that correspond to the Java values of the input array.
     *
     * @param jarray A Java array
     * @returns {*} The resulting JavaScript array
     */
    extractJavaScriptArray(jarray) {
        throw new Error("Unimplemented: Conversion.extractJavaScriptArray");
    }

    // JavaScript-to-Java conversions (standard Java classes)

    /**
     * Creates a java.lang.Boolean object from a JavaScript boolean value.
     */
    createJavaBoolean(b) {
        throw new Error("Unimplemented: Conversion.createJavaBoolean");
    }

    /**
     * Creates a java.lang.Byte object from a JavaScript number value.
     */
    createJavaByte(x) {
        throw new Error("Unimplemented: Conversion.createJavaByte");
    }

    /**
     * Creates a java.lang.Short object from a JavaScript number value.
     */
    createJavaShort(x) {
        throw new Error("Unimplemented: Conversion.createJavaShort");
    }

    /**
     * Creates a java.lang.Character object from a JavaScript number value.
     */
    createJavaCharacter(x) {
        throw new Error("Unimplemented: Conversion.createJavaCharacter");
    }

    /**
     * Creates a java.lang.Integer object from a JavaScript number value.
     */
    createJavaInteger(x) {
        throw new Error("Unimplemented: Conversion.createJavaInteger");
    }

    /**
     * Creates a java.lang.Float object from a JavaScript number value.
     */
    createJavaFloat(x) {
        throw new Error("Unimplemented: Conversion.createJavaFloat");
    }

    /**
     * Creates a java.lang.Long object from a JavaScript number value.
     */
    createJavaLong(x) {
        throw new Error("Unimplemented: Conversion.createJavaLong");
    }

    /**
     * Creates a java.lang.Double object from a JavaScript number value.
     */
    createJavaDouble(x) {
        throw new Error("Unimplemented: Conversion.createJavaDouble");
    }

    /**
     * Gets the JavaKind ordinal for the given hub, as expected by `boxIfNeeded`.
     */
    getHubKindOrdinal(hub) {
        throw new Error("Unimplemented: Conversion.getHubKindOrdinal");
    }

    /**
     * Box the given value if the specified type is primitive.
     *
     * The parameter type is the enum index as defined in jdk.vm.ci.meta.JavaKind.
     * The following is a summary:
     *
     *      0 - Boolean
     *      1 - Byte
     *      2 - Short
     *      3 - Char
     *      4 - Int
     *      5 - Float
     *      6 - Long
     *      7 - Double
     *      8 - Object
     *
     * @param {number=} type
     */
    boxIfNeeded(javaValue, type) {
        switch (type) {
            case 0:
                return this.createJavaBoolean(javaValue);
            case 1:
                return this.createJavaByte(javaValue);
            case 2:
                return this.createJavaShort(javaValue);
            case 3:
                return this.createJavaCharacter(javaValue);
            case 4:
                return this.createJavaInteger(javaValue);
            case 5:
                return this.createJavaFloat(javaValue);
            case 6:
                return this.createJavaLong(javaValue);
            case 7:
                return this.createJavaDouble(javaValue);
            default:
                return javaValue;
        }
    }

    /**
     * Unbox the given value if the specified type is primitive.
     *
     * See documentation for `boxIfNeeded`.
     */
    unboxIfNeeded(javaObject, type) {
        switch (type) {
            case 0:
                return this.unboxBoolean(javaObject);
            case 1:
                return this.unboxByte(javaObject);
            case 2:
                return this.unboxShort(javaObject);
            case 3:
                return this.unboxChar(javaObject);
            case 4:
                return this.unboxInt(javaObject);
            case 5:
                return this.unboxFloat(javaObject);
            case 6:
                return this.unboxLong(javaObject);
            case 7:
                return this.unboxDouble(javaObject);
            default:
                return javaObject;
        }
    }

    unboxBoolean(jlBoolean) {
        throw new Error("Unimplemented: Conversion.unboxBoolean");
    }

    unboxByte(jlByte) {
        throw new Error("Unimplemented: Conversion.unboxByte");
    }

    unboxShort(jlShort) {
        throw new Error("Unimplemented: Conversion.unboxShort");
    }

    unboxChar(jlChar) {
        throw new Error("Unimplemented: Conversion.unboxChar");
    }

    unboxInt(jlInt) {
        throw new Error("Unimplemented: Conversion.unboxInt");
    }

    unboxFloat(jlFloat) {
        throw new Error("Unimplemented: Conversion.unboxFloat");
    }

    unboxLong(jlLong) {
        throw new Error("Unimplemented: Conversion.unboxLong");
    }

    unboxDouble(jlDouble) {
        throw new Error("Unimplemented: Conversion.unboxDouble");
    }

    /**
     * Gets the boxed counterpart of the given primitive hub.
     */
    getBoxedHub(jlClass) {
        throw new Error("Unimplemented: Conversion.getBoxedHub");
    }

    // JavaScript-to-Java conversions (JSValue classes)

    /**
     * Gets the Java singleton object that represents the JavaScript undefined value.
     */
    createJSUndefined() {
        throw new Error("Unimplemented: Conversion.createJSUndefined");
    }

    /**
     * Wraps a JavaScript Boolean into a Java JSBoolean object.
     *
     * @param boolean The JavaScript boolean to wrap
     * @return {*} The Java JSBoolean object
     */
    createJSBoolean(boolean) {
        throw new Error("Unimplemented: Conversion.createJSBoolean");
    }

    /**
     * Wraps a JavaScript Number into a Java JSNumber object.
     *
     * @param number The JavaScript number to wrap
     * @return {*} The Java JSNumber object
     */
    createJSNumber(number) {
        throw new Error("Unimplemented: Conversion.createJSNumber");
    }

    /**
     * Wraps a JavaScript BigInt into a Java JSBigInt object.
     *
     * @param bigint The JavaScript BigInt value to wrap
     * @return {*} The Java JSBigInt object
     */
    createJSBigInt(bigint) {
        throw new Error("Unimplemented: Conversion.createJSBigInt");
    }

    /**
     * Wraps a JavaScript String into a Java JSString object.
     *
     * @param string The JavaScript String value to wrap
     * @return {*} The Java JSString object
     */
    createJSString(string) {
        throw new Error("Unimplemented: Conversion.createJSString");
    }

    /**
     * Wraps a JavaScript Symbol into a Java JSSymbol object.
     *
     * @param symbol The JavaScript Symbol value to wrap
     * @return {*} The Java JSSymbol object
     */
    createJSSymbol(symbol) {
        throw new Error("Unimplemented: Conversion.createJSSymbol");
    }

    /**
     * Wraps a JavaScript object into a Java JSObject object.
     *
     * @param obj The JavaScript Object value to wrap
     * @returns {*} The Java JSObject object
     */
    createJSObject(obj) {
        throw new Error("Unimplemented: Conversion.createJSObject");
    }

    // Helper methods

    /**
     * Checks if the specified object (which may be a JavaScript value or a Java value) is an internal Java object.
     */
    isInternalJavaObject(obj) {
        throw new Error("Unimplemented: Conversion.isInternalJavaObject");
    }

    isPrimitiveHub(hub) {
        throw new Error("Unimplemented: Conversion.isPrimitiveHub");
    }

    isJavaLangString(obj) {
        throw new Error("Unimplemented: Conversion.isJavaLangString");
    }

    isJavaLangClass(obj) {
        throw new Error("Unimplemented: Conversion.isJavaLangClassHub");
    }

    isInstance(obj, hub) {
        throw new Error("Unimplemented: Conversion.isInstance");
    }

    /**
     * Copies own fields from source to destination.
     *
     * Existing fields in the destination are overwritten.
     */
    copyOwnFields(src, dst) {
        for (let name of Object.getOwnPropertyNames(src)) {
            dst[name] = src[name];
        }
    }

    /**
     * Creates an anonymous JavaScript object, and does the mirror handshake.
     */
    createAnonymousJavaScriptObject() {
        const x = {};
        const jsObject = this.createJSObject(x);
        x[runtime.symbol.javaNative] = jsObject;
        return x;
    }

    /**
     * Obtains or creates the proxy handler for the given Java class
     */
    getOrCreateProxyHandler(arg) {
        throw new Error("Unimplemented: Conversion.getOrCreateProxyHandler");
    }

    /**
     * For proxying the given object returns value that should be passed to
     * getOrCreateProxyHandler.
     */
    _getProxyHandlerArg(obj) {
        throw new Error("Unimplemented: Conversion._getProxyHandlerArg");
    }

    /**
     * Creates a proxy that intercepts messages that correspond to Java method calls and Java field accesses.
     *
     * @param obj The Java object to create a proxy for
     * @return {*} The proxy around the Java object
     */
    toProxy(obj) {
        let proxyHandler = this.getOrCreateProxyHandler(this._getProxyHandlerArg(obj));
        // The wrapper is a temporary object that allows having the non-identifier name of the target function.
        // We declare the property as a function, to ensure that it is constructable, so that the Proxy handler's construct method is callable.
        let targetWrapper = {
            ["Java Proxy"]: function (key) {
                if (key === runtime.symbol.javaNative) {
                    return obj;
                }
                return undefined;
            },
        };

        return new Proxy(targetWrapper["Java Proxy"], proxyHandler);
    }

    /**
     * Converts a JavaScript value to the corresponding Java representation.
     *
     * The exact rules of the mapping are documented in the Java JS annotation class.
     *
     * This method is only meant to be called from the conversion code generated for JS-annotated methods.
     *
     * @param x The JavaScript value to convert
     * @return {*} The Java representation of the JavaScript value
     */
    javaScriptToJava(x) {
        // Step 1: check null, which is mapped 1:1 to null in Java.
        if (x === null) {
            return null;
        }

        // Step 2: check undefined, which is a singleton in Java.
        if (x === undefined) {
            return this.createJSUndefined();
        }

        // Step 3: check if the javaNative property is set.
        // This covers objects that already have Java counterparts (for example, Java proxies).
        const javaValue = x[runtime.symbol.javaNative];
        if (javaValue !== undefined) {
            return javaValue;
        }

        // Step 4: use the JavaScript type to select the appropriate Java representation.
        const tpe = typeof x;
        switch (tpe) {
            case "boolean":
                return this.createJSBoolean(x);
            case "number":
                return this.createJSNumber(x);
            case "bigint":
                return this.createJSBigInt(x);
            case "string":
                return this.createJSString(x);
            case "symbol":
                return this.createJSSymbol(x);
            case "object":
            case "function":
                // We know this is a normal object created in JavaScript,
                // otherwise it would have a runtime.symbol.javaNative property,
                // and the conversion would have returned in Step 3.
                return this.createJSObject(x);
            default:
                throw new Error("unexpected type: " + tpe);
        }
    }

    /**
     * Maps each JavaScript value in the input array to a Java value.
     * See {@code javaScriptToJava}.
     */
    eachJavaScriptToJava(javaScriptValues) {
        const javaValues = new Array(javaScriptValues.length);
        for (let i = 0; i < javaScriptValues.length; i++) {
            javaValues[i] = this.javaScriptToJava(javaScriptValues[i]);
        }
        return javaValues;
    }

    /**
     * Converts a Java value to JavaScript.
     */
    javaToJavaScript(x) {
        throw new Error("Unimplemented: Conversion.javaToJavaScript");
    }

    throwClassCastExceptionImpl(javaObject, tpeNameJavaString) {
        throw new Error("Unimplemented: Conversion.throwClassCastExceptionImpl");
    }

    throwClassCastException(javaObject, tpe) {
        let tpeName;
        if (typeof tpe === "string") {
            tpeName = tpe;
        } else if (typeof tpe === "function") {
            tpeName = tpe.name;
        } else {
            tpeName = tpe.toString();
        }
        this.throwClassCastExceptionImpl(javaObject, toJavaString(tpeName));
    }

    /**
     * Converts the specified Java Proxy to the target JavaScript type, if possible.
     *
     * This method is meant to be called from Java Proxy object, either when implicit coercion is enabled,
     * or when the user explicitly invokes coercion on the Proxy object.
     *
     * @param proxyHandler handler for the proxy that must be converted
     * @param proxy the Java Proxy object that should be coerced
     * @param tpe target JavaScript type name (result of the typeof operator) or constructor function
     * @return {*} the resulting JavaScript value
     */
    coerceJavaProxyToJavaScriptType(proxyHandler, proxy, tpe) {
        throw new Error("Unimplemented: Conversion.coerceJavaProxyToJavaScriptType");
    }

    /**
     * Try to convert the JavaScript object to a Java facade class, or return null.
     *
     * @param obj JavaScript object whose Java facade class we search for
     * @param cls target Java class in the form of its JavaScript counterpart
     * @return {*} the mirror instance wrapped into a JavaScript Java Proxy, or null
     */
    tryExtractFacadeClass(obj, cls) {
        const facades = runtime.findFacadesFor(obj.constructor);
        const rawJavaHub = cls[runtime.symbol.javaNative];
        const internalJavaClass = rawJavaHub[runtime.symbol.jsClass];
        if (facades.has(internalJavaClass)) {
            const rawJavaMirror = new internalJavaClass();
            // Note: only one-way handshake, since the JavaScript object could be recast to a different Java facade class.
            this.setJavaScriptNative(rawJavaMirror, obj);
            return this.toProxy(rawJavaMirror);
        } else {
            return null;
        }
    }

    /**
     * Coerce the specified JavaScript value to the specified Java type.
     *
     * See VM.as for the specification of this function.
     */
    coerceJavaScriptToJavaType(javaScriptValue, type) {
        throw new Error("Unimplemented: Conversion.coerceJavaScriptToJavaType");
    }
}

/**
 * Handle for proxying Java objects.
 *
 * Client JS code never directly sees Java object, instead they see proxies
 * using this handler. The handler is generally specialized per type.
 * It provides access to the underlying Java methods.
 *
 * It also supports invoking the proxy, which calls the single abstract method
 * in the Java object if available, and for Class objects the new operator
 * works, creating a Java object and invoking a matching constructor.
 *
 * The backends provide method metadata describing the Java methods available
 * to the proxy. At runtime, when a method call is triggered (a method is
 * accessed and called, the proxy itself is invoked, or a constructor is called),
 * the proxy will find a matching implementation based on the types of the
 * passed arguments.
 * Arguments and return values are automatically converted to and from Java
 * objects respectively, but no coercion is done.
 */
class ProxyHandler {
    constructor() {
        this._initialized = false;
        this._methods = {};
        this._staticMethods = {};
        this._javaConstructorMethod = null;
    }

    ensureInitialized() {
        if (!this._initialized) {
            this._initialized = true;
            // Function properties derived from accessible Java methods.
            this._createProxyMethods();
            // Default function properties.
            this._createDefaultMethods();
        }
    }

    _getMethods() {
        this.ensureInitialized();
        return this._methods;
    }

    _getStaticMethods() {
        this.ensureInitialized();
        return this._staticMethods;
    }

    _getJavaConstructorMethod() {
        this.ensureInitialized();
        return this._javaConstructorMethod;
    }

    /**
     * Returns a ClassMetadata instance for the class this proxy handler represents.
     */
    _getClassMetadata() {
        throw new Error("Unimplemented: ProxyHandler._getClassMetadata");
    }

    _getMethodTable() {
        const classMeta = this._getClassMetadata();
        if (classMeta === undefined) {
            return undefined;
        }
        return classMeta.methodTable;
    }

    /**
     * String that can be printed as part of the toString and valueOf functions.
     */
    _getClassName() {
        throw new Error("Unimplemented: ProxyHandler._getClassName");
    }

    /**
     * Link the methods object to the prototype chain of the methods object of the superclass' proxy handler.
     */
    _linkMethodPrototype() {
        throw new Error("Unimplemented: ProxyHandler._linkMethodPrototype");
    }

    _createProxyMethods() {
        // Create proxy methods for the current class.
        const methodTable = this._getMethodTable();
        if (methodTable === undefined) {
            return;
        }

        const proxyHandlerThis = this;
        for (const name in methodTable) {
            const overloads = methodTable[name];
            const instanceOverloads = [];
            const staticOverloads = [];
            for (const m of overloads) {
                if (m.isStatic) {
                    staticOverloads.push(m);
                } else {
                    instanceOverloads.push(m);
                }
            }
            if (instanceOverloads.length > 0) {
                this._methods[name] = function (...javaScriptArgs) {
                    // Note: the 'this' value is bound to the Proxy object.
                    return proxyHandlerThis._invokeProxyMethod(name, instanceOverloads, this, ...javaScriptArgs);
                };
            }
            if (staticOverloads.length > 0) {
                this._staticMethods[name] = function (...javaScriptArgs) {
                    // Note: the 'this' value is bound to the Proxy object.
                    return proxyHandlerThis._invokeProxyMethod(name, staticOverloads, null, ...javaScriptArgs);
                };
            }
        }
        if (methodTable[runtime.symbol.ctor] !== undefined) {
            const overloads = methodTable[runtime.symbol.ctor];
            this._javaConstructorMethod = function (javaScriptJavaProxy, ...javaScriptArgs) {
                // Note: the 'this' value is bound to the Proxy object.
                return proxyHandlerThis._invokeProxyMethod("<init>", overloads, javaScriptJavaProxy, ...javaScriptArgs);
            };
        } else {
            this._javaConstructorMethod = function (javaScriptJavaProxy, ...javaScriptArgs) {
                throw new Error(
                    "Cannot invoke the constructor. Make sure that the constructors are explicitly added to the image."
                );
            };
        }

        this._linkMethodPrototype();
    }

    /**
     * Checks whether the given argument values can be used to call the method identified by the given metdata class.
     */
    _conforms(args, metadata) {
        if (metadata.paramHubs.length !== args.length) {
            return false;
        }
        for (let i = 0; i < args.length; i++) {
            const arg = args[i];
            let paramHub = metadata.paramHubs[i];
            if (paramHub === null) {
                // A null parameter hub means that the type-check always passes.
                continue;
            }
            if (conversion.isPrimitiveHub(paramHub)) {
                // A primitive hub must be replaced with the hub of the corresponding boxed type.
                paramHub = conversion.getBoxedHub(paramHub);
            }
            if (!conversion.isInstance(arg, paramHub)) {
                return false;
            }
        }
        return true;
    }

    _unboxJavaArguments(args, metadata) {
        // Precondition -- method metadata refers to a method with a correct arity.
        for (let i = 0; i < args.length; i++) {
            const paramHub = metadata.paramHubs[i];
            args[i] = conversion.unboxIfNeeded(args[i], conversion.getHubKindOrdinal(paramHub));
        }
    }

    _createDefaultMethods() {
        if (!this._methods.hasOwnProperty("toString")) {
            // The check must use hasOwnProperty, because toString always exists in the prototype.
            this._methods["toString"] = () => "[Java Proxy: " + this._getClassName() + "]";
        } else {
            const javaToString = this._methods["toString"];
            this._methods[runtime.symbol.toString] = javaToString;
            this._methods["toString"] = function () {
                // The `this` value must be bound to the proxy instance.
                //
                // The `toString` method is used often in JavaScript, and treated specially.
                // If its return type is a Java String, then that string is converted to a JavaScript string.
                // In other words, if the result of the call is a JavaScript proxy (see _invokeProxyMethod return value),
                // then proxies that represent java.lang.String are converted to JavaScript strings.
                const javaScriptResult = javaToString.call(this);
                if (typeof javaScriptResult === "function" || typeof javaScriptResult === "object") {
                    const javaResult = javaScriptResult[runtime.symbol.javaNative];
                    if (javaResult !== undefined && conversion.isJavaLangString(javaResult)) {
                        return conversion.extractJavaScriptString(javaResult);
                    } else {
                        return javaScriptResult;
                    }
                } else {
                    return javaScriptResult;
                }
            };
        }

        // Override Java methods that return valueOf.
        // JavaScript requires that valueOf returns a JavaScript primitive (in this case, string).
        this._methods["valueOf"] = () => "[Java Proxy: " + this._getClassName() + "]";

        const proxyHandlerThis = this;
        const asProperty = function (tpe) {
            // Note: this will be bound to the Proxy object.
            return conversion.coerceJavaProxyToJavaScriptType(proxyHandlerThis, this, tpe);
        };
        if (!("$as" in this._methods)) {
            this._methods["$as"] = asProperty;
        }
        this._methods[runtime.symbol.javaScriptCoerceAs] = asProperty;

        const vmProperty = vm;
        if (!("$vm" in this._methods)) {
            this._methods["$vm"] = vmProperty;
        }
    }

    _loadMethod(target, key) {
        const member = this._getMethods()[key];
        if (member !== undefined) {
            return member;
        }
    }

    _methodNames() {
        return Object.keys(this._getMethods());
    }

    _invokeProxyMethod(name, overloads, javaScriptJavaProxy, ...javaScriptArgs) {
        // For static methods, javaScriptThis is set to null.
        const isStatic = javaScriptJavaProxy === null;
        const javaThis = isStatic ? null : javaScriptJavaProxy[runtime.symbol.javaNative];
        const javaArgs = conversion.eachJavaScriptToJava(javaScriptArgs);
        for (let i = 0; i < overloads.length; i++) {
            const metadata = overloads[i];
            if (this._conforms(javaArgs, metadata)) {
                // Where necessary, perform unboxing of Java arguments.
                this._unboxJavaArguments(javaArgs, metadata);
                let javaResult;
                try {
                    if (isStatic) {
                        javaResult = metadata.method.call(null, ...javaArgs);
                    } else {
                        javaResult = metadata.method.call(null, javaThis, ...javaArgs);
                    }
                } catch (error) {
                    throw conversion.javaToJavaScript(error);
                }
                if (javaResult === undefined) {
                    // This only happens when the return type is void.
                    return undefined;
                }
                // If necessary, box the Java return value.
                const retHub = metadata.returnHub;
                javaResult = conversion.boxIfNeeded(javaResult, conversion.getHubKindOrdinal(retHub));
                const javaScriptResult = conversion.javaToJavaScript(javaResult);
                return javaScriptResult;
            }
        }
        const methodName = name !== null ? "method '" + name + "'" : "single abstract method";
        throw new Error("No matching signature for " + methodName + " and argument list '" + javaScriptArgs + "'");
    }

    /**
     * The Java type hierarchy is not modelled in the proxy and the proxied
     * object has no prototype.
     */
    getPrototypeOf(target) {
        return null;
    }

    /**
     * Modifying the prototype of the proxied object is not allowed.
     */
    setPrototypeOf(target, prototype) {
        return false;
    }

    /**
     * Proxied objects are not extensible in any way.
     */
    isExtensible(target) {
        return false;
    }

    /**
     * We allow calling Object.preventExtensions on the proxy.
     * However, it won't do anything, the proxy handler already prevents extensions.
     */
    preventExtensions(target) {
        return true;
    }

    getOwnPropertyDescriptor(target, key) {
        const value = this._loadMethod(target, key);
        if (value === undefined) {
            return undefined;
        }
        return {
            value: value,
            writable: false,
            enumerable: false,
            configurable: false,
        };
    }

    /**
     * Defining properties on the Java object is not allowed.
     */
    defineProperty(target, key, descriptor) {
        return false;
    }

    has(target, key) {
        return this._loadMethod(target, key) !== undefined;
    }

    get(target, key) {
        if (key === runtime.symbol.javaNative) {
            return target(runtime.symbol.javaNative);
        } else {
            const javaObject = target(runtime.symbol.javaNative);
            // TODO GR-60603 Deal with arrays in WasmGC backend
            if (Array.isArray(javaObject) && typeof key === "string") {
                const index = Number(key);
                if (0 <= index && index < javaObject.length) {
                    return conversion.javaToJavaScript(javaObject[key]);
                } else if (key === "length") {
                    return javaObject.length;
                }
            }
        }
        return this._loadMethod(target, key);
    }

    set(target, key, value, receiver) {
        const javaObject = target(runtime.symbol.javaNative);
        // TODO GR-60603 Deal with arrays in WasmGC backend
        if (Array.isArray(javaObject)) {
            const index = Number(key);
            if (0 <= index && index < javaObject.length) {
                javaObject[key] = conversion.javaScriptToJava(value);
                return true;
            }
        }
        return false;
    }

    /**
     * Deleting properties on the Java object is not allowed.
     */
    deleteProperty(target, key) {
        return false;
    }

    ownKeys(target) {
        return this._methodNames();
    }

    apply(target, javaScriptThisArg, javaScriptArgs) {
        // We need to convert the Proxy's target function to the Java Proxy.
        const javaScriptJavaProxy = conversion.toProxy(target(runtime.symbol.javaNative));
        // Note: the JavaScript this argument for the apply method is never exposed to Java, so we just ignore it.
        return this._applyWithObject(javaScriptJavaProxy, javaScriptArgs);
    }

    _getSingleAbstractMethod(javaScriptJavaProxy) {
        return this._getClassMetadata().singleAbstractMethod;
    }

    _applyWithObject(javaScriptJavaProxy, javaScriptArgs) {
        const sam = this._getSingleAbstractMethod(javaScriptJavaProxy);
        if (sam === undefined) {
            throw new Error("Java Proxy is not a functional interface, so 'apply' cannot be called from JavaScript.");
        }
        return this._invokeProxyMethod(null, [sam], javaScriptJavaProxy, ...javaScriptArgs);
    }

    /**
     * Create uninitialized instance of given Java type.
     */
    _createInstance(hub) {
        throw new Error("Unimplemented: ProxyHandler._createInstance");
    }

    construct(target, argumentsList) {
        const javaThis = target(runtime.symbol.javaNative);
        // This is supposed to be a proxy handler for java.lang.Class objects
        // and javaThis is supposed to be some Class instance.
        if (!conversion.isJavaLangClass(javaThis)) {
            throw new Error(
                "Cannot invoke the 'new' operator. The 'new' operator can only be used on Java Proxies that represent the 'java.lang.Class' type."
            );
        }
        // Allocate the Java object from Class instance
        const javaInstance = this._createInstance(javaThis);
        // Lookup constructor method of the target class.
        // This proxy handler is for java.lang.Class while javaThis is a
        // java.lang.Class instance for some object type for which we want to
        // lookup the constructor.
        const instanceProxyHandler = conversion.getOrCreateProxyHandler(conversion._getProxyHandlerArg(javaInstance));
        const javaConstructorMethod = instanceProxyHandler._getJavaConstructorMethod();
        // Convert the Java instance to JS (usually creates a proxy)
        const javaScriptInstance = conversion.javaToJavaScript(javaInstance);
        // Call the Java constructor method.
        javaConstructorMethod(javaScriptInstance, ...argumentsList);
        return javaScriptInstance;
    }
}


/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * WasmGC-backend specific implementation of conversion code.
 *
 * In the WasmGC backend, all Java values are originally Wasm values, with
 * objects being references to Wasm structs. How those values are represented
 * in Java is governed by the "WebAssembly JavaScript Interface".
 * Java Objects are represented as opaque JS objects, these objects do not have
 * any properties of their own nor are they extensible (though identity with
 * regards to the === operator is preserved), only when passing them back to
 * Wasm code can they be manipulated.
 * Java long values are represented as JS BigInt values.
 *
 * Unlike the JS backend, Java code compiled to WasmGC cannot directly be passed
 * JS objects as arguments due to Wasm's type safety. Instead, JS values are
 * first wrapped in WasmExtern, a custom Java class with some special handling
 * to have it store and externref value in one of its fields.
 *
 * To support JSValue instances, there are Java factory methods for each
 * subclass exported under convert.create.*, which create a new instance of the
 * type and associate it with the given JavaScript value.
 * Instead of having the JS code store the JS native value directly in the
 * WasmGC object (which won't work because they are immutable on the JS side),
 * the JS value is first wrapped in a WasmExtern, and then passed to Java, where
 * that WasmExtern value is stored in a hidden field of the JSValue instance.
 */
class WasmGCConversion extends Conversion {
    constructor() {
        super();
        this.proxyHandlers = new WeakMap();
    }

    #wrapExtern(jsObj) {
        return getExport("extern.wrap")(jsObj);
    }

    #unwrapExtern(javaObj) {
        return getExport("extern.unwrap")(javaObj);
    }

    handleJSError(jsError) {
        if (jsError instanceof WebAssembly.Exception) {
            // Wasm exceptions can be rethrown as-is. They will be caught in Wasm code
            throw jsError;
        } else {
            // Have Java code wrap the JS error in a Java JSError instance and throw it.
            getExport("convert.throwjserror")(this.javaScriptToJava(jsError));
        }
    }

    extractJavaScriptNumber(jldouble) {
        return getExport("unbox.double")(jldouble);
    }

    extractJavaScriptString(jlstring) {
        return charArrayToString(proxyCharArray(getExport("string.tochars")(jlstring)));
    }

    extractJavaScriptArray(jarray) {
        const length = getExport("array.length")(jarray);
        const jsarray = new Array(length);
        for (let i = 0; i < length; i++) {
            jsarray[i] = this.javaToJavaScript(getExport("array.object.read")(jarray, i));
        }
        return jsarray;
    }

    createJavaBoolean(x) {
        return getExport("box.boolean")(x);
    }

    createJavaByte(x) {
        return getExport("box.byte")(x);
    }

    createJavaShort(x) {
        return getExport("box.short")(x);
    }

    createJavaCharacter(x) {
        return getExport("box.char")(x);
    }

    createJavaInteger(x) {
        return getExport("box.int")(x);
    }

    createJavaFloat(x) {
        return getExport("box.float")(x);
    }

    createJavaLong(x) {
        return getExport("box.long")(x);
    }

    createJavaDouble(x) {
        return getExport("box.double")(x);
    }

    getHubKindOrdinal(hub) {
        return getExport("class.getkindordinal")(hub);
    }

    getBoxedHub(jlClass) {
        return getExport("class.getboxedhub")(jlClass);
    }

    unboxBoolean(jlBoolean) {
        return getExport("unbox.boolean")(jlBoolean);
    }

    unboxByte(jlByte) {
        return getExport("unbox.byte")(jlByte);
    }

    unboxShort(jlShort) {
        return getExport("unbox.short")(jlShort);
    }

    unboxChar(jlChar) {
        return getExport("unbox.char")(jlChar);
    }

    unboxInt(jlInt) {
        return getExport("unbox.int")(jlInt);
    }

    unboxFloat(jlFloat) {
        return getExport("unbox.float")(jlFloat);
    }

    unboxLong(jlLong) {
        return getExport("unbox.long")(jlLong);
    }

    unboxDouble(jlDouble) {
        return getExport("unbox.double")(jlDouble);
    }

    createJSUndefined() {
        return getExport("convert.create.jsundefined")();
    }

    createJSBoolean(boolean) {
        return getExport("convert.create.jsboolean")(this.#wrapExtern(boolean));
    }

    createJSNumber(number) {
        return getExport("convert.create.jsnumber")(this.#wrapExtern(number));
    }

    createJSBigInt(bigint) {
        return getExport("convert.create.jsbigint")(this.#wrapExtern(bigint));
    }

    createJSString(string) {
        return getExport("convert.create.jsstring")(this.#wrapExtern(string));
    }

    createJSSymbol(symbol) {
        return getExport("convert.create.jssymbol")(this.#wrapExtern(symbol));
    }

    createJSObject(obj) {
        return getExport("convert.create.jsobject")(this.#wrapExtern(obj));
    }

    isInternalJavaObject(obj) {
        return getExport("extern.isjavaobject")(obj);
    }

    isPrimitiveHub(hub) {
        return getExport("class.isprimitive")(hub);
    }

    isJavaLangString(obj) {
        return getExport("convert.isjavalangstring")(obj);
    }

    isJavaLangClass(obj) {
        return getExport("convert.isjavalangclass")(obj);
    }

    isInstance(obj, hub) {
        return getExport("object.isinstance")(obj, hub);
    }

    getOrCreateProxyHandler(clazz) {
        if (!this.proxyHandlers.has(clazz)) {
            this.proxyHandlers.set(clazz, new WasmGCProxyHandler(clazz));
        }
        return this.proxyHandlers.get(clazz);
    }

    _getProxyHandlerArg(obj) {
        return getExport("object.getclass")(obj);
    }

    javaToJavaScript(x) {
        let effectiveJavaObject = x;

        /*
         * When catching exceptions in JavaScript, exceptions thrown from Java
         * aren't caught as Java objects, but as WebAssembly.Exception objects.
         * Instead of having to do special handling whenever we catch an
         * exception in JS, converting to JavaScript first unwraps the original
         * Java Throwable before converting.
         */
        if (x instanceof WebAssembly.Exception && x.is(getExport("tag.throwable"))) {
            effectiveJavaObject = x.getArg(getExport("tag.throwable"), 0);
        }

        return this.#unwrapExtern(getExport("convert.javatojavascript")(effectiveJavaObject));
    }

    throwClassCastExceptionImpl(javaObject, tpeNameJavaString) {
        getExport("convert.throwClassCastException")(javaObject, tpeNameJavaString);
    }

    coerceJavaProxyToJavaScriptType(proxyHandler, proxy, tpe) {
        const o = proxy[runtime.symbol.javaNative];
        switch (tpe) {
            case "boolean":
                // Due to Java booleans being numbers, the double-negation is necessary.
                return !!this.#unwrapExtern(getExport("convert.coerce.boolean")(o));
            case "number":
                return this.#unwrapExtern(getExport("convert.coerce.number")(o));
            case "bigint":
                const bs = this.#unwrapExtern(getExport("convert.coerce.bigint")(o));
                return BigInt(bs);
            case "string":
                return this.#unwrapExtern(getExport("convert.coerce.string")(o));
            case "object":
                return this.#unwrapExtern(getExport("convert.coerce.object")(o));
            case "function":
                const sam = proxyHandler._getSingleAbstractMethod(proxy);
                if (sam !== undefined) {
                    return (...args) => proxyHandler._applyWithObject(proxy, args);
                }
                this.throwClassCastException(o, tpe);
            case Uint8Array:
            case Int8Array:
            case Uint16Array:
            case Int16Array:
            case Int32Array:
            case Float32Array:
            case BigInt64Array:
            case Float64Array:
                // TODO GR-60603 Support array coercion
                throw new Error("Coercion to arrays is not supported yet");
            default:
                this.throwClassCastException(o, tpe);
        }
    }
}

const METADATA_PREFIX = "META.";
const SAM_PREFIX = "SAM.";
const METADATA_SEPARATOR = " ";

class WasmGCProxyHandler extends ProxyHandler {
    #classMetadata = null;

    constructor(clazz) {
        super();
        this.clazz = clazz;
    }

    #lookupClass(name) {
        const clazz = getExport("conversion.classfromencoding")(toJavaString(name));
        if (!clazz) {
            throw new Error("Failed to lookup class " + name);
        }

        return clazz;
    }

    _getClassMetadata() {
        if (!this.#classMetadata) {
            this.#classMetadata = new ClassMetadata({}, this.#extractSingleAbstractMethod(), this.#createMethodTable());
        }
        return this.#classMetadata;
    }

    #decodeMetadata(exports, name, prefix) {
        if (name.startsWith(prefix)) {
            const parts = name.slice(prefix.length).split(METADATA_SEPARATOR);
            if (parts.length < 3) {
                throw new Error("Malformed metadata: " + name);
            }
            const classId = parts[0];

            if (this.#lookupClass(classId) == this.clazz) {
                const methodName = parts[1];
                const returnTypeId = parts[2];
                const argTypeIds = parts.slice(3);

                return [
                    methodName,
                    mmeta(
                        exports[name],
                        this.#lookupClass(returnTypeId),
                        ...argTypeIds.map((i) => this.#lookupClass(i))
                    ),
                ];
            }
        }

        return undefined;
    }

    #extractSingleAbstractMethod() {
        const exports = getExports();

        for (const name in exports) {
            const meta = this.#decodeMetadata(exports, name, SAM_PREFIX);
            if (meta !== undefined) {
                return meta[1];
            }
        }

        return undefined;
    }

    #createMethodTable() {
        const exports = getExports();
        const methodTable = {};

        for (const name in exports) {
            const meta = this.#decodeMetadata(exports, name, METADATA_PREFIX);
            if (meta !== undefined) {
                let methodName = meta[0];

                if (methodName === "<init>") {
                    methodName = runtime.symbol.ctor;
                }

                if (!methodTable.hasOwnProperty(methodName)) {
                    methodTable[methodName] = [];
                }

                methodTable[methodName].push(meta[1]);
            }
        }

        return methodTable;
    }

    _getClassName() {
        return conversion.extractJavaScriptString(getExport("class.getname")(this.clazz));
    }

    _linkMethodPrototype() {
        // Link the prototype chain of the superclass' proxy handler, to include super methods.
        if (!getExport("class.isjavalangobject")(this.clazz)) {
            const parentClass = getExport("class.superclass")(this.clazz);
            const parentProxyHandler = conversion.getOrCreateProxyHandler(parentClass);
            Object.setPrototypeOf(this._getMethods(), parentProxyHandler._getMethods());
        }
    }

    _createInstance(hub) {
        return getExport("unsafe.create")(hub);
    }
}

const conversion = new WasmGCConversion();
const createVM = function(vmArgs, data) {
runtime.data = data;
wasmRun(vmArgs);

return vm;

};
GraalVM.Config = Config;
/** @suppress {checkVars,duplicate} */ GraalVM.run = async function (vmArgs, config = new GraalVM.Config()) {
   let data = new Data(config);
   for (let libname in config.libraries) {
       const content = await runtime.fetchText(config.libraries[libname]);
       data.libraries[libname] = content;
   }
data.wasm = await wasmInstantiate(config, vmArgs);
   let vm = createVM(vmArgs, data);
   return vm;
}
})();

})();

})();

(function() {
/*
 * Copyright (c) 2025, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * Try to load commandline arguments for various JS runtimes.
 */
function load_cmd_args() {
    if (typeof process === "object" && "argv" in process) {
        // nodejs
        return process.argv.slice(2);
    } else if (typeof scriptArgs == "object") {
        // spidermonkey
        return scriptArgs;
    }

    return ['help'];
}

const config = new GraalVM.Config();
GraalVM.run(load_cmd_args(),config).catch(console.error);
})();