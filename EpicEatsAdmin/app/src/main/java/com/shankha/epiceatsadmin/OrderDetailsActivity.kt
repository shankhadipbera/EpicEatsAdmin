package com.shankha.epiceatsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.shankha.epiceatsadmin.adapter.OrderDetailsAdapter
import com.shankha.epiceatsadmin.databinding.ActivityOrderDetailsBinding
import com.shankha.epiceatsadmin.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy{
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName :String? =null
    private var phoneNo :String? =null
    private var address :String? =null
    private var totalPrice :String? =null
    private  var foodNames : ArrayList<String> = arrayListOf()
    private  var foodImages : ArrayList<String> = arrayListOf()
    private  var foodQuantites : ArrayList<Int> = arrayListOf()
    private  var foodPrices : ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val recivedOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        recivedOrderDetails?.let{orderDetails ->
                userName = recivedOrderDetails.userName
                phoneNo = recivedOrderDetails.phoneNo
                address = recivedOrderDetails.address
                totalPrice = recivedOrderDetails.totalPrice
                foodNames = recivedOrderDetails.foodNames as ArrayList<String>
                foodImages = recivedOrderDetails.foodImage as ArrayList<String>
                foodQuantites = recivedOrderDetails.foodQuantities as ArrayList<Int>
                foodPrices = recivedOrderDetails.foodPrice as ArrayList<String>

                setUserDetails()
                setAdapter()

        }

    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter =OrderDetailsAdapter(this,foodNames,foodImages,foodQuantites,foodPrices)
        binding.orderDetailsRecyclerView.adapter=adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address .text = address
        binding .contactNo .text = phoneNo
        binding .totalAmount .text = totalPrice
    }
}