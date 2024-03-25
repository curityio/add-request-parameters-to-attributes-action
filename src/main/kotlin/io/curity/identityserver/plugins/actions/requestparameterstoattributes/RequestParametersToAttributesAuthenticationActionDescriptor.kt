package io.curity.identityserver.plugins.actions.requestparameterstoattributes


import se.curity.identityserver.sdk.plugin.descriptor.AuthenticationActionPluginDescriptor

class RequestParametersToAttributesAuthenticationActionDescriptor: AuthenticationActionPluginDescriptor<RequestParametersToAttributesAuthenticationActionConfig>
{
    override fun getAuthenticationAction() = RequestParametersToAttributesAuthenticationAction::class.java

    override fun getPluginImplementationType() = "requestparameterstoattributes"

    override fun getConfigurationType() = RequestParametersToAttributesAuthenticationActionConfig::class.java;
}
