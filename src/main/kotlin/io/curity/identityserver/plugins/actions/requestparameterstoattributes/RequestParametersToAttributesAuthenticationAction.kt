package io.curity.identityserver.plugins.actions.requestparameterstoattributes

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.curity.identityserver.sdk.attribute.AuthenticationAttributes
import se.curity.identityserver.sdk.attribute.MapAttributeValue
import se.curity.identityserver.sdk.attribute.SubjectAttributes
import se.curity.identityserver.sdk.authenticationaction.AuthenticationAction
import se.curity.identityserver.sdk.authenticationaction.AuthenticationActionContext
import se.curity.identityserver.sdk.authenticationaction.AuthenticationActionResult
import se.curity.identityserver.sdk.web.Request

class RequestParametersToAttributesAuthenticationAction(private val configuration: RequestParametersToAttributesAuthenticationActionConfig,
                                                        private val request: Request): AuthenticationAction
{
    companion object {
        var logger: Logger = LoggerFactory.getLogger(RequestParametersToAttributesAuthenticationAction::class.java)
    }
    override fun apply(context: AuthenticationActionContext): AuthenticationActionResult
    {
        logger.debug("Parameter names in request: " + request.parameterNames)
        val parameterAttributes = mutableMapOf<String, String>()
        configuration.requestParameterNames().forEach { parameterName ->
            val parameters = request.getParameterValues(parameterName)

            if (parameters.size == 1) {
                logger.debug("Found $parameterName in request, adding to the attributes")
                parameterAttributes[parameterName] = parameters.first()
            } else if (parameters.size > 1) {
                logger.debug("Multiple parameters with name $parameterName, only adding first to attributes")
                parameterAttributes[parameterName] = parameters.first()
            } else {
                logger.debug("$parameterName not found")
            }
        }

        val subjectAttributes = context.authenticationAttributes.subjectAttributes.with(MapAttributeValue.of(parameterAttributes))
        val authenticationAttributes = AuthenticationAttributes.of(SubjectAttributes.of(subjectAttributes),
            context.authenticationAttributes.contextAttributes)
        return AuthenticationActionResult.successfulResult(authenticationAttributes)
    }
}
