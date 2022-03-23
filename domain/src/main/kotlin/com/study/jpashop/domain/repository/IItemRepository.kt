package com.study.jpashop.domain.repository

import com.study.jpashop.domain.entity.item.Item
import org.springframework.data.jpa.repository.JpaRepository

interface IItemRepository : JpaRepository<Item, Long>