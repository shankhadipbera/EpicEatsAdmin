package com.shankha.epiceatsadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceatsadmin.adapter.FeedbackAdapter
import com.shankha.epiceatsadmin.databinding.ActivityFeedbackBinding
import com.shankha.epiceatsadmin.model.Feedback
import com.shankha.epiceatsadmin.model.OrderDetails

class FeedbackActivity : AppCompatActivity() {
    private val binding: ActivityFeedbackBinding by lazy {
        ActivityFeedbackBinding.inflate(layoutInflater)
    }

    private var listOfFeedbackItem: ArrayList<Feedback> = arrayListOf()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usrrId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth=FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usrrId = auth.currentUser?.uid ?: ""

        val feedbackOrderRef = database.reference.child("Feedback")
        val sortingQuery = feedbackOrderRef.orderByChild("orderTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (feedbackSnapshot in snapshot.children) {
                    val feedbackItems = feedbackSnapshot.getValue(Feedback::class.java)
                    feedbackItems?.let { listOfFeedbackItem.add(it) }
                }
                setAdapters()
            }

            fun setAdapters() {
                val adapter = FeedbackAdapter(listOfFeedbackItem)
                binding.feedbackRecycleView.layoutManager = LinearLayoutManager(this@FeedbackActivity, LinearLayoutManager.VERTICAL, false)
                binding.feedbackRecycleView.adapter = adapter
            }

                    override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FeedbackActivity, "Feedback not Fetch", Toast.LENGTH_SHORT).show()
            }

    })

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}