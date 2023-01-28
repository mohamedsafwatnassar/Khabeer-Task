package com.khabeer.payrollapp.helpers

import kotlinx.coroutines.flow.FlowCollector

class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent() = content
}

class EventObserver<T>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onStopLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : FlowCollector<Event<DataState<T>>> {

    override suspend fun emit(value: Event<DataState<T>>) {
        when (val content = value.peekContent()) {
            is DataState.Success -> {
                content.data?.let(onSuccess)
            }
            is DataState.Error -> {
                value.getContentIfNotHandled()?.let {
                    onError?.let { error ->
                        error(it.message!!)
                    }
                }
            }
            is DataState.Loading -> {
                onLoading?.let { loading ->
                    loading()
                }
            }
            is DataState.StopLoading -> {
                onStopLoading?.let { stopLoading ->
                    stopLoading()
                }
            }
        }
    }
}


