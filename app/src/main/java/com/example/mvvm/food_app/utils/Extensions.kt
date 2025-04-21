package com.example.mvvm.food_app.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.R

fun Spinner.setupListWithAdapter(list: MutableList<out Any>, callback:(String) -> Unit){
    val adapter = ArrayAdapter(context, R.layout.item_spinner, list)
    adapter.setDropDownViewResource(R.layout.item_spinner_list)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            callback(list[position].toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }
}


fun View.isVisible(isShowLoading:Boolean, container:View){
    if (isShowLoading){
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    }else{
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }
}

fun RecyclerView.setupRecyclerView(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>){
    this.layoutManager = layoutManager
    this.setHasFixedSize(true)
    this.isNestedScrollingEnabled = false
    this.adapter = adapter
}