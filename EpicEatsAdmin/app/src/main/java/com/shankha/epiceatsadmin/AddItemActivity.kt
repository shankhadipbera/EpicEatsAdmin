package com.shankha.epiceatsadmin

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.shankha.epiceatsadmin.databinding.ActivityAddItemBinding
import com.shankha.epiceatsadmin.model.AllMenu

class AddItemActivity : AppCompatActivity() {
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addItemBtn.setOnClickListener {
            foodName = binding.enterFoodName.text.toString().trim()
            foodDescription = binding.descriptionET.text.toString().trim()
            foodIngredient = binding.ingredentsET.text.toString().trim()
            foodPrice = binding.enterFoodPrice.text.toString().trim()
            // foodImageUri=binding.selectedImage.to

            if (foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()

                finish()
            }

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun uploadData() {
        val menuRef = database.getReference("Menu")
        val newItemKey = menuRef.push().key
        //foodImageUri=binding.selectedImage.imageU
        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_items/$newItemKey.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodImage = downloadUrl.toString(),
                        foodIngredient = foodIngredient,
                        newItemKey,
                        1
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
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
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}