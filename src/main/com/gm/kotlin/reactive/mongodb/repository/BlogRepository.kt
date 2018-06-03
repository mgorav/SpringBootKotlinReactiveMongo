package com.gm.kotlin.reactive.mongodb.repository

import com.gm.kotlin.reactive.mongodb.domain.Blog
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface BlogRepository : ReactiveMongoRepository<Blog,String>