package uz.urverse.dataloadingpractice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import uz.urverse.dataloadingpractice.GlideBlurTransformation
import uz.urverse.dataloadingpractice.databinding.ItemLayoutBinding
import uz.urverse.dataloadingpractice.databinding.ItemLoadingBinding
import uz.urverse.dataloadingpractice.model.ApiResponse

class MainAdapter(private var itemList: MutableList<ApiResponse?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    inner class ItemViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding =
                ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = itemList[position]
            holder.binding.titleTextView.text = item?.title

            val crossFadeFactory =
                DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

            Glide.with(holder.itemView.context)
                .load(item?.thumbnailUrl)
                .apply(
                    RequestOptions().override(50, 50)
                        .transform(GlideBlurTransformation(holder.itemView.context))
                )
                .into(holder.binding.imageView)

            Glide.with(holder.itemView.context)
                .load(item?.url)
                .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
                .into(holder.binding.imageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = itemList.size

    fun addLoadingView() {
        itemList.add(null)
        notifyItemInserted(itemList.size - 1)
    }

    fun removeLoadingView() {
        if (itemList.isNotEmpty()) {
            itemList.removeAt(itemList.size - 1)
            notifyItemRemoved(itemList.size)
        }
    }

    fun addItems(items: List<ApiResponse>) {
        val positionStart = itemList.size
        itemList.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }
}
