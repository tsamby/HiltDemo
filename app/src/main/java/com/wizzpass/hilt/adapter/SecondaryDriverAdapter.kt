package com.wizzpass.hilt.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.wizzpass.hilt.R
import com.wizzpass.hilt.data.local.db.entity.SecondaryDriver
import kotlinx.android.synthetic.main.secondary_driver_list_item.view.*
import java.io.File

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
        private val  prof_img = view.imageView7

        init {
            view.setOnClickListener(this)
        }


        fun  bind(model : SecondaryDriver) {

            sec_driver_name.text = model.fName.toString() + " " + model.lname
            val imgFile = File(model.profImage)
            if (imgFile.exists()) {
                prof_img.setColorFilter(null)
                prof_img.setImageURI(Uri.fromFile(imgFile))


                Picasso.get().load(Uri.fromFile(imgFile)).into(prof_img);

                Picasso.get().load(Uri.fromFile(imgFile)).memoryPolicy(MemoryPolicy.NO_CACHE).into(prof_img);

                //Picasso.with(context).load(rowItem.getProductImages().get(0)).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(holder.productImageView);
            }
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }



    }

    fun removeAt(position: Int) {
        postList.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}