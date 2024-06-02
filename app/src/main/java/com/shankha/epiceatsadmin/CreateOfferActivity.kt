package com.shankha.epiceatsadmin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.shankha.epiceatsadmin.databinding.ActivityCreateOfferBinding
import com.shankha.epiceatsadmin.model.Offers

class CreateOfferActivity : AppCompatActivity() {
    private val binding : ActivityCreateOfferBinding by lazy{
        ActivityCreateOfferBinding.inflate(layoutInflater)
    }

    private var foodOfferUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var offerDetails: String
    private lateinit var OfferTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.manageOldOffer.setOnClickListener {
            startActivity(Intent(this,EditOfferActivity::class.java))
        }

        binding.uploadOffer.setOnClickListener {
            offerDetails = binding.offerDescriptrion.text.toString()
            OfferTitle=binding.offerTitles.text.toString()

            if (offerDetails.isBlank()||OfferTitle.isBlank() ) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()
                finish()
            }

        }

        binding.selectOffer.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun uploadData() {
        val offerRef = database.getReference("Offer")
        val newItemKey = offerRef.push().key
        if (foodOfferUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("offer_list/$newItemKey.jpg")
            val uploadTask = imageRef.putFile(foodOfferUri!!)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newOffer= Offers(
                       offerImage = downloadUrl.toString(),
                        offerDetails = offerDetails,
                        key = newItemKey,
                        offerTitle = OfferTitle
                    )
                    newItemKey?.let { key ->
                        offerRef.child(key).setValue(newOffer).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                            Toast.makeText(this, "Item added Successfully", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to Upload Data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT).show()
            }


        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }

    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.offerImage.setImageURI(uri)
            foodOfferUri = uri
        }
    }
}