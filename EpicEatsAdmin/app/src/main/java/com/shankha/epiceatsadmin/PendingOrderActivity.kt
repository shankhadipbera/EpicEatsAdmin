package com.shankha.epiceatsadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.adapter.PendingOrderAdapter
import com.shankha.epiceatsadmin.databinding.ActivityPendingOrderBinding
import com.shankha.epiceatsadmin.model.OrderDetails

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {

    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfQuantities: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private var totalPays: MutableList<String> = mutableListOf()
    private var isAccepted: MutableList<Boolean> = mutableListOf()
    private var isDispatch: MutableList<Boolean> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrderDetails()

        binding.btnBack.setOnClickListener { finish() }


    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.foodPrice?.let { listOfTotalPrice.add(it.toString()) }
            orderItem.foodQuantities?.let { listOfQuantities.add(it.toString()) }
            orderItem.totalPrice?.let { totalPays.add(it) }
            orderItem.orderAccepted?.let { isAccepted.add(it) }
            orderItem.orderDispatch?.let{isDispatch.add(it)}
            orderItem.foodImage?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }

        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecycleView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(
            listOfName,
            listOfQuantities,
            listOfImageFirstFoodOrder,
            listOfTotalPrice,
            totalPays,
            isAccepted,
            this,
            isDispatch,
            this
        )
        binding.pendingOrderRecycleView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("userOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)

    }
    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfOrderItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("Users").child(userIdOfClickedItem!!).child("BuyHistory").child("PendingOrder")
                .child(pushKeyOfOrderItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfOrderItem).child("orderAccepted").setValue(true)

    }


    override fun onItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position]).addOnSuccessListener {
            deleteThisItemFromOrderDetails(dispatchItemPushKey)
        }
        dispatchItemOrderReference.child("orderDispatch").setValue(true)
        dispatchItemOrderReference.child("orderAccepted").setValue(true)
        updateOrderDispatchStatus(position)

    }

    private fun updateOrderDispatchStatus(position: Int) {
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfOrderItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference =
            database.reference.child("Users").child(userIdOfClickedItem!!).child("BuyHistory").child("PendingOrder")
                .child(pushKeyOfOrderItem!!)
        buyHistoryReference.child("orderDispatch").setValue(true)
      //  databaseOrderDetails.child(pushKeyOfOrderItem).child("orderDispatch").setValue(true)

    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        var orderDetailsItemReference =database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue().addOnSuccessListener {
            Toast.makeText(this,"Order is Dispatched ",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Order is not Dispatched ",Toast.LENGTH_SHORT).show()
        }

    }

}