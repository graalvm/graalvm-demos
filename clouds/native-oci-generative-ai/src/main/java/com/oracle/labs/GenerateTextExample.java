/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.labs;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.CohereLlmInferenceRequest;
import com.oracle.bmc.generativeaiinference.model.GenerateTextDetails;
import com.oracle.bmc.generativeaiinference.model.OnDemandServingMode;
import com.oracle.bmc.generativeaiinference.requests.GenerateTextRequest;
import com.oracle.bmc.generativeaiinference.responses.GenerateTextResponse;
import com.oracle.bmc.retrier.RetryConfiguration;
/**
 * This class provides an example of how to use OCI Generative AI Service to
 * generate text.
 * <p>
 * The Generative AI Service queried by this example will be assigned:
 * <ul>
 * <li>an endpoint url defined by constant ENDPOINT</li>
 * <li>
 * The configuration file used by service clients will be sourced from the
 * default
 * location (~/.oci/config) and the CONFIG_PROFILE profile will be used.
 * </li>
 * </ul>
 * </p>
 */

public class GenerateTextExample {
  private static final String ENDPOINT = "https://inference.generativeai.us-chicago-1.oci.oraclecloud.com";
  private static final Region REGION = Region.US_CHICAGO_1;
  private static final String CONFIG_LOCATION = "~/.oci/config";
  // TODO: Please update config profile name and use the compartmentId that has
  // policies grant permissions for using Generative AI Service
  private static final String CONFIG_PROFILE = "DEFAULT";
  private static final String COMPARTMENT_ID = "ocid1.compartment.oc1..aaaaaaaaul6555vm7yt6qm7tmsq64yvivkidstxajilzcjv2laaki46rtdzq";

  /**
   * The entry point for the example.
   *
   * @param args Arguments to provide to the example. This example expects no
   *             arguments.
   * @throws java.lang.Exception
   */
  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      throw new IllegalArgumentException(
          "This example expects no argument");
    }
    // Configuring the AuthenticationDetailsProvider. It's assuming there is a
    // default OCI config file
    // "~/.oci/config", and a profile in that config with the name defined in
    // CONFIG_PROFILE variable.
    final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(CONFIG_LOCATION, CONFIG_PROFILE);
    final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

    // Set up Generative AI client with credentials and endpoint
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .readTimeoutMillis(240000)
        .retryConfiguration(RetryConfiguration.NO_RETRY_CONFIGURATION)
        .build();
    final GenerativeAiInferenceClient generativeAiInferenceClient = GenerativeAiInferenceClient.builder()
          .configuration(clientConfiguration)
          .build(provider);
    generativeAiInferenceClient.setEndpoint(ENDPOINT);
    generativeAiInferenceClient.setRegion(REGION);

    // Build generate text request, send, and get response
    CohereLlmInferenceRequest llmInferenceRequest = CohereLlmInferenceRequest.builder()
        .prompt(
            "Generate a job description for a data visualization expert with the following three qualifications only:\n1) At least 5 years of data visualization expert\n2) A great eye for details\n3) Ability to create original visualizations")
        .maxTokens(600)
        .temperature((double) 0.5)
        .frequencyPenalty((double) 1)
        .topP((double) 0.75)
        .isStream(false)
        .isEcho(false)
        .build();

    GenerateTextDetails generateTextDetails = GenerateTextDetails.builder()
        .servingMode(OnDemandServingMode.builder()
            .modelId(
                "ocid1.generativeaimodel.oc1.us-chicago-1.amaaaaaask7dceyafhwal37hxwylnpbcncidimbwteff4xha77n5xz4m7p6a")
            .build())
        .compartmentId(COMPARTMENT_ID)
        .inferenceRequest(llmInferenceRequest)
        .build();
    GenerateTextRequest generateTextRequest = GenerateTextRequest.builder()
        .generateTextDetails(generateTextDetails)
        .build();
    GenerateTextResponse generateTextResponse = generativeAiInferenceClient.generateText(generateTextRequest);
    System.out.println(generateTextResponse.getGenerateTextResult().getInferenceResponse());
  }
}