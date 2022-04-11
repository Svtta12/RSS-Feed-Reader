package main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import main.`interface`.ItemClickListener
import main.model.Item
import science.example.myrssreader.R
import kotlinx.android.synthetic.main.row.view.*



class FeedAdapter(initItems: List<Item>, private val itemClickListener: ItemClickListener): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private val items = mutableListOf<Item>()

    init {
        items.addAll(initItems)
    }

    class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, itemClickListener: ItemClickListener) {
            itemView.txtTitle.text = item.title
            itemView.txtContent.text = item.content
            itemView.txtPubdate.text = item.pubDate

            itemView.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int = items.size
}