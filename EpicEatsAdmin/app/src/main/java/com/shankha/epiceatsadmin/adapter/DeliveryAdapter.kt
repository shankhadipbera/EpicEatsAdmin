package com.shankha.epiceatsadmin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shankha.epiceatsadmin.databinding.DeliveryItemBinding

class DeliveryAdapter(private val customerNames:MutableList<String>,private val moneyStatues:MutableList<Boolean>):RecyclerView.Adapter<DeliveryAdapter.DeliveryItemViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemViewHolder {
        return DeliveryItemViewHolder(DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = customerNames.size

    override fun onBindViewHolder(holder: DeliveryItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DeliveryItemViewHolder(private val binding: DeliveryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
                binding.apply {
                    customerName.text=customerNames[position]
                    if(moneyStatues[position]==true){
                        paymentStatus.text="Received"
                        deliveryStatus.text="Delivered"
                        deliveryStatusCV.visibility=View.VISIBLE
                    }else{
                        paymentStatus.text="Not Received"
                        deliveryStatus.text="Not Delivered"
                        deliveryStatusCV.visibility=View.INVISIBLE
                    }


                    val paymentColorMap= mapOf(true to Color.GREEN, false to Color.RED)
                    paymentStatus.setTextColor(paymentColorMap[moneyStatues[position]]?:Color.BLACK)
                    deliveryStatus.setTextColor(paymentColorMap[moneyStatues[position]]?:Color.BLACK)


                }
        }

    }
}