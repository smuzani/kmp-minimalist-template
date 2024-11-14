import com.template.kmp.util.Resource

class GetRandomUsers(
    private val client: RandomUserClient,
) {
    suspend fun execute(count: Int = 10): Resource<RandomUserDto> {
        return try {
            val users = client.getRandomUsers(count)
            Resource.Success(users)
        } catch (e: RandomUserException) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}