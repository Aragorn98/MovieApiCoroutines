package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.movieproject.R
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context,
                LoginActivity::class.java))
        }
    }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onClick(v: View?) {
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        when (v?.id) {
            R.id.login_button -> {
                signIn(email, password)
            }
            R.id.go_to_Sign_up -> {
                RegistrationActivity.start(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        login_button.setOnClickListener(this)
        go_to_Sign_up.setOnClickListener(this)
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.success_message),
                        Toast.LENGTH_LONG).show()
                    MainActivity.start(this)
                    finish()
                } else {
                    val error = task.exception

                    Toast.makeText(this, error?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
