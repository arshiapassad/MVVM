package academy.nouri.s4_mvvm.food_app.ui.favorite

import academy.nouri.s4_mvvm.food_app.data.database.FoodEntity
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mvvm.databinding.ItemFoodsBinding
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var binding: ItemFoodsBinding
    private var moviesList = emptyList<FoodEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem from PagingDataAdapter
        holder.bind(moviesList[position])
        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = moviesList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: FoodEntity) {
            binding.apply {
                itemFoodsImg.load(item.img) {
                    crossfade(true)
                    crossfade(100)
                }
                itemFoodsTitle.text = item.title

                itemFoodsCategory.visibility = View.GONE
                itemFoodsArea.visibility = View.GONE
                itemFoodsCount.visibility = View.GONE
                //Click
                root.setOnClickListener { onItemClickListener?.let { it(item) } }
            }
        }
    }

    private var onItemClickListener: ((FoodEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (FoodEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<FoodEntity>) {
        val moviesDiffUtil = MoviesDiffUtils(moviesList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class MoviesDiffUtils(private val oldItem: List<FoodEntity>, private val newItem: List<FoodEntity>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}