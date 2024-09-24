package org.example;

import org.graalvm.polyglot.io.ByteSequence;

interface IO {
    BytesIO BytesIO();

    interface BytesIO {
        ByteSequence getvalue();
    }
}
