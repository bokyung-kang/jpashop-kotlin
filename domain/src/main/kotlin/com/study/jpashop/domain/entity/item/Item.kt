package com.study.jpashop.domain.entity.item

import com.study.jpashop.domain.entity.BaseEntity
import com.study.jpashop.domain.exception.ErrorCode
import com.study.jpashop.domain.extensions.validate
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item(
    name: String,
    price: Int,
    stockQuantity: Int,
) : BaseEntity() {

    @Column(nullable = false)
    var name: String? = name
        protected set

    @Column(nullable = false)
    var price: Int = price
        protected set

    @Column(nullable = false)
    var stockQuantity: Int = stockQuantity
        protected set

    // 비즈니스 로직
    /**
     * 재고 추가
     */
    fun addStock(quantity: Int) {
        this.stockQuantity = this.stockQuantity.plus(quantity)
    }

    /**
     * 재고 삭감
     */
    fun removeStock(quantity: Int) {
        val remainStock = this.stockQuantity.minus(quantity)
        validate(remainStock >= 0) { ErrorCode.OUT_OF_STOCK }
        this.stockQuantity = remainStock
    }

    /**
     * 아이템 변경
     */
    fun change(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
    }
}
