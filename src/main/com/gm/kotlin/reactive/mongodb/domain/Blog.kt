package com.gm.kotlin.reactive.mongodb.domain

import org.springframework.data.annotation.Id


data class Blog(@Id var id: String? = null, var title: String? = null, var content: String? = null)