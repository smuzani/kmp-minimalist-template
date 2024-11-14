interface RandomUserClient {
    suspend fun getRandomUsers(count: Int = 10): RandomUserDto
}