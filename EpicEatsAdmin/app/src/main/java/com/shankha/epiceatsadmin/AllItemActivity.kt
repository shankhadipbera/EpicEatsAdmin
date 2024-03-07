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
import com.shankha.epiceatsadmin.databinding.ActivityAllItemBinding
import com.shankha.epiceatsadmin.model.AllMenu

class AllItemActivity : AppCompatActivity() {
    private lateinit var database:FirebaseDatabase
    private lateinit var  auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var menuItems: ArrayList<AllMenu> = ArrayList()


    private val binding :ActivityAllItemBinding by lazy{
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference= FirebaseDatabase.getInstance().reference

        auth=FirebaseAuth.getInstance()

        retriveMenuItem()

        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun retriveMenuItem() {
        database= FirebaseDatabase.getInstance()
        val foodRef =database.reference.child("Menu")
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for( foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapter() {
        val adapter= AllItemAdapter(this@AllItemActivity,menuItems,databaseReference){position ->
            deleteMenuItems(position)
        }
        binding.menuRecyclerView.adapter=adapter
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(this)
    }

    private fun deleteMenuItems(position: Int) {
      val menuItemToDelete =menuItems[position]
        val menuItemKey=menuItemToDelete.key
        val foodMenuReference =database.reference.child("Menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task->
            if(task.isSuccessful){
                val storageRef = FirebaseStorage.getInstance().reference.child("menu_items/$menuItemKey.jpg")
                storageRef.delete().addOnSuccessListener {  }.addOnFailureListener {  }
                menuItems.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Item delete is not Successfull",Toast.LENGTH_SHORT).show()
            }
        }
    }
}