package com.example.sos_it.karma.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.SearchItem
import com.example.sos_it.karma.adapter.SearchAdapter
import com.google.firebase.database.*

class SearchFragment : Fragment() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<SearchItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)
        userRecyclerView=view.findViewById(R.id.searchlist)
        userRecyclerView.layoutManager= LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)
        userArrayList= arrayListOf()
        val search=view.findViewById<EditText>(R.id.searchbar)
       search.addTextChangedListener(object :TextWatcher{
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               userArrayList.clear()
               var text=s.toString()
               if(s?.isNotEmpty() == true){
                  text=(s.first().uppercaseChar().toString()+s.substring(1,text.length))
               }
               getSearch(text)
           }

           override fun afterTextChanged(s: Editable?) {
           }

       })
        return view
    }

    private fun getSearch(s: String?) {
        if(s!=null && s!="") {
            val dbref =
                FirebaseDatabase.getInstance().getReference("Restaurants").orderByChild("name")
                    .startAt(s).endAt(s + "\uf8ff")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userArrayList.clear()
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(SearchItem::class.java)
                            userArrayList.add(user!!)

                        }
                        userRecyclerView.adapter = SearchAdapter(userArrayList)
                    } else
                        Toast.makeText(context, "No such data found", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}