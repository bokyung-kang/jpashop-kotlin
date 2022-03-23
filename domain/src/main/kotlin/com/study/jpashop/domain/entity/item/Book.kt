package com.study.jpashop.domain.entity.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
class Book(
    name: String,
    price: Int,
    stockQuantity: Int,
    val author: String,
    val isbn: String,
) : Item(name = name, price = price, stockQuantity = stockQuantity) {

    companion object {
        fun create(author: String, isbn: String, name: String, price: Int, stockQuantity: Int): Book {
            return Book(
                author = author,
                isbn = isbn,
                name = name,
                price = price,
                stockQuantity = stockQuantity,
            )
        }
    }
}