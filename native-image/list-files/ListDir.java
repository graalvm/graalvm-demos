/*
 * Copyright (c) 2014, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ListDir {
	public static void main(String[] args) throws java.io.IOException {

		String root = ".";
		if(args.length > 0) {
			root = args[0];
		}
		System.out.println("Walking path: " + Paths.get(root));

		long[] size = {0};
		long[] count = {0};

		try (Stream<Path> paths = Files.walk(Paths.get(root))) {
			paths.filter(Files::isRegularFile).forEach((Path p) -> {
				File f = p.toFile();
				size[0] += f.length();
				count[0] += 1;
			});
		}

		System.out.println("Total: " + count[0] + " files, total size = " + size[0] + " bytes");
	}
}