package com.abedfattal.foodie.framework.utils

import com.abedfattal.foodie.models.ProcessState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Sometimes we can't control the data type of the [ProcessState], e.g. when have fixed types in remote API response.
 *
 * @param block is only class the process is [ProcessState.Success], since it's the only one that have data.
 */
inline fun <reified Data, T : ProcessState<Data>, R> Flow<T>.transform(
    crossinline block: suspend (Data) -> R
): Flow<ProcessState<R>> {
    return map {
        if (it is ProcessState.Success<*>)
            ProcessState.Success(block(it.data as Data))
        else
            it.transformProcessType<R>()
    }
}

/**
 * Create new flow that only if [ProcessState.Success] will emit values.
 *
 */
inline val <reified T> Flow<ProcessState<T>>.onSuccess: Flow<T?>
    get() = flow<T?> {
        this@onSuccess.collect {
            if (it is ProcessState.Success<*>)
                emit(it.data as? T)
        }
    }
inline val <reified T> Flow<ProcessState<T>>.onFail: Flow<ProcessState.Failed<T>>
    get() = flow {
        this@onFail.collect {
            if (it is ProcessState.Failed<T>)
                emit(it)
        }
    }

fun <T> T.toSuccessProcess(exception: Exception?=null): ProcessState.Success<T> {
    return ProcessState.Success(this, exception)
}

