package com.example.sos_it.kartru.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.kartru.model.WorkItem
import com.example.sos_it.kartru.adapter.WorkAdapter
import com.google.firebase.database.*

class WorkFragment : Fragment(){
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<WorkItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_work, container, false)
        userRecyclerView = view.findViewById(R.id.worklist)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
            getWorkData()
        return view
    }
    private fun getWorkData() {
        dbref= FirebaseDatabase.getInstance().getReference("Orders")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userArrayList.clear()
                    for(userSnapshot in snapshot.children){
                        val user=userSnapshot.getValue(WorkItem::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerView.adapter= WorkAdapter(userArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

