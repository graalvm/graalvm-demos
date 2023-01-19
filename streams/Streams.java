/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;
import java.util.Random;

public class Streams {

	static final double EMPLOYMENT_RATIO = 0.5;
	static final int MAX_AGE = 100;
	static final int MAX_SALARY = 200_000;

	public static void main(String[] args) {

		int iterations;
		int dataLength;
		try {
			iterations = Integer.valueOf(args[0]);
			dataLength = Integer.valueOf(args[1]);
		} catch (Throwable ex) {
			System.out.println("expected 2 integer arguments: number of iterations, length of data array");
			return;
		}

		/* Create data set with a deterministic random seed. */
		Random random = new Random(42);
		Person[] persons = new Person[dataLength];
		for (int i = 0; i < dataLength; i++) {
			persons[i] = new Person(
					random.nextDouble() >= EMPLOYMENT_RATIO ? Employment.EMPLOYED : Employment.UNEMPLOYED,
					random.nextInt(MAX_SALARY),
					random.nextInt(MAX_AGE));
		}

		long totalTime = 0;
		for (int i = 1; i <= 20; i++) {
			long startTime = System.currentTimeMillis();

			long checksum = benchmark(iterations, persons);

			long iterationTime = System.currentTimeMillis() - startTime;
			totalTime += iterationTime;
			System.out.println("Iteration " + i + " finished in " + iterationTime + " milliseconds with checksum " + Long.toHexString(checksum));
		}
		System.out.println("TOTAL time: " + totalTime);
	}

	static long benchmark(int iterations, Person[] persons) {
		long checksum = 1;
		for (int i = 0; i < iterations; ++i) {
			double result = getValue(persons);

			checksum = checksum * 31 + (long) result;
		}
		return checksum;
	}

	/*
	 * The actual stream expression that we want to benchmark.
	 */
	public static double getValue(Person[] persons) {
		return Arrays.stream(persons)
				.filter(p -> p.getEmployment() == Employment.EMPLOYED)
				.filter(p -> p.getSalary() > 100_000)
				.mapToInt(Person::getAge)
				.filter(age -> age >= 40).average()
				.getAsDouble();
	}
}

enum Employment {
	EMPLOYED, UNEMPLOYED
}

class Person {
	private final Employment employment;
	private final int age;
	private final int salary;

	public Person(Employment employment, int height, int age) {
		this.employment = employment;
		this.salary = height;
		this.age = age;
	}

	public int getSalary() {
		return salary;
	}

	public int getAge() {
		return age;
	}

	public Employment getEmployment() {
		return employment;
	}
}
