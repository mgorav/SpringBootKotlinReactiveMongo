package com.gm.kotlin.reactive.mongodb

import com.gm.kotlin.reactive.mongodb.domain.Blog
import com.gm.kotlin.reactive.mongodb.repository.BlogRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.gateway.handler.predicate.RoutePredicates.path
import org.springframework.cloud.gateway.route.gateway
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux

@SpringBootApplication
class BlogApp {}

fun main(args: Array<String>) {

    SpringApplicationBuilder()
            .sources(BlogApp::class.java)
            .initializers(beans {
                bean {
                    ApplicationRunner {

                        val customerService = ref<BlogRepository>()

                        val customers: Flux<Blog> = arrayOf(Blog(title = "Blog A", content = "Blog A Content"),
                                Blog(title = "Blog B", content = "Blog B content"),
                                Blog(title = "Blog C", content = "Blog C content"),
                                Blog(title = "Blog D", content = "Blog D content"))
                                .toFlux()
                                .flatMap { customerService.save(it) }

                        customerService
                                .deleteAll()
                                .thenMany(customers)
                                .thenMany(customerService.findAll())
                                .subscribe { println(it) }
                    }
                }
                bean {
                    router {
                        val customerRepository = ref<BlogRepository>()
                        GET("/blogs/{id}") { ServerResponse.ok().body(customerRepository.findById(it.pathVariable("id"))) }
                        GET("/blogs") { ServerResponse.ok().body(customerRepository.findAll()) }
                    }
                }
                bean {
                    gateway {
                        route {
                            id("gmblog")
                            predicate(path("/myblog") or path("/gmblog"))
                            uri("http://architectcorner.blogspot.com/")
                        }
                    }
                }
            })
            .run(*args)
}
