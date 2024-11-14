enum class RandomUserError {
    SERVICE_UNAVAILABLE,
    UNKNOWN_ERROR
}

class RandomUserException(val error: RandomUserError): Exception(
    "An error occurred when fetching random users: $error"
)
