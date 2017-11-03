package com.github.insanusmokrassar.IOCStrategies

import com.github.insanusmokrassar.IOC.core.IOCStrategy
import com.github.insanusmokrassar.IOC.core.ResolveStrategyException
import com.github.insanusmokrassar.IOC.utils.extract
import com.github.insanusmokrassar.IOC.utils.getClass

class SimpleIOCStrategy(private val classPath: String) : IOCStrategy {


    /**
     * Check that [com.github.insanusmokrassar.IOCStrategies.SimpleIOCStrategy#classPath] class can be
     *  resolved
     */
    init {
        getClass<Any>(classPath)//check class availability
    }

    /**
     * @param args Args for using in strategy
     * *
     * @return new instance
     * *
     * @throws ResolveStrategyException Throw when object with this classPath and args can't be instantiated
     */
    @Throws(ResolveStrategyException::class)
    override fun <T : Any> getInstance(vararg args: Any?): T {
        try {
            return extract(classPath, *args)
        } catch (e: ClassNotFoundException) {
            throw ResolveStrategyException("Can't resolve instance with this classPath:\n$classPath", e)
        } catch (e: IllegalArgumentException) {
            throw ResolveStrategyException("Can't resolve instance with this args:\n${args.contentToString()}", e)
        }

    }
}
