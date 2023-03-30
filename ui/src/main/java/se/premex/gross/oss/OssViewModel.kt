package se.premex.gross.oss

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val failed: String) : State<T>()
}

data class OssViewState(
    val loadingState: State<List<ViewData>> = State.Loading(),
)

data class ViewData(
    val title: String,
    val licenses: List<License>,
)

data class License(
    val title: String,
    val url: String,
)

sealed class OssViewEffect {
    data class Loaded(val viewData: List<ViewData>) : OssViewEffect()
}

