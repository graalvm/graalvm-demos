/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

//DEPS org.graalvm.python:python-language:24.1.0
//DEPS org.graalvm.python:python-launcher:24.1.0
//DEPS org.graalvm.python:python-resources:24.1.0
//DEPS org.graalvm.python:python-embedding:24.1.0
//DEPS org.graalvm.python:python-embedding-tools:24.1.0
//PIP qrcode==7.4.2

import org.graalvm.python.embedding.utils.GraalPyResources;

public class qrcode {

    public static void main(String[] args) {
        if (args.length != 1 || args[0].equals("-h") || args[0].equals("--help")) {
            System.out.println("This tool takes only a single argument, the QR code data.");
            return;
        }
        try (var context = GraalPyResources.contextBuilder().option("python.PythonHome", "").build()) {
            QRCodeModule qrcodeModule = context.eval("python", "import qrcode; qrcode").as(QRCodeModule.class);
            IO io = context.eval("python", "import io; io").as(IO.class);

            QRCode qr = qrcodeModule.QRCode();
            qr.add_data(args[0]);

            StringIO f = io.StringIO();
            qr.print_ascii(f);
            String qrCodeString = f.getvalue();

            System.out.println(qrCodeString);
        }
    }

    public interface QRCodeModule {
        QRCode QRCode();
    }

    public interface QRCode {
        void add_data(String text);

        void print_ascii(StringIO out);
    }

    public interface IO {
        StringIO StringIO();
    }

    public interface StringIO {
        String getvalue();
    }
}