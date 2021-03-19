package com.alqudri.sie

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.folder_item.view.*


class ItemAdapter(
    var anggota: List<Items>,
    var listener: OnItemClickListener,
    var folder: List<Folder>,
    var data: OnLongClickListener,
    var context: Context
) : RecyclerView.Adapter<ItemAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(anggota: Items?, position: Folder?, pos: Int) {
            if (pos <= (folder.size -1) && !folder.isNullOrEmpty()) {
                itemView.imageView.setImageResource(R.drawable.bb)
//                item
                itemView.tv1.text = position!!.nama
                itemView.setOnClickListener {
                    listener.OnItemClick(anggota as Items, position)
                }
            }else{
                itemView.tv1.text = anggota!!.nama
                itemView.setOnClickListener {
                    val intent = Intent(it.context, PDFActivity::class.java)
                    intent.putExtra("url", anggota!!.url);
                    it.context.startActivity(intent)
                }
                itemView.setOnLongClickListener {
                    data.OnLongClick(anggota)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return ( anggota.size + folder.size)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var exFolder = arrayListOf<Folder>()
        var exItems = arrayListOf<Items>()
        var folderCount = getFolderCount(position)
        var itemCount = getItemsCount(position)
        var folderList: List<Folder> = folder
        var itemList:List<Items> = anggota

        if (folder.isEmpty()){
            exFolder.add(Folder("Kosong", "k"))
            folderList = exFolder
        }else{
            folderList = folder
        }
        if (anggota.isEmpty()){
            exItems.add(Items("Kosong", "k"))
            itemList = exItems
        }else{
            itemList = anggota
        }

        return holder.bind(itemList[itemCount], folderList[folderCount], position)
    }


    fun getItemsCount(position: Int): Int{
        var count = 0
        if (!(position <= (folder.size-1))){
            count = position-folder.size
        }
        return count
    }

    fun getFolderCount(position: Int):Int{
        var count = 0
        if (position <= (folder.size-1)){
            count = position
        }
        return count
    }

    interface OnItemClickListener {
        fun OnItemClick(items: Items, folder: Folder)
    }

    interface OnLongClickListener {
        fun OnLongClick(items: Items)
    }
}