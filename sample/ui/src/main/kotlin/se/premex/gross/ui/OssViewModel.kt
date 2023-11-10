package se.premex.gross.ui

import se.premex.gross.core.Artifact

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val errorMessage: String) : State<T>()
}

data class OssViewState(val viewState: State<List<Artifact>> = State.Loading())

data class ViewArtifact(
    val title: String,
    val licenses: List<License>,
)

data class License(
    val title: String,
    val url: String,
)
