package com.example.mvvm.food_app.ui.favorite

import academy.nouri.s4_mvvm.food_app.ui.favorite.FavoriteAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.databinding.FragmentFoodsFavoriteBinding
import com.example.mvvm.food_app.ui.list.FoodListFragmentDirections
import com.example.mvvm.food_app.utils.isVisible
import com.example.mvvm.food_app.utils.setupRecyclerView
import com.example.mvvm.food_app.viewmodel.FoodsFavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FoodsFavoriteFragment : Fragment() {

    //Binding
    private var _binding: FragmentFoodsFavoriteBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val viewModel: FoodsFavoriteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodsFavoriteBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding?.apply {
            //Load data
            viewModel.loadFavorites()
            viewModel.favoriteListData.observe(viewLifecycleOwner){
                if (it.isEmpty){
                    emptyLay.isVisible(true, favoriteList)
                    statusLay.disImg.setImageResource(R.drawable.box)
                    statusLay.disTxt.text = getString(R.string.emptyList)
                }else{
                    emptyLay.isVisible(false, favoriteList)
                    favoriteAdapter.setData(it.data!!)
                    favoriteList.setupRecyclerView(LinearLayoutManager(requireContext()), favoriteAdapter)

                    favoriteAdapter.setOnItemClickListener { food ->
                        val directions = FoodListFragmentDirections.actionListToDetail(food.id)
                        findNavController().navigate(directions)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}