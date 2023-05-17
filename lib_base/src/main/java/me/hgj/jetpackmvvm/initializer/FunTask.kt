package me.hgj.jetpackmvvm.initializer

import com.caij.app.startup.Task

/**
 *
 */
abstract class FunTask : Task() {

    override fun dependencies(): MutableList<Class<out Task>> {
        return arrayListOf()
    }

    override fun getTaskName(): String {
        return this::class.java.simpleName
    }
}