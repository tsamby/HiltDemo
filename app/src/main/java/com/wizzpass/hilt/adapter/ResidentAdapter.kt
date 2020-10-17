package com.wizzpass.hilt.adapter

/**
 * Created by novuyo on 26,September,2020
 */


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wizzpass.hilt.R
import com.wizzpass.hilt.data.local.db.entity.Resident
import kotlinx.android.synthetic.main.resident_list_item.view.*

class ResidentAdapter(
    var postList : ArrayList<Resident>,
    var listener : OnItemClickListener
) : RecyclerView.Adapter<ResidentAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.resident_list_item , parent  , false))

    override fun getItemCount(): Int  = postList.size


    override fun onBindViewHolder(holder: ResidentAdapter.PostViewHolder, position: Int)  =  holder.bind(postList[position])

    fun refreshAdapter(newPostList : ArrayList<Resident>){
        postList.clear()
        postList.addAll(newPostList)
        notifyDataSetChanged()

    }

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val  resident_name = view.resident_name

        init {
            view.setOnClickListener(this)
        }


        fun  bind(model : Resident){
            resident_name.text = model.fName.toString() + " " + model.lname
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}