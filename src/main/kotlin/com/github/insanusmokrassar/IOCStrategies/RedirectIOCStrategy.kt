package com.github.insanusmokrassar.IOCStrategies

import com.github.insanusmokrassar.IOC.core.IOCStrategy
import com.github.insanusmokrassar.IOC.core.ResolveStrategyException
import com.github.insanusmokrassar.IOC.core.getOrCreateIOC

/**
 * Simple realisation of redirect IOCStrategy
 * <pre>{
 *      [
 *          "commonIOC",
 *          "redirectTo...",
 *          "preset param one",|  >
 *          "preset param two",|  > preset presetArgs if input presetArgs is not set
 *          "preset param three"| >
 *      ]
 * }
 * </pre>
 * @param iocName Name of target ioc instance
 * @param targetStrategy Strategy which will used for resolving dependencies
 */
class RedirectIOCStrategy(
        iocName: String,
        private val targetStrategy: String,
        private val presetArgs: Array<Any?>
) : IOCStrategy {
    private val ioc = getOrCreateIOC(iocName)

    /**
     * Your args will upgraded by presets if some are absent
     */
    @Throws(ResolveStrategyException::class)
    override fun <T : Any> getInstance(vararg args: Any?): T {
        val realArgs: Array<Any?> = arrayOf(
                *args,
                *if (args.size < presetArgs.size) {
                    presetArgs.copyOfRange(
                            args.size,
                            presetArgs.size
                    )
                } else {
                    emptyArray()
                }
        )
        return ioc.resolve(targetStrategy, *realArgs)
    }
}

