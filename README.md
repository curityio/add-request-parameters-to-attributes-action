# Request Parameters to Attributes Authentication Action

[![Quality](https://img.shields.io/badge/quality-demo-yellow)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This is an action the makes it possible to add parameters in an authenticator request into the Authentication Attributes available in the Action pipeline. 

Note that this action operates on the latest request made, so for it to pick up custom parameters added to a login form it will need to be first in the pipeline. If any other actions triggers additional requests before this action is run, the parameters won't be available to this action.

## Building from source

Ensure that JDK 17 or later is installed, then build the plugin code with this command:

```bash
./gradlew build
```

Next gather the jar from the `build/libs` folder:

```text
identityserver.plugins.actions.*.jar
```

## Prerequisites

Before using this plugin you must be running version 9.0 or later of the Curity Identity Server.


### Deploy the JAR File

Deploy JAR files to your instances of the Curity Identity Server, in a plugins subfolder:

```text
$IDSVR_HOME/usr/share/plugins/request-to-attributes/*.jar
```

### Use the Plugin

In the Admin UI, create an instance of the `Request Parameters To Attributes` to use in your authenticators.

### Configuration

There's two options available for configuration.
 - Parameter Names.
 This is a list of strings with the names of the parameters to pick up from the request. The corresponding attribute will have the same name
 - Attribute Location
 The location to put the new attributes. Subject, Context or Action Attributes.

## Further Information

Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
