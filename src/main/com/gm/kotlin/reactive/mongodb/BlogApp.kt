package com.gm.kotlin.reactive.mongodb

import com.gm.kotlin.reactive.mongodb.domain.Blog
import com.gm.kotlin.reactive.mongodb.repository.BlogRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.gateway.handler.predicate.RoutePredicates.path
import org.springframework.cloud.gateway.route.gateway
import org.springframework.context.support.beans
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux

@SpringBootApplication
@EnableWebFlux
class BlogApp {}

fun main(args: Array<String>) {

    SpringApplicationBuilder()
            .sources(BlogApp::class.java)
            .initializers(beans {
                bean {
                    ApplicationRunner {

                        val blogRepository = ref<BlogRepository>()

                        val customers: Flux<Blog> = arrayOf(Blog(title = "Blog A", content = "Blog A Content"),
                                Blog(title = "Blog B", content = "Blog B content"),
                                Blog(title = "Blog C", content = "Blog C content"),
                                Blog(title = "Blog D", content = "Blog D content"))
                                .toFlux()
                                .flatMap { blogRepository.save(it) }

                        blogRepository
                                .deleteAll()
                                .thenMany(customers)
                                .thenMany(blogRepository.findAll())
                                .subscribe { println(it) }
                    }
                }
                bean {
                    router {
                        val blogRepository = ref<BlogRepository>()
                        GET("/blogs/{id}") { ok().body(blogRepository.findById(it.pathVariable("id"))) }
                        GET("/blogs") { ok().body(blogRepository.findAll()) }
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
