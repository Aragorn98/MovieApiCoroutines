package com.example.movieproject.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.movieproject.R
//import com.example.firebasedatabase.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val USERS = "users"

        fun start(context: Context) {
            context.startActivity(Intent(context,
                RegistrationActivity::class.java))
        }
    }

    private val firebaseCloudstore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val users by lazy { firebaseCloudstore.collection(USERS) }

    override fun onClick(v: View?) {
        val email = reg_email.text.toString()
        val password = reg_password.text.toString()
        when (v?.id) {
            R.id.sign_up_button -> {
                signUp(email, password)
            }
            R.id.go_to_login_button -> {
                LoginActivity.start(this)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.success_message),
                        Toast.LENGTH_LONG).show()
//                    addUser(email)
                } else {
                    val error = task.exception

                    Toast.makeText(this, error?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    private fun addUser(email: String) {
//        val name = reg_name.text.toString()
//        val surname = reg_surname.text.toString()
//
//        val user = User(email = email, name = name, surname = surname)
//
//        users.add(user).addOnCompleteListener {
//                task ->
//            run {
//                if (task.isSuccessful) {
//                    Toast.makeText(this, R.string.success_message,
//                        Toast.LENGTH_LONG).show()
//                    finish()
//
//                } else {
//                    Toast.makeText(this, task.exception?.message,
//                        Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initUI()
    }

    private fun initUI() {
        sign_up_button.setOnClickListener(this)
        go_to_login_button.setOnClickListener(this)
    }
}
