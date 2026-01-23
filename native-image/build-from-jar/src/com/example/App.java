/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

  package com.example;

  public class App {

      public static void main(String[] args) {
          String str = "Native Image is awesome";
          String reversed = reverseString(str);
          System.out.println("The reversed string is: " + reversed);
      }

      public static String reverseString(String str) {
          if (str.isEmpty())
              return str;
          return reverseString(str.substring(1)) + str.charAt(0);
      }
  }