package com.shankha.epiceatsadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.databinding.AllMenuItemBinding
import com.shankha.epiceatsadmin.model.AllMenu

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position :Int)  ->Unit
) : RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>() {

    // private val itemQuenties = IntArray(menuList.size) { 1 }

    init {
        val database = FirebaseDatabase.getInstance()

        menuItemReference = database.reference.child("Menu")


    }
    companion object{
        private lateinit var menuItemReference : DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        return AddItemViewHolder(
            AllMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddItemViewHolder(private val binding: AllMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
       // val itemQuenties = IntArray(menuList.size) { 1 }
        fun bind(position: Int) {
            binding.apply {
              //  val quantity =itemQuenties[position]
              //  var quantity = menuList[position].foodQuantity
                val menuItem=menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                Foodname.text = menuItem.foodName
                FoodPrice.text = menuItem.foodPrice
                TotaltemCount.text= menuItem.foodQuantity.toString()
                Glide.with(context).load(uri).into(FoodPic)
              //  TotaltemCount.text=quantity.toString()

             //   val itemQuenties= menuItem.foodQuantity



                plusBtn.setOnClickListener {
                    increseQuantity(adapterPosition)
                }
                minusBtn.setOnClickListener {
                    decreseQuentity(adapterPosition)
                }
                deleteBtn.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }

        private fun deleteItem(position: Int) {
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)

        }

        private fun decreseQuentity(position: Int) {
            if (menuList[position].foodQuantity > 0) {
                menuList[position].foodQuantity--
                updateCartItem(position)
                binding.TotaltemCount.text = menuList[position].foodQuantity.toString()
            }
        }

        private fun increseQuantity(position: Int) {
            if (menuList[position].foodQuantity < 30) {
                menuList[position].foodQuantity++
                updateCartItem(position)
                binding.TotaltemCount.text = menuList[position].foodQuantity.toString()
            }
        }

        private fun updateCartItem(position: Int) {
            val uniqueKey: String?= menuList[position].key
                if (uniqueKey != null) {
                    val updatedQuantity = menuList[position].foodQuantity
                    menuItemReference.child(uniqueKey).child("foodQuantity").setValue(updatedQuantity)
                        .addOnSuccessListener {
                            // Handle success if needed
                        }
                        .addOnFailureListener {
                            // Handle failure if needed
                        }
                }
        }

    }


}