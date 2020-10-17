package com.wizzpass.hilt.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wizzpass.hilt.R
import com.wizzpass.hilt.data.local.db.entity.Vehicles
import kotlinx.android.synthetic.main.secondary_driver_list_item.view.*

class AdditionalVehicleAdapter(
    var postList : ArrayList<Vehicles>,
    var listener : OnItemClickListener
) : RecyclerView.Adapter<AdditionalVehicleAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.secondary_driver_list_item , parent  , false))

    override fun getItemCount(): Int  = postList.size


    override fun onBindViewHolder(holder: AdditionalVehicleAdapter.PostViewHolder, position: Int)  =  holder.bind(postList[position])

    fun refreshAdapter(newPostList : ArrayList<Vehicles>){
        postList.clear()
        postList.addAll(newPostList)
        notifyDataSetChanged()

    }

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val  sec_driver_name = view.secondarydriver_name

        init {
            view.setOnClickListener(this)
        }


        fun  bind(model : Vehicles){
            sec_driver_name.text = model.carReg
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