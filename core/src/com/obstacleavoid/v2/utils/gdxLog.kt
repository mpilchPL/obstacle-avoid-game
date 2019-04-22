package com.obstacleavoid.v2.utils

import com.badlogic.gdx.utils.Logger


//fun <T: Any> logger (clazz: Class<T>): Logger = Logger(clazz.simpleName, Logger.DEBUG)
inline fun <reified T: Any> logger(): Logger = Logger(T::class.java.simpleName, Logger.DEBUG)
