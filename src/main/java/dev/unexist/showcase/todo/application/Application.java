/**
 * @package Showcase
 *
 * @file Main application
 * @copyright 2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id\$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "Todo",
                version = "0.1",
                description = "Todo API",
                license = @License(name = "Apache 2.0", url = "https://unexist.dev"),
                contact = @Contact(url = "https://unexist.dev", name = "Christoph Kappel", email = "christoph@unexist.dev")
        ),
        tags = {
                @Tag(name = "Todo", description = "desc", externalDocs = @ExternalDocumentation(description = "docs desc")),
        },
        externalDocs = @ExternalDocumentation(description = "definition docs desc"),
        security = {
                @SecurityRequirement(name = "req 1", scopes = {"a", "b"}),
                @SecurityRequirement(name = "req 2", scopes = {"b", "c"})
        },
        servers = {
                @Server(
                        description = "server 1",
                        url = "http://foo",
                        variables = {
                                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                                @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
                        })
        }
)
public class Application {
    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        Micronaut.build(args)
                .packages("dev.unexist.showcase.todo")
                .mainClass(Application.class)
                .start();
    }
}
