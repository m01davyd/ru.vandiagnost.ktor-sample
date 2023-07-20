
class Config {
    companion object {
        const val serverDomain = "127.0.0.1"
        const val serverPort = 8000
        const val serverApi = "1"
        const val serverUrl = "http://$serverDomain:$serverPort/"
       // const val pathPrefix = "api$serverApi/"

        const val pPath = "p/"
        const val pURL = "$serverUrl$pPath"
        const val usersPath = "/users"
        const val teachPath="/teach"
        const val uURL = "$serverUrl$usersPath"
    }
}