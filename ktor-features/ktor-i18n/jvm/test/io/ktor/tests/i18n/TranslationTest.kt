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
    fun testTranslationToDefaultLanguage() = withTestApplication {
        application.install(I18n) {
            encoding = java.nio.charset.StandardCharsets.UTF_8
        }

        application.routing {
            get("/") {
                val valueInPortuguese = call.translate("some_key")
                call.respond(OK, valueInPortuguese)
            }
        }

        handleRequest(HttpMethod.Get, "/") {
            addHeader("Accept-Language", "pt-BR")
        }.response.let { response ->
            assertEquals(OK, response.status())
            assertNotNull(response.content)

            val contentAsString = response.content!!
            assertEquals("Portuguese Key", contentAsString)
        }
    }

}
