package com.wizzpass.hilt.adapter

/**
 * Created by novuyo on 06,October,2020
 */




import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wizzpass.hilt.R
import com.wizzpass.hilt.data.local.db.entity.Visitor
import kotlinx.android.synthetic.main.resident_list_item.view.resident_name
import kotlinx.android.synthetic.main.visitor_list_item.view.*
import java.io.File

class VisitorAdapter(
    var postList : ArrayList<Visitor>,
    var listener : OnItemClickListener
) : RecyclerView.Adapter<VisitorAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.visitor_list_item , parent  , false))

    override fun getItemCount(): Int  = postList.size


    override fun onBindViewHolder(holder: VisitorAdapter.PostViewHolder, position: Int)  =  holder.bind(postList[position])

    fun refreshAdapter(newPostList : ArrayList<Visitor>){
        postList.clear()
        postList.addAll(newPostList)
        notifyDataSetChanged()

    }

    inner class PostViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private val  resident_name = view.resident_name
        private val  address = view.textView19
        private val reasonForVisit = view.textView21
        private val profileImage = view.imageView9
        private val date = view.textView22
        private val time = view.textView23

        init {
            view.setOnClickListener(this)
        }

        fun  bind(model : Visitor){
            resident_name.text = model.vis_fName.toString() + " " + model.vis_lname
            address.text = model.resAddress + " " + model.res_street_address
            reasonForVisit.text = model.reasonForVist
            val imgFile = File(model.vis_profImage)
            if (imgFile.exists()) {
                profileImage.setColorFilter(null)
                profileImage.setImageURI(Uri.fromFile(imgFile))
            }

            if(model.visEntryTimeStamp!=null) {
                if(!model.visEntryTimeStamp.isEmpty()) {
                    date.text = model.visEntryTimeStamp.substring(0, 10)
                    time.text = model.visEntryTimeStamp.substring(11, 16)
                }
            }
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

    //2020-10-07T18:58:16.731

}