/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.i18n

import io.ktor.application.*
import io.ktor.util.pipeline.*
import java.util.*

/**
 * Translate a message key to an accepted language specified in HTTP request
 */

fun PipelineContext<Unit, ApplicationCall>.i18n(key: String): String {
    val acceptedLanguage = context.attributes[I18n.acceptedLanguageKey]
    val configuredEncoding = context.attributes[I18n.encodingKey]

    val locale = Locale.forLanguageTag(acceptedLanguage)

    val bundle = ResourceBundle.getBundle("messages/messages", locale)
    val value = String(bundle.getString(key).toByteArray(configuredEncoding))

    application.log.debug("translating to $acceptedLanguage [$locale]: $key=$value")
    return value
}
