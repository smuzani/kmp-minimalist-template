import com.template.kmp.randomuser.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException

class KtorRandomUserClient(
  private val httpClient: HttpClient
) : RandomUserClient {
  override suspend fun getRandomUsers(count: Int): RandomUserDto {
    return try {
      httpClient.get {
        url("${NetworkConstants.BASE_URL}/api")
        parameter("results", count)
      }.body()
    } catch (e: IOException) {
      throw RandomUserException(RandomUserError.SERVICE_UNAVAILABLE)
    } catch (e: Exception) {
      throw RandomUserException(RandomUserError.UNKNOWN_ERROR)
    }
  }
}