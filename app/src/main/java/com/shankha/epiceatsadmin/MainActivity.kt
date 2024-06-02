package com.shankha.epiceatsadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.databinding.ActivityMainBinding
import com.shankha.epiceatsadmin.model.OrderDetails

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database:FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var completeOrderReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.cvAddMenu.setOnClickListener {
            startActivity(Intent(this,AddItemActivity::class.java))
        }
        binding.cvAllMenu.setOnClickListener {
            startActivity(Intent(this,AllItemActivity::class.java))
        }
        binding.cvOutForDelivery.setOnClickListener {
            startActivity(Intent(this,OutForDeliveryActivity::class.java))
        }
        binding.cvFeedback.setOnClickListener {
            startActivity(Intent(this,FeedbackActivity::class.java))
        }
        binding.cvProfile.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        binding.cvCreateNewUser.setOnClickListener {
            startActivity(Intent(this,CreateOfferActivity::class.java))
        }
        binding.cvPendingOrder.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }
        binding.cvLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        val listOfTotalPay = mutableListOf<Int>()
        completeOrderReference= FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("₹","")?.toIntOrNull()?.let{   i ->
                        listOfTotalPay.add(i)
                    }

                }
                binding.tvTotalEarning.text = "₹ "+ listOfTotalPay.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun completedOrders() {
        database= FirebaseDatabase.getInstance()
        val completedOrderReference =database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount= snapshot.childrenCount.toInt()
                binding.tvCompletOrder.text =completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pendingOrders() {
      database= FirebaseDatabase.getInstance()
      val pendingOrderReference =database.reference.child("OrderDetails")
      var pendingOrderItemCount = 0
      pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              pendingOrderItemCount= snapshot.childrenCount.toInt()
              binding.tvPendingOrder.text =pendingOrderItemCount.toString()
          }

          override fun onCancelled(error: DatabaseError) {

          }

      })
    }
}