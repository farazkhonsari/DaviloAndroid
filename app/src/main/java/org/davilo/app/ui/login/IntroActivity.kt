package org.davilo.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("constructed")
        setContentView(R.layout.intro_activity)
        val login = findViewById<View>(R.id.login)
        val register = findViewById<View>(R.id.register)
        login.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
        }
        register.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


}

