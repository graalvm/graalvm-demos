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
package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;

@MicronautTest 
public class AnagramControllerTest {

    @Inject
    @Client("/")  
    HttpClient client;

    /**
     * Test that the AnagramController returns the prompt
     */
    @Test
    @DisplayName("Return the prompt")
    public void testNoClue() {
        HttpRequest<String> request = HttpRequest.GET("/");  
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Provide an anagram clue", body);
    }
    
    /**
     * Test that the AnagramController returns an expected result
     */
    @Test
    @DisplayName("Return an expected result")
    public void testSolve() {
        HttpRequest<String> request = HttpRequest.GET("/redrum");  
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertTrue(body.contains("murder"), "Failed to return `murder`");
    }
    
    /**
     * Test that the AnagramController throws an exception with too many chars
     */
    @Test
    @DisplayName("Throw an exception with too many chars")
    public void testEmptyString() {
        String char64 = new String(new char[64]).replace('\0', 'z');
        HttpRequest<String> request = HttpRequest.GET(char64);  
        String body = client.toBlocking().retrieve(request);
        
        assertNotNull(body);
        assertTrue(body.equals("Anagram has too many characters"), "Failed to throw an exception");
    }
    
    /**
     * Test that the AnagramController throws an exception with no results
     */
    @Test
    @DisplayName("Throw an exception with no results")
    public void testNoResults() {
        HttpRequest<String> request = HttpRequest.GET("foobarbaz");  
        String body = client.toBlocking().retrieve(request);
        
        assertNotNull(body);
        assertTrue(body.equals("No Results"), "Failed to throw an exception");
    }
}