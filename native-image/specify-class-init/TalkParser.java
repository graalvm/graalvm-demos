/*
 * Copyright (c) 2024, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TalkParser {
private static final List<Talk> TALKS = new ArrayList<>();
static {
    Scanner s = new Scanner("""
        Asynchronous Programming in Java: Options to Choose from by Venkat Subramaniam
        Anatomy of a Spring Boot App with Clean Architecture by Steve Pember
        Java in the Cloud with GraalVM by Alina Yurenko
        Bootiful Spring Boot 3 by Josh Long
        """);
    while (s.hasNextLine()) {
    TALKS.add(new Talk(s.nextLine()));
    }
    s.close();
}

public static void main(String[] args) {
    System.out.println("Talks loaded using scanner:");
    for (Talk talk : TALKS) {
        System.out.println("- " + talk.name());
        }
    }
}

record Talk (String name) {}