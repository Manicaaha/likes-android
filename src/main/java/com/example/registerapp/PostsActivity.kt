package com.example.registerapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class PostsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val posts = mutableListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_posts)
        val db = Firebase.firestore
        auth = Firebase.auth

        val Myadapter = myAdapter(posts)
        val myRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
        myRecyclerView.adapter = Myadapter
        myRecyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("posts")
//            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    posts.add(Post(document.id,
                        document.data["uid"].toString(),
                        document.data["title"].toString(),
                        document.data["desc"].toString()))
                }
                Myadapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


    }
}