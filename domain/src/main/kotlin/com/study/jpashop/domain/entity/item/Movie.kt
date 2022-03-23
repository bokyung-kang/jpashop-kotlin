package com.study.jpashop.domain.entity.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("M")
class Movie(
    name: String,
    price: Int,
    stockQuantity: Int,
    val director: String,
    val actor: String,
) : Item(name = name, price = price, stockQuantity = stockQuantity) {

    companion object {
        fun create(director: String, actor: String, name: String, price: Int, stockQuantity: Int): Movie {
            return Movie(
                director = director,
                actor = actor,
                name = name,
                price = price,
                stockQuantity = stockQuantity
            )
        }
    }
}