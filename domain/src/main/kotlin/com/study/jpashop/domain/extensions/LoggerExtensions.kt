package com.study.jpashop.domain.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *  @author bokyung
 */
inline fun <reified T : Any> T.logger(): Logger = LoggerFactory.getLogger(
    if (T::class.isCompanion) T::class.java.enclosingClass
    else T::class.java
)