/*
 * Copyright © 2023, Oracle and/or its affiliates.
 * Released under the Universal Permissive License v1.0 as shown at https://oss.oracle.com/licenses/upl/.
 */

package com.example.benchmarks.jibber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class DemoApplication {

    @Autowired
    Jabberwocky j;

	public static void main(String[] args) {
	    SpringApplication.run(DemoApplication.class, args);
	}

    @RequestMapping(method = RequestMethod.GET, path = "/jibber")
    ResponseEntity<String> jibber() {
	    return ResponseEntity.ok(j.generate());
    }
}