/**
 * @package Showcase-AOP-Micronaut
 *
 * @file Todo resource
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import dev.unexist.showcase.todo.domain.todo.Todo;
import dev.unexist.showcase.todo.domain.todo.TodoBase;
import dev.unexist.showcase.todo.domain.todo.TodoService;
import dev.unexist.showcase.todo.infrastructure.interceptor.LogTime;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.uri.UriBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@LogTime
@Controller("/todo")
public class TodoResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoResource.class);

    @Inject
    TodoService todoService;

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Create new todo")
    @Tag(name = "Todo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Todo created"),
            @ApiResponse(responseCode = "406", description = "Bad data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public HttpResponse<?> create(TodoBase base) {
        HttpResponse<?> response;

        int newId = this.todoService.create(base);

        LOGGER.info("todo={}", base);

        if (-1 != newId) {
            URI uri = UriBuilder.of("/todo")
                    .path(Integer.toString(newId))
                    .build();

            response = HttpResponse.created(uri);
        } else {
            response = HttpResponse.status(HttpStatus.NOT_ACCEPTABLE);
        }

        return response;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all todos")
    @Tag(name = "Todo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of todo", content =
                @Content(array = @ArraySchema(schema = @Schema(implementation = Todo.class)))),
            @ApiResponse(responseCode = "204", description = "Nothing found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public HttpResponse<?> getAll() {
        List<Todo> todoList = this.todoService.getAll();

        HttpResponse<?> response;

        if (todoList.isEmpty()) {
            response = HttpResponse.noContent();
        } else {
            response = HttpResponse.ok(todoList);
        }

        return response;
    }

    @Get(uri = "{id}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get todo by id")
    @Tag(name = "Todo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo found", content =
                @Content(schema = @Schema(implementation = Todo.class))),
            @ApiResponse(responseCode = "404", description = "Todo not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public HttpResponse<?> findById(@Parameter("id") int id) {
        Optional<Todo> result = this.todoService.findById(id);

        HttpResponse<?> response;

        if (result.isPresent()) {
            response = HttpResponse.ok(result.get());
        } else {
            response = HttpResponse.status(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @Put(uri = "{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Update todo by id")
    @Tag(name = "Todo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Todo updated"),
            @ApiResponse(responseCode = "404", description = "Todo not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public HttpResponse<?> update(@Parameter("id") int id, TodoBase base) {
        HttpResponse<?> response;

        if (this.todoService.update(id, base)) {
            response = HttpResponse.noContent();
        } else {
            response = HttpResponse.status(HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @Delete(uri = "{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete todo by id")
    @Tag(name = "Todo")
    public HttpResponse<?> delete(@Parameter("id") int id) {
        HttpResponse<?> response;

        if (this.todoService.delete(id)) {
            response = HttpResponse.noContent();
        } else {
            response = HttpResponse.status(HttpStatus.NOT_FOUND);
        }

        return response;
    }
}