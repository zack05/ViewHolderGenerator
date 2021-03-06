package com.fairy.templateapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fairy.templateapp.databinding.ActivityMainBinding
import com.fairy.templateapp.models.Animal
import com.fairy.templateapp.models.Device
import com.fairy.templateapp.models.Option
import com.fairy.templateapp.models.Vehicle
import com.fairy.templateapp.recyclerviewmodels.CarouselUIModel
import com.fairy.templateapp.recyclerviewmodels.HeaderUIModel
import com.fairy.templateapp.recyclerviewmodels.ListSimpleUIModel
import com.fairy.templateapp.recyclerviewmodels.SwitchUIModel
import com.fairy.viewholdergenerator.BaseAdapter
import com.fairy.viewholdergenerator.EmptyUIModel
import com.fairy.viewholdergenerator.RecyclerViewUIModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)
    }

    private lateinit var viewBinding: ActivityMainBinding


    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val time = System.currentTimeMillis()
        viewModel.recyclerViewData.observe(this, Observer { data ->
            println("Time: ${System.currentTimeMillis() - time}")
            viewBinding.mainRecyclerView.adapter?.apply {
                notifyDataSetChanged()
                return@Observer
            }
            val adapter = BaseAdapter(data, onBindData())
            viewBinding.mainRecyclerView.adapter = adapter
        })

        viewModel.loadRecyclerViewData()

        viewModel.firstTodo.observe(this, Observer {
            println("Todo: ${it.title}")
        })
    }

    private fun onBindData(): (data: Any) -> RecyclerViewUIModel = { data ->
        when (data) {
            is String -> {
                HeaderUIModel(data)
            }
            is Animal -> {
                val icon = ContextCompat.getDrawable(this, data.icon)
                val title = getString(data.name)
                ListSimpleUIModel(icon, title, null)
            }
            is Vehicle -> {
                val icon = ContextCompat.getDrawable(this, data.icon)
                val title = getString(data.name)
                ListSimpleUIModel(icon, title, data.description)
            }
            is Device -> {
                val icon = ContextCompat.getDrawable(this, data.icon)
                val title = getString(data.name)
                ListSimpleUIModel(icon, title, data.description)
            }
            is Option -> {
                SwitchUIModel(data.title, data.option, data.enabled) { isChecked ->
                    data.enabled = isChecked
                }
            }
            is List<*> -> {
                CarouselUIModel(data as List<String>)
            }
            else -> {
                EmptyUIModel()
            }
        }
    }


}