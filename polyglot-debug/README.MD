# GraalVM Java to JavaScript Polyglot Debugging

This sample code helps to demonstrate simplicity of debugging and developing GraalVM Polyglot applications using __VS Code extensions__.

## Prerequisites

1. VS Code has to be installed.
2. Then go to VS Code Extensions activity panel and search for _GraalVM Extension Pack_, install it. The GraalVM Extension Pack will install all required extensions.
3. [Install GraalVM in VS Code](https://www.graalvm.org/latest/tools/vscode/graalvm-extension/#graalvm-installation-wizard): Navigate to Gr activity panel in VS Code and install some of the latest GraalVM __Enterprise Edition__ versions available from the list.
4. Confirm to set this as _Active GraalVM_ for VS Code.
5. Install the `js` component.

## Debugging the project

Working with Polyglot project in VSCode is simple just open this sample.
It will be working out of the box with VS Code GraalVM extensions.

Place breakpoints either in Java or JavaScript code.

To debug the project go to __Run and Debug__ activity or simply press F5.
Choose **Launch Java 8+** as this is the Polyglot debugger provided by GraalVM extensions.

That is, the sample is running under debugger on GraalVM.
