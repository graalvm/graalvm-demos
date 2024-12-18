# GraalVM Demos

This repository contains a collection of example applications that highlight key features and best practices for working with GraalVM technologies.

## Repository Structure

The demos are organized into categories for easier navigation

### Native Image

Demos showcasing the capabilities of GraalVM Native Image, including performance optimization and configuration tips, such as:

- _build_: Covers building native images, including configurations and setup steps for various use cases.
- _benchmark_: Provides performance measurement demos for Native Image.
- _containerize_: Focuses on containerizing native Java applications and following best practices.
- _monitor_: Demonstrates how to monitor native applications using observability and diagnostics tools.
- _microservices_: Provides examples of building microservices ahead of time using frameworks such as Micronaut and Spring Boot.
- _optimize_: Includes Native Image optimization examples for different criteria (runtime and performance tuning, file size, build time, and more).

### Compiler

Demos designed to test and showcase the capabilities of the Graal Just-In-Time (JIT) compiler.
These examples focus on evaluating the compiler's performance, including its optimizations for modern Java workloads.

### Clouds

Demos showcasing the building and deployment of native applications to Oracle Cloud Infrastructure (OCI), AWS, and Google Cloud.

### Archive

Contains legacy or blog-related demos, as well as examples involving polyglot capabilities.

## Get Started

To get started, clone this repository and navigate to the relevant demo directory:
```bash
git clone https://github.com/graalvm/graalvm-demos.git
``` 
```bash
cd graalvm-demos
```

You will find instructions for running a particular demo in the corresponding README.md file. Some demos may also redirect you to a specific guide on the [GraalVM website](https://www.graalvm.org/latest/guides/).

## Compatibility

The demos are standard Java applications and benchmarks, compatible with any virtual machine capable of running Java.
They are [tested against the latest GraalVM LTS release and the GraalVM Early Access build using GitHub Actions](https://github.com/graalvm/graalvm-demos/tree/master/.github/workflows), with the exception of archived examples.
If you come across an issue, please submit it [here](https://github.com/graalvm/graalvm-demos/issues).

## License

Unless specified otherwise, all code in this repository is licensed under the [Universal Permissive License (UPL)](http://opensource.org/licenses/UPL).