package com.shankha.epiceatsadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shankha.epiceatsadmin.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
   private val customerNames: MutableList<String>,
    private val foodQuantity: MutableList<String>,
    private val foodImages: MutableList<String>,
   private val foodPrice:MutableList<String>,
   private val totalprice:MutableList<String>,
   private val isAccepte:MutableList<Boolean>,
    private val itemClicked: OnItemClicked,
   private val isDispatch:MutableList<Boolean>,
    private val context: Context
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderItemViewHolder>() {

    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderItemViewHolder {
        return PendingOrderItemViewHolder(
            PendingOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    override fun onBindViewHolder(holder: PendingOrderItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PendingOrderItemViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                customerName.isSelected = true
                totalPrice.text=foodPrice[position]
                totalPrice.isSelected = true
                quentity.text = foodQuantity[position]
                quentity.isSelected = true
                totalPay.text=totalprice[position]
                val img =foodImages[position]
                val uri = Uri.parse(img)
                 var isAccepted = isAccepte[position]
                var isDispatch= isDispatch[position]
                Glide.with(context).load(uri).into(foodImage)
                acceptBtn.apply {
                    if (!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickListener(position)

                        } else {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is Diapatched")
                            isDispatch=true
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }

        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}