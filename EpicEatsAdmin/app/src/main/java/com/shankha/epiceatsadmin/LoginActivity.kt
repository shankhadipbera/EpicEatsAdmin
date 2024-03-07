package com.shankha.epiceatsadmin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shankha.epiceatsadmin.databinding.ActivityLoginBinding
import com.shankha.epiceatsadmin.model.UserModel

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        binding.dontHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()

        }
        binding.loginBtn.setOnClickListener {
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill all the Details", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }
        }

        binding.loginGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)


        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            this,
                            "Login to your Account Succsessfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "Authentication Failed", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()
        val user = UserModel(email, password)
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Users").child(userId).setValue(user)
    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully SignIn with Google",
                                Toast.LENGTH_SHORT
                            ).show()
                            //
                            val currentUser=auth.currentUser
                            val user= UserModel(currentUser?.email,null,currentUser?.displayName)
                            database.child("Users").child(currentUser!!.uid).setValue(user)
                            //
                            updateUi(authTask.result?.user)


                        } else {
                            Toast.makeText(
                                this,
                                "Failed to  SignIn with Google",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }else{
                    Toast.makeText(
                        this,
                        "Failed to  SignIn with Google",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

// already login

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }



    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}