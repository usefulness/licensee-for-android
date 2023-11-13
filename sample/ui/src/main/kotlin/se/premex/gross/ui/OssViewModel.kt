package se.premex.gross.ui

import io.github.usefulness.licensee.Artifact

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val errorMessage: String) : State<T>()
}

data class OssViewState(val viewState: State<List<Artifact>> = State.Loading())
