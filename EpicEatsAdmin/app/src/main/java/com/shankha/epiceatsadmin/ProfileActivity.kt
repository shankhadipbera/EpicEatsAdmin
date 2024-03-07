package com.shankha.epiceatsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.databinding.ActivityProfileBinding
import com.shankha.epiceatsadmin.model.UserModel

class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("Users")

        binding.btnBack.setOnClickListener { finish() }

        binding.saveBtn.setOnClickListener {
            updateUserData()
            binding.profileName.isEnabled = false

            binding.profilePhNo.isEnabled = false
            binding.profileAddredd.isEnabled = false

            binding.restruentName.isEnabled = false
            binding.saveBtn.isEnabled = false
        }
        binding.profileName.isEnabled = false
        binding.profileEmail.isEnabled = false
        binding.profilePhNo.isEnabled = false
        binding.profileAddredd.isEnabled = false
        binding.profilePassword.isEnabled = false
        binding.restruentName.isEnabled = false
        binding.saveBtn.isEnabled = false

        var isEnable = false
        binding.editBtn.setOnClickListener {
            isEnable = !isEnable
            binding.profileName.isEnabled = isEnable
           // binding.profileEmail.isEnabled = isEnable
            binding.profilePhNo.isEnabled = isEnable
            binding.profileAddredd.isEnabled = isEnable
           // binding.profilePassword.isEnabled = isEnable
            binding.restruentName.isEnabled = isEnable
            binding.saveBtn.isEnabled = isEnable
            if (isEnable) {
                binding.profileName.requestFocus()
            }
        }

        retriveveUserData()

    }

    private fun disableView() {

    }


    private fun retriveveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ownerName = snapshot.child("name").getValue()
                        var ownerEmail = snapshot.child("email").getValue()
                        var ownerPhoneNo = snapshot.child("address").getValue()
                        var ownerAddress = snapshot.child("phoneNo").getValue()
                        var ownerPassword = snapshot.child("password").getValue()
                        var ownerRestruentName = snapshot.child("nameOfRestaurant").getValue()
                        setDataToTextView(
                            ownerName,
                            ownerEmail,
                            ownerPhoneNo,
                            ownerAddress,
                            ownerPassword,
                            ownerRestruentName
                        )

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        ownerEmail: Any?,
        ownerPhoneNo: Any?,
        ownerAddress: Any?,
        ownerPassword: Any?,
        ownerRestruentName: Any?
    ) {
        binding.profileName.setText(ownerName.toString())
        binding.profileEmail.setText(ownerEmail.toString())
        binding.profilePhNo.setText(ownerPhoneNo.toString())
        binding.profileAddredd.setText(ownerAddress.toString())
        binding.profilePassword.setText(ownerPassword.toString())
        binding.restruentName.setText(ownerRestruentName.toString())


    }

   private fun updateUserData() {
        var updateName = binding.profileName.text.toString().trim()
        var updateEmail = binding.profileEmail.text.toString().trim()
        var updatePhNo = binding.profilePhNo.text.toString().trim()
        var updaAddress = binding.profileAddredd.text.toString().trim()
        var updatePassword = binding.profilePassword.text.toString().trim()
        var updateRestruentName = binding.restruentName.text.toString().trim()

        var userData = UserModel(
            updateEmail,
            updatePassword,
            updateName,
            updateRestruentName,
            updaAddress,
            updatePhNo
        )
       val currentUserUid = auth.currentUser?.uid
        adminReference.child(currentUserUid!!).setValue(userData).addOnSuccessListener {
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }.addOnFailureListener {
            Toast.makeText(this, "Profile Updated not Successful", Toast.LENGTH_SHORT).show()
        }
    }
}