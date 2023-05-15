package com.app.cashierstation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app.cashierstation.ui.theme.User
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import io.swagger.client.ApiClient
import io.swagger.client.api.MyApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var account: Auth0
    private var appJustLaunched = true
    private var userIsAuthenticated = false
    private var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        account = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )

        buttonLogin = findViewById(R.id.rectangle_70)
        buttonLogin.setOnClickListener { login() }

        buttonLogin = findViewById(R.id.rectangle_70)
        buttonLogin.setOnClickListener { login() }

        val apiClient = ApiClient()
        apiClient.basePath = "https://csbackend.fly.dev/swagger/index.html"

        val sessionToken = "your_session_token"

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionToken))
            .build()

        apiClient.httpClient = client

        val api = MyApi(apiClient)
    }

    private fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(resources.getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    // The user either pressed the “Cancel” button
                    // on the Universal Login screen or something
                    // unusual happened.
                }

                override fun onSuccess(credentials: Credentials) {
                    val idToken = credentials.idToken
                    val accessToken = credentials.accessToken
                    // The user successfully logged in.
                    user = User(accessToken)
                    Log.d("MainActivity", "Access token: $accessToken")
                    user = User(idToken)

                    userIsAuthenticated = true
                    Log.d("MainActivity", "Bisa Anjay")
                }
            })
    }

    private fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    // For some reason, logout failed.
                }

                override fun onSuccess(payload: Void?) {
                    // The user successfully logged out.
                    user = User()
                    userIsAuthenticated = false
                }

            })
    }

    class AuthInterceptor(private val authToken: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            return chain.proceed(request)
        }
    }

}
