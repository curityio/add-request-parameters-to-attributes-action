/*
 *   Copyright 2024 Curity AB
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.curity.identityserver.plugins.actions.requestparameterstoattributes

import io.curity.identityserver.plugins.actions.requestparameterstoattributes.RequestParametersToAttributesAuthenticationActionConfig.AttributeLocation.ACTION_ATTRIBUTES
import io.curity.identityserver.plugins.actions.requestparameterstoattributes.RequestParametersToAttributesAuthenticationActionConfig.AttributeLocation.CONTEXT_ATTRIBUTES
import io.curity.identityserver.plugins.actions.requestparameterstoattributes.RequestParametersToAttributesAuthenticationActionConfig.AttributeLocation.SUBJECT_ATTRIBUTES
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.curity.identityserver.sdk.attribute.AuthenticationActionAttributes
import se.curity.identityserver.sdk.attribute.AuthenticationAttributes
import se.curity.identityserver.sdk.attribute.ContextAttributes
import se.curity.identityserver.sdk.attribute.MapAttributeValue
import se.curity.identityserver.sdk.attribute.SubjectAttributes
import se.curity.identityserver.sdk.authenticationaction.AuthenticationAction
import se.curity.identityserver.sdk.authenticationaction.AuthenticationActionContext
import se.curity.identityserver.sdk.authenticationaction.AuthenticationActionResult
import se.curity.identityserver.sdk.web.Request

class RequestParametersToAttributesAuthenticationAction(
    private val configuration: RequestParametersToAttributesAuthenticationActionConfig,
    private val request: Request
) : AuthenticationAction {
    companion object {
        var logger: Logger = LoggerFactory.getLogger(RequestParametersToAttributesAuthenticationAction::class.java)
    }

    override fun apply(context: AuthenticationActionContext): AuthenticationActionResult {
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

        var actionAttributes: AuthenticationActionAttributes = AuthenticationActionAttributes.empty()
        val authenticationAttributes = when (configuration.getAttributeLocation()) {
            SUBJECT_ATTRIBUTES -> {
                val subjectAttributes =
                    context.authenticationAttributes.subjectAttributes.with(MapAttributeValue.of(parameterAttributes))
                actionAttributes = context.actionAttributes
                AuthenticationAttributes.of(
                    SubjectAttributes.of(subjectAttributes),
                    context.authenticationAttributes.contextAttributes
                )
            }

            CONTEXT_ATTRIBUTES -> {
                val contexAttributes =
                    context.authenticationAttributes.contextAttributes.with(MapAttributeValue.of(parameterAttributes))
                actionAttributes = context.actionAttributes
                AuthenticationAttributes.of(
                    context.authenticationAttributes.subjectAttributes,
                    ContextAttributes.of(contexAttributes)
                )
            }

            ACTION_ATTRIBUTES -> {
                val attributes = context.actionAttributes.with(MapAttributeValue.of(parameterAttributes))
                actionAttributes = AuthenticationActionAttributes.fromAttributes(attributes)
                context.authenticationAttributes
            }
        }

        return AuthenticationActionResult.successfulResult(authenticationAttributes, actionAttributes)
    }
}
