package com.wizzpass.hilt.adapter

/**
 * Created by novuyo on 06,October,2020
 */




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wizzpass.hilt.R
import com.wizzpass.hilt.db.entity.Resident
import com.wizzpass.hilt.db.entity.Visitor
import kotlinx.android.synthetic.main.resident_list_item.view.*

class VisitorAdapter(
    var postList : ArrayList<Visitor>,
    var listener : OnItemClickListener
) : RecyclerView.Adapter<VisitorAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.resident_list_item , parent  , false))

    override fun getItemCount(): Int  = postList.size


    override fun onBindViewHolder(holder: VisitorAdapter.PostViewHolder, position: Int)  =  holder.bind(postList[position])

    fun refreshAdapter(newPostList : ArrayList<Visitor>){
        postList.clear()
        postList.addAll(newPostList)
        notifyDataSetChanged()

    }

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val  resident_name = view.resident_name

        init {
            view.setOnClickListener(this)
        }


        fun  bind(model : Visitor){
            resident_name.text = model.vis_fName.toString() + " " + model.vis_lname
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