/*
 * Copyright (c) 2023, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

 public class JFRDemo {

   @Label("Hello World")
   @Description("Build and run a native executable with JFR.")
   static class HelloWorldEvent extends Event {
       @Label("Message")
       String message;
   }

   public static void main(String... args) {
       HelloWorldEvent event = new HelloWorldEvent();
       event.message = "Hello, World!";
       event.commit();
   }
 }