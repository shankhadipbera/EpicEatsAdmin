package com.shankha.epiceatsadmin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shankha.epiceatsadmin.databinding.ActivitySignUpBinding
import com.shankha.epiceatsadmin.model.UserModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)




        val locationList = arrayOf(
            "acbsdvm",
            "kjbsdbjkldv",
            "jkvsjbkvjb",
            "jbdkjbxv",
            "ajfvbnsv",
            "zcxnmsduigwbf",
            "ascjknmx"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.listOfLocation.setAdapter(adapter)

        binding.alredyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.btnSignup.setOnClickListener {
            email = binding.signupEmail.text.toString().trim()
            password = binding.signupPassword.text.toString().trim()
            userName = binding.signupName.text.toString().trim()
            nameOfRestaurant = binding.signupRestrunt.text.toString().trim()

            if(email.isBlank()||password.isBlank()||userName.isBlank()||nameOfRestaurant.isBlank()){
                Toast.makeText(this,"Please Fill All Details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }

        binding.signupGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }


    }

    private fun createAccount(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                    saveUserData()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                    Log.d("Account","createAccount : Failure",task.exception)
                }
            }
    }

    private fun saveUserData() {
        email = binding.signupEmail.text.toString().trim()
        password = binding.signupPassword.text.toString().trim()
        userName = binding.signupName.text.toString().trim()
        nameOfRestaurant = binding.signupRestrunt.text.toString().trim()

        val user=UserModel(email,password,userName,nameOfRestaurant)
        val userId:String =FirebaseAuth.getInstance().currentUser!!.uid
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

                            val currentUser=auth.currentUser
                            val user= UserModel(currentUser?.email,null,currentUser?.displayName)
                            database.child("Users").child(currentUser!!.uid).setValue(user)

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

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

}