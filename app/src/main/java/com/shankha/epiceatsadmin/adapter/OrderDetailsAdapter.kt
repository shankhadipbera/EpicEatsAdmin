package com.shankha.epiceatsadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shankha.epiceatsadmin.databinding.OrderDetailsItemBinding
import java.util.PrimitiveIterator

class OrderDetailsAdapter(
    private var context: Context,
    private  var foodName:ArrayList<String>,
    private  var foodImage : ArrayList<String>,
    private var foodQuantity :ArrayList<Int>,
    private  var foodPrice : ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
       return OrderDetailsViewHolder(OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return foodName.size
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OrderDetailsViewHolder(private  val binding: OrderDetailsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodname.text =foodName[position]
                foodname.isSelected = true
                foodlprice.text =foodPrice[position]
                foodlprice.isSelected = true
                quentity.text = foodQuantity[position].toString()
                quentity.isSelected = true
                val img = foodImage[position]
                val uri = Uri.parse(img)
                Glide.with(context).load(uri).into(foodimage)
            }

        }

    }

}