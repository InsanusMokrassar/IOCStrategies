package com.github.insanusmokrassar.IOCStrategies

import com.github.insanusmokrassar.IOC.core.IOCStrategy
import com.github.insanusmokrassar.IOC.core.ResolveStrategyException
import com.github.insanusmokrassar.IObjectK.interfaces.IObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FixedExecutorServiceStrategy(settings: IObject<Any>) : IOCStrategy {

    private var executorServices: MutableMap<String, ExecutorService> = HashMap()

    private var defaultThreadsCount: Int = 1

    /**
     * Await next config iobject:
     * <pre>
     *     {
     *         "default" : number,//this number will used for keys which will not set in settings
     *         "taskName" : number,
     *         "taskName2" : number,
     *         "taskName3" : number,
     *         ...
     *     }
     * </pre>
     */
     init {
        val keys = settings.keys()
        for (key in keys) {
            if (key == "default") {
                defaultThreadsCount = settings.get(key)
                continue
            }
            executorServices.put(key, Executors.newFixedThreadPool(settings.get<Int>(key)))
        }
    }

    @Throws(ResolveStrategyException::class)
    override fun <T : Any> getInstance(vararg args: Any?): T {
        val targetTaskName = args[0] as String
        if (executorServices.containsKey(targetTaskName)) {
            return executorServices[targetTaskName]!! as T
        }
        val executorService = Executors.newFixedThreadPool(defaultThreadsCount)
        executorServices.put(targetTaskName, executorService)
        return executorService as T
    }
}
