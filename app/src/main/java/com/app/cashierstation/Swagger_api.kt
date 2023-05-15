package com.app.cashierstation

class swagger_api {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create ApiClient and set base path
        val apiClient = ApiClient()
        apiClient.basePath = "https://api.example.com/"

        // Create MyApi object using ApiClient
        val api = MyApi(apiClient)

        // Use the api object to make API calls
        // ...
    }



}