package oracle;

/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 * Holds the count data - number of files and total size, in Bytes.
 */
class FileCount {
    final long size;
    final long count;

    public FileCount(final long size, final long count) {
        this.count = count;
        this.size = size;
    }

    public long getSize() {
        return this.size;
    }

    public long getCount() {
        return this.count;
    }
}

public class ListDir {
    // Add a logger - don't do this yet :)
    //final static Logger logger = Logger.getLogger(ListDir.class);


    /**
     * Counts the number of files, and their total size, within a folder tree.
     * @param dirName The directory to process, count files within
     * @throws IOException
     */
    public static final FileCount list(final String dirName) throws IOException {
		long[] size = {0};
		long[] count = {0};

        /*
        // Add some logging
        if(logger.isDebugEnabled()){
			logger.debug("Processing : " + dirName);
        }
        */

		try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
			paths.filter(Files::isRegularFile).forEach((Path p) -> {
				File f = p.toFile();

                /*
                // Add some logging
                if(logger.isDebugEnabled()){
                    logger.debug("Processing : " + f.getAbsolutePath());
                }
                */

                size[0] += f.length();
				count[0] += 1;
			});
        }
        //
        return new FileCount(size[0], count[0]);
    }

    /**
     * Converts bytes into something fit for non-robots.
     *
     * @param bytes
     * @return Human readbale string
     */
    public static final String humanReadableByteCountBin(final long bytes) {
        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        return b < 1024L ? bytes + " B"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f KiB", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f MiB", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f GiB", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f TiB", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.1f PiB", (bytes >> 10) / 0x1p40)
                : String.format("%.1f EiB", (bytes >> 20) / 0x1p40);
    }
}
