data class RandomUserState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: RandomUserError? = null
)