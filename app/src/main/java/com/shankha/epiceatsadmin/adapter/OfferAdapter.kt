package com.shankha.epiceatsadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.shankha.epiceatsadmin.databinding.AllMenuItemBinding
import com.shankha.epiceatsadmin.databinding.OfferListBinding
import com.shankha.epiceatsadmin.model.AllMenu
import com.shankha.epiceatsadmin.model.Offers

class OfferAdapter(private val context: Context,
                   private val offerList: ArrayList<Offers>,
                   private val onDeleteClickListener:(position :Int)  ->Unit): RecyclerView.Adapter<OfferAdapter.OfferItemViewHolder> (){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferAdapter.OfferItemViewHolder {
        return OfferItemViewHolder(OfferListBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: OfferAdapter.OfferItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    inner  class OfferItemViewHolder (private val binding : OfferListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {

                val offerDetails=offerList[position]
                val uriString = offerDetails.offerImage
                val uri = Uri.parse(uriString)
                offerDesc.text=offerDetails.offerDetails
                offerName.text=offerDetails.offerTitle
                Glide.with(context).load(uri).into(offerPic)

                delete.setOnClickListener {
                    onDeleteClickListener(position)
                }

            }

        }

    }
}