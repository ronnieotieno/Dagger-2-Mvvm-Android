package dev.ronnie.imageloaderdagger2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.imageloaderdagger2.data.model.ImagesResponse
import dev.ronnie.imageloaderdagger2.databinding.ImageItemBinding


/**
 *created by Ronnie Otieno on 04-Apr-21.
 **/
class ImagesAdapter(private val navigate: (ImagesResponse, ImageView) -> Unit) :
    PagingDataAdapter<ImagesResponse, ImagesAdapter.ViewHolder>(
        DiffCallback()
    ) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var imageResponse: ImagesResponse? = null


        fun bind(imagesResponse: ImagesResponse, position: Int) {
            this.imageResponse = imagesResponse
            binding.apply {
                image = imagesResponse
                shouldRound = true
                //  imageView.transitionName = image?.urls?.regular not working

                binding.root.setOnClickListener {
                    imageResponse?.let {
                        navigate.invoke(it, imageView)
                    }
                }
                executePendingBindings()
            }
        }

    }


    private class DiffCallback : DiffUtil.ItemCallback<ImagesResponse>() {
        override fun areItemsTheSame(oldItem: ImagesResponse, newItem: ImagesResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImagesResponse, newItem: ImagesResponse): Boolean {
            return oldItem == newItem
        }
    }
}