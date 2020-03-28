package com.bennyhuo.github.common.ext

/**
 * sealed	英[siːld]	美[siːld]
 * 密封类：为父类添加一个sealed修饰符，对可能创建的子类做出严格的限制。
 * sealed修饰符隐含的这个类是一个open类
 * out：表示类型只能输出不能输入
 */
sealed class BooleanExt<out T> {
    // object 定义一个类并同时创建一个实例，单例
    object Otherwise : BooleanExt<Nothing>()
    class WithData<T>(val data: T) : BooleanExt<T>()
}

// inline 内联函数：减少资源开销
inline fun <T> Boolean.yes(block: () -> T) =
        when {
            this -> BooleanExt.WithData(block())
            else -> BooleanExt.Otherwise
        }

inline fun <T> Boolean.no(block: () -> T) =
        when {
            this -> BooleanExt.Otherwise
            else -> BooleanExt.WithData(block())
        }

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
        // when表示式中处理所有sealed类的子类，不需要提供默认分支
        when (this) {
            is BooleanExt.Otherwise -> block()
            is BooleanExt.WithData -> this.data
        }