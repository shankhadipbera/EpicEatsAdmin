package com.shankha.epiceatsadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shankha.epiceatsadmin.databinding.DeliveryItemBinding
import com.shankha.epiceatsadmin.databinding.FeedbackItemBinding
import com.shankha.epiceatsadmin.model.Feedback

class FeedbackAdapter(private var listOfFeedbackItem: ArrayList<Feedback> ):RecyclerView.Adapter<FeedbackAdapter.FeedbackItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackItemViewHolder {
        return FeedbackItemViewHolder(FeedbackItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listOfFeedbackItem.size
    }

    override fun onBindViewHolder(holder: FeedbackItemViewHolder, position: Int) {
       holder.bind(position)
    }
    inner  class FeedbackItemViewHolder (private val binding:FeedbackItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                customerName.text=listOfFeedbackItem[position].UserName
                feedback.text=listOfFeedbackItem[position].feedbackText
                date.text=listOfFeedbackItem[position].orderTime
                feedbackOrderId.text=listOfFeedbackItem[position].orderId
            }

        }

    }
}