package io.curity.identityserver.plugins.actions.requestparameterstoattributes


import se.curity.identityserver.sdk.config.Configuration
import se.curity.identityserver.sdk.config.annotation.Description

interface RequestParametersToAttributesAuthenticationActionConfig : Configuration {
    @Description("The parameter names to find from the request")
    fun requestParameterNames(): List<String>

}
