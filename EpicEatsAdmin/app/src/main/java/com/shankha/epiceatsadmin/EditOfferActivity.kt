package com.shankha.epiceatsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.shankha.epiceatsadmin.adapter.AllItemAdapter
import com.shankha.epiceatsadmin.adapter.OfferAdapter
import com.shankha.epiceatsadmin.databinding.ActivityEditOfferBinding
import com.shankha.epiceatsadmin.model.AllMenu
import com.shankha.epiceatsadmin.model.Offers

class EditOfferActivity : AppCompatActivity() {
private val binding : ActivityEditOfferBinding by lazy{
    ActivityEditOfferBinding.inflate(layoutInflater)
}

    private lateinit var database: FirebaseDatabase
    private lateinit var  auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var offerItems: ArrayList<Offers> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBck.setOnClickListener { finish() }
        databaseReference= FirebaseDatabase.getInstance().reference

        auth=FirebaseAuth.getInstance()

        retriveOfferItem()
    }
    private fun retriveOfferItem() {
        database= FirebaseDatabase.getInstance()
        val offerRef =database.reference.child("Offer")
        offerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offerItems.clear()
                for( offerSnapshot in snapshot.children){
                    val offerItem = offerSnapshot.getValue(Offers::class.java)
                    offerItem?.let {
                        offerItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapter() {
        val adapter= OfferAdapter(this@EditOfferActivity,offerItems){position ->
            deleteOfferItems(position)
        }
        binding.offerRV.adapter=adapter
        binding.offerRV.layoutManager= LinearLayoutManager(this)
    }

    private fun deleteOfferItems(position: Int) {
        val offerToDelete =offerItems[position]
        val offerItemKey=offerToDelete.key
        val offerReference =database.reference.child("Offer").child(offerItemKey!!)
        offerReference.removeValue().addOnCompleteListener { task->
            if(task.isSuccessful){
                val storageRef = FirebaseStorage.getInstance().reference.child("offer_list/$offerItemKey.jpg")
                storageRef.delete().addOnSuccessListener {  }.addOnFailureListener {  }
                offerItems.removeAt(position)
                binding.offerRV.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Item delete is not Successfull", Toast.LENGTH_SHORT).show()
            }
        }
    }


}