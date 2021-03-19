package com.alqudri.sie


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PDFAdapter(
    list: List<PDFModel>,
    context: Context,
    itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<PDFAdapter.Holder>() {
    private val list: List<PDFModel>
    private val context: Context
    var itemClickListener: ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.pdf_item, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder!!.pdfName.setText(list[position].pdfName)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
         val pdfName: TextView
         val imageView: ImageView
        override fun onClick(v: View?) {
            itemClickListener.onClick(v, adapterPosition, false)
        }

        init {
            pdfName = itemView.findViewById(R.id.TV)
            imageView = itemView.findViewById(R.id.IV)
            imageView.setOnClickListener(this)
        }
    }

    init {
        this.list = list
        this.context = context
        this.itemClickListener = itemClickListener
    }
}