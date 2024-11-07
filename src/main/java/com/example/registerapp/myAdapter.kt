package com.example.registerapp

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class myAdapter(val posts: MutableList<Post>) : RecyclerView.Adapter<myAdapter.MyViewHolder>() {
        inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    private var auth = Firebase.auth
    val db = Firebase.firestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val titleTextView: TextView = holder.itemView.findViewById(R.id.title_textView)
        val descTextView: TextView = holder.itemView.findViewById(R.id.desc_textView)
        val deleteImage: ImageView = holder.itemView.findViewById(R.id.delete_imageView)
        val followImage: ImageView = holder.itemView.findViewById(R.id.item_follow_imageView)
        var doesExist = false

        val docRef = db.collection("follows").document(auth.currentUser!!.uid+":"+posts[position].id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    followImage.setImageResource(R.drawable.baseline_add_reaction_24)
                    doesExist = true
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        titleTextView.text = posts[position].title
        descTextView.text = posts[position].desc

//        if(auth.currentUser!!.uid != posts[position].uid) {
//            titleTextView.isVisible = false
//            descTextView.isVisible = false
//            deleteImage.isVisible = false
//        }
        followImage.setOnClickListener{

            if( !doesExist){
                val follow = hashMapOf(
                    "uid" to auth.currentUser!!.uid,
                    "pid" to posts[position].id
                )
                db.collection("follows").document(auth.currentUser!!.uid+":"+posts[position].id)
                    .set(follow)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ")
                        followImage.setImageResource(R.drawable.baseline_add_reaction_24)
                        doesExist = true

                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
            else{
                db.collection("follows").document(auth.currentUser!!.uid+":"+posts[position].id)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                        followImage.setImageResource(R.drawable.outline_add_reaction_24)
                        doesExist = false
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
            }


        }
        deleteImage.setOnClickListener {
            db.collection("posts").document(posts[position].id)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    posts.removeAt(position.toString().toInt())
                    notifyDataSetChanged()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}