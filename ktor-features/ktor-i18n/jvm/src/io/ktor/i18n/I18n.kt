/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.i18n

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import java.nio.charset.*

class I18n(configuration: Configuration) {

    private val defaultLanguage = configuration.defaultLanguage

    private val encoding = configuration.encoding

    class Configuration {
        var defaultLanguage: String = "pt_BR"
        var encoding: Charset = StandardCharsets.UTF_8
    }

    private fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        val acceptedLanguage = context.call.request.acceptLanguage() ?: defaultLanguage
        context.call.request.pipeline.attributes.put(acceptedLanguageKey, acceptedLanguage)
        context.call.request.pipeline.attributes.put(encodingKey, encoding)
    }

    companion object Feature : ApplicationFeature<Application, Configuration, I18n> {
        val acceptedLanguageKey = AttributeKey<String>("acceptedLanguage")
        val encodingKey = AttributeKey<Charset>("Charset")

        override val key = AttributeKey<I18n>("I18n")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): I18n {
            val configuration = Configuration().apply(configure)
            val feature = I18n(configuration)

            pipeline.intercept(ApplicationCallPipeline.Call) {
                feature.intercept(this)
            }

            return feature
        }
    }
}
