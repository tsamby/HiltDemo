package com.wizzpass.hilt.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.SecondaryDriver
import kotlinx.android.synthetic.main.secondary_driver_list_item.view.*

class SecondaryDriverAdapter(
    var postList : ArrayList<SecondaryDriver>,
    var listener : OnItemClickListener
) : RecyclerView.Adapter<SecondaryDriverAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.secondary_driver_list_item , parent  , false))

    override fun getItemCount(): Int  = postList.size


    override fun onBindViewHolder(holder: SecondaryDriverAdapter.PostViewHolder, position: Int)  =  holder.bind(postList[position])

    fun refreshAdapter(newPostList : ArrayList<SecondaryDriver>){
        postList.clear()
        postList.addAll(newPostList)
        notifyDataSetChanged()

    }

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val  sec_driver_name = view.secondarydriver_name

        init {
            view.setOnClickListener(this)
        }


        fun  bind(model : SecondaryDriver){
            sec_driver_name.text = model.fName.toString() + " " + model.lname
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