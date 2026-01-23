/*
 * Copyright (c) 2024, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
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