package com.alanford.carpediem

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.alanford.carpediem.LoginActivity.Constants.CONNECTION_NAME_FACEBOOK
import com.alanford.carpediem.LoginActivity.Constants.CONNECTION_NAME_GOOGLE
import com.alanford.carpediem.LoginActivity.Constants.EXTRA_ACCESS_TOKEN
import com.alanford.carpediem.LoginActivity.Constants.EXTRA_CLEAR_CREDENTIALS
import com.alanford.carpediem.LoginActivity.Constants.EXTRA_ID_TOKEN
import com.alanford.carpediem.LoginActivity.Constants.SCHEME
import com.alanford.carpediem.LoginActivity.Constants.WITH_AUDIENCE
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials


class LoginActivity : AppCompatActivity() {

    private lateinit var auth0: Auth0
    private lateinit var credentialsManager: SecureCredentialsManager

    private lateinit var facebookLoginButton: Button
    private lateinit var googleLoginButton: Button

    object Constants {
        const val EXTRA_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS"
        const val EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN"
        const val EXTRA_ID_TOKEN = "com.auth0.ID_TOKEN"
        const val SCHEME = "demo"
        const val WITH_AUDIENCE = "https://%s/userinfo"
        const val CONNECTION_NAME_FACEBOOK = "facebook"
        const val CONNECTION_NAME_GOOGLE = "google-oauth2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth0 = Auth0(this)
        auth0.isOIDCConformant = true
        credentialsManager = SecureCredentialsManager(this, AuthenticationAPIClient(auth0), SharedPreferencesStorage(this))

        facebookLoginButton = findViewById<Button>(R.id.facebookLoginButton).apply {
            setOnClickListener {
                login(CONNECTION_NAME_FACEBOOK)
            }
        }

        googleLoginButton = findViewById<Button>(R.id.googleLoginButton).apply {
            setOnClickListener {
                login(CONNECTION_NAME_GOOGLE)
            }
        }

        //Check if the activity was launched to log the user out
        if (intent.getBooleanExtra(EXTRA_CLEAR_CREDENTIALS, false)) {
            logout()
            return
        }

        if (credentialsManager.hasValidCredentials()) {
            // Obtain the existing credentials and move to the next activity
            showNextActivity()
        }
    }

    /**
     * Override required when setting up Local Authentication in the Credential Manager
     * Refer to SecureCredentialsManager#requireAuthentication method for more information.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (credentialsManager.checkAuthenticationResult(requestCode, resultCode)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showNextActivity() {
        credentialsManager.getCredentials(object : BaseCallback<Credentials?, CredentialsManagerException?> {
            override fun onSuccess(credentials: Credentials?) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra(EXTRA_ACCESS_TOKEN, credentials?.accessToken)
                intent.putExtra(EXTRA_ID_TOKEN, credentials?.idToken)
                startActivity(intent)
                finish()
            }

            override fun onFailure(error: CredentialsManagerException) {
                //Authentication cancelled by the user. Exit the app
                finish()
            }
        })
    }

    private fun logout() {
        WebAuthProvider.logout(auth0)
            .withScheme(SCHEME)
            .start(this, object : VoidCallback {
                override fun onSuccess(payload: Void?) {
                    credentialsManager.clearCredentials()
                }

                override fun onFailure(error: Auth0Exception) {
                    showNextActivity()
                }
            })
    }

    private fun login(connectionName: String) {
        WebAuthProvider.login(auth0)
            .withScheme(SCHEME)
            .withConnection(connectionName)
            .withAudience(String.format(WITH_AUDIENCE, getString(R.string.com_auth0_domain)))
            .start(this@LoginActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                     dialog.show()
                }

                override fun onFailure(exception: AuthenticationException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onSuccess(credentials: Credentials) {
                    credentialsManager.saveCredentials(credentials)
                    showNextActivity()
                }
            })
    }


}