package com.shankha.epiceatsadmin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.adapter.DeliveryAdapter
import com.shankha.epiceatsadmin.databinding.ActivityOutForDeliveryBinding
import com.shankha.epiceatsadmin.model.OrderDetails

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy{
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database :FirebaseDatabase
    private var listOfCompleteOrderList :ArrayList<OrderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        retriveCompleteOrderDetails()

        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun retriveCompleteOrderDetails() {
        database =FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder").orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        val cusromerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()
        for(order in listOfCompleteOrderList){
            order.userName?.let{
                cusromerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter =DeliveryAdapter(cusromerName,moneyStatus)
        binding.deliveryRecycleView.layoutManager= LinearLayoutManager(this)
        binding.deliveryRecycleView.adapter=adapter
    }
}