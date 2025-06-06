package com.example.mvvm.food_app.ui.detail

import academy.nouri.s4_mvvm.food_app.data.database.FoodEntity
import academy.nouri.s4_mvvm.food_app.utils.CheckConnection
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.mvvm.R
import com.example.mvvm.databinding.ActivityFoodsBinding
import com.example.mvvm.databinding.FragmentFoodDetailBinding
import com.example.mvvm.food_app.ui.list.FoodListFragment
import com.example.mvvm.food_app.utils.MyResponse
import com.example.mvvm.food_app.utils.VIDEO_ID
import com.example.mvvm.food_app.utils.isVisible
import com.example.mvvm.food_app.utils.setupRecyclerView
import com.example.mvvm.food_app.viewmodel.FoodsDetailViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class FoodDetailFragment : Fragment() {

    //Binding
    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var connection: CheckConnection

    @Inject
    lateinit var entity: FoodEntity

    //Other
    private val args: FoodDetailFragmentArgs by navArgs()
    private var foodID = 0
    private val viewModel: FoodsDetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDetailBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding?.apply {
            //Get data
            foodID = args.foodId
            //InitView
            binding.apply {
                //back
                detailBack.setOnClickListener { findNavController().navigateUp() }
                //Call api
                viewModel.loadFoodDetail(foodID)
                viewModel.foodDetailData.observe(viewLifecycleOwner) {
                    when (it.status) {
                        MyResponse.Status.LOADING -> {
                            detailLoading.isVisible(true, detailContentLay)
                        }

                        MyResponse.Status.SUCCESS -> {
                            detailLoading.isVisible(false, detailContentLay)
                            it.data?.meals?.get(0)?.let { itMeal ->
                                //Entity
                                entity.id = itMeal.idMeal!!.toInt()
                                entity.title = itMeal.strMeal.toString()
                                entity.img = itMeal.strMealThumb.toString()
                                //Set data
                                foodCoverImg.load(itMeal.strMealThumb) {
                                    crossfade(true)
                                    crossfade(500)
                                }
                                foodCategoryTxt.text = itMeal.strCategory
                                foodAreaTxt.text = itMeal.strArea
                                foodTitleTxt.text = itMeal.strMeal
                                foodDescTxt.text = itMeal.strInstructions
                                //Play
                                if (itMeal.strYoutube != null) {
                                    foodPlayImg.visibility = View.VISIBLE
                                    foodPlayImg.setOnClickListener {
                                        /*val videoId = itMeal.strYoutube.split("=")[1]
                                        Intent(requireContext(), PlayerActivity::class.java).also {
                                            it.putExtra(VIDEO_ID, videoId)
                                            startActivity(it)
                                        }*/
                                    }
                                } else {
                                    foodPlayImg.visibility = View.GONE
                                }
                                //Source
                                if (itMeal.strSource != null) {
                                    foodSourceImg.visibility = View.VISIBLE
                                    foodSourceImg.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(itMeal.strSource)
                                            )
                                        )
                                    }
                                } else {
                                    foodSourceImg.visibility = View.GONE
                                }
                            }
                            //Json Array
                            val jsonData = JSONObject(Gson().toJson(it.data))
                            val meals = jsonData.getJSONArray("meals")
                            val meal = meals.getJSONObject(0)
                            //Ingredient
                            for (i in 1..15) {
                                val ingredient = meal.getString("strIngredient$i")
                                if (ingredient.isNullOrEmpty().not()) {
                                    ingredientsTxt.append("$ingredient\n")
                                }
                            }
                            //Measure
                            for (i in 1..15) {
                                val measure = meal.getString("strMeasure$i")
                                if (measure.isNullOrEmpty().not()) {
                                    measureTxt.append("$measure\n")
                                }
                            }

                    }
                        MyResponse.Status.ERROR -> {
                            detailLoading.isVisible(false, detailContentLay)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                //Favorite
                viewModel.existsFood(foodID)
                viewModel.isFavoriteData.observe(viewLifecycleOwner){
                    isFavorite = it
                    if (it) {
                        detailFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.tartOrange))
                    } else {
                        detailFav.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
                //Save / Delete
                detailFav.setOnClickListener {
                    if (isFavorite){
                        viewModel.deleteFood(entity)
                    } else {
                        viewModel.saveFood(entity)
                    }
                }
                //Internet
                connection.observe(viewLifecycleOwner){
                    if (it){
                        checkConnectionOrEmpty(false, FoodListFragment.PageState.NONE)
                    }else{
                        checkConnectionOrEmpty(true, FoodListFragment.PageState.NETWORK)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkConnectionOrEmpty(isShownError: Boolean, state: FoodListFragment.PageState){
        binding?.apply {
            if (isShownError){
                homeDisLay.isVisible(true, detailContentLay)
                when(state){
                    FoodListFragment.PageState.EMPTY -> {
                        statusLay.disImg.setImageResource(R.drawable.box)
                        statusLay.disTxt.text = getString(R.string.emptyList)
                    }
                    FoodListFragment.PageState.NETWORK -> {
                        statusLay.disImg.setImageResource(R.drawable.disconnect)
                        statusLay.disTxt.text = getString(R.string.checkInternet)
                    }
                    else ->{}
                }
            }else{
                homeDisLay.isVisible(false, detailContentLay)
            }
        }
    }
}