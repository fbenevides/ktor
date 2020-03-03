/*
 * Copyright 2014-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.tests.i18n

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.routing.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.i18n.*
import io.ktor.response.*

class TranslationTest {

    @Test
    fun testTranslationToLanguageSpecifiedInAcceptLanguageHeader() = withTestApplication {
        application.install(I18n) {
            encoding = java.nio.charset.StandardCharsets.UTF_8
        }

        application.routing {
            get("/") {
                val valueInPortuguese = i18n("some_key")
                call.respond(OK, valueInPortuguese)
            }
        }

        handleRequest(HttpMethod.Get, "/") {
            addHeader(HttpHeaders.AcceptLanguage, "pt-BR")
        }.response.let { response ->
            assertEquals(OK, response.status())
            assertNotNull(response.content)

            val contentAsString = response.content!!
            assertEquals("Portuguese Key", contentAsString)
        }
    }

    @Test
    fun testTranslationToDefaultLanguage() = withTestApplication {
        application.install(I18n) {
            defaultLanguage = "en-US"
        }

        application.routing {
            get("/") {
                val valueInPortuguese = i18n("some_key")
                call.respond(OK, valueInPortuguese)
            }
        }

        handleRequest(HttpMethod.Get, "/").response.let { response ->
            assertEquals(OK, response.status())
            assertNotNull(response.content)

            val contentAsString = response.content!!
            assertEquals("English Key", contentAsString)
        }
    }

    @Test
    fun testDefaultBundleWhenDefaultLanguageIsNotConfigured() = withTestApplication {
        application.install(I18n)

        application.routing {
            get("/") {
                val valueInPortuguese = i18n("some_key")
                call.respond(OK, valueInPortuguese)
            }
        }

        handleRequest(HttpMethod.Get, "/").response.let { response ->
            assertEquals(OK, response.status())
            assertNotNull(response.content)

            val contentAsString = response.content!!
            assertEquals("Default key", contentAsString)
        }
    }

}



