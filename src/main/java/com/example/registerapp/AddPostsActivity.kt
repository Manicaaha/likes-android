package com.example.registerapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AddPostsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_posts)
        auth = Firebase.auth
        val Title_EditText: EditText = findViewById(R.id.addpost_title_editText)
        val Desc_EditText: EditText = findViewById(R.id.addpost_desc_ediText)
        val addButton: Button = findViewById(R.id.addpost_addButton)
        val db = Firebase.firestore

        addButton.setOnClickListener {
            if(Title_EditText.text.isEmpty() || Desc_EditText.text.isEmpty()) {
                return@setOnClickListener
            }

            val title = Title_EditText.text.toString()
            val desc = Desc_EditText.text.toString()
            val currentUserId = auth.currentUser?.uid

            val post = hashMapOf(
                "uid" to currentUserId,
                "title" to title,
                "desc" to desc
            )

            db.collection("posts")
                .add(post)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }

    }


}