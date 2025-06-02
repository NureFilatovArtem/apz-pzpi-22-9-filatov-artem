object Constants {
    private const val BASE_URL_DEV = "http://10.0.2.2:3000/api/"
    private const val BASE_URL_PROD = "https://api.example.com/api/"
    
    // Change this to switch between environments
    const val BASE_URL = BASE_URL_DEV
    
    // API timeout in milliseconds
    const val API_TIMEOUT = 30000L
}