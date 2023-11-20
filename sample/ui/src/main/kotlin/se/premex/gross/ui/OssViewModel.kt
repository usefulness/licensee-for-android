package se.premex.gross.ui

import io.github.usefulness.licensee.Artifact

sealed class State<out T> {
    data object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val errorMessage: String) : State<T>()
}

typealias OssViewState = State<List<Artifact>>
