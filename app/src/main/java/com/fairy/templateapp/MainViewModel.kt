package com.fairy.templateapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fairy.templateapp.models.Animal
import com.fairy.templateapp.models.Device
import com.fairy.templateapp.models.Option
import com.fairy.templateapp.models.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { TodoRepository() }

    val firstTodo = liveData(Dispatchers.IO) {
        try {
            val retrivedTodo = repository.getTodo(1)
            emit(retrivedTodo)
        } catch (e: Exception) {
            println("Todo EXCEPTION!! (${e.message}")
        }
    }

    private val Dog = Animal(R.string.animal_dog, R.drawable.ic_dog)

    private val Cat = Animal(R.string.animal_cat, R.drawable.ic_cat)

    private val Bus = Vehicle(
        R.string.vehicle_bus,
        R.drawable.ic_bus,
        "A bus (contracted from omnibus,[1] with variants multibus, motorbus, autobus, etc.)" +
                " is a road vehicle designed to carry many passengers. " +
                "Buses can have a capacity as high as 300 passengers."
    )

    private val Tram = Vehicle(
        R.string.vehicle_tram,
        R.drawable.ic_tram,
        "A tram (in North America streetcar or trolley) is a rail vehicle that runs on tramway " +
                "tracks along public urban streets; some include segments of segregated right-of-way."
    )

    private val IPhone = Device(
        R.string.iphone,
        R.drawable.ic_baseline_phone_iphone_24,
        "The best mobile."
    )

    private val Android = Device(
        R.string.android,
        R.drawable.ic_android,
        "The most used OS"
    )

    private val OptionOne = Option(
        "Super cool option",
        "This is the best option ever!!",
        false
    )

    private val OptionTwo = Option(
        "Super cool option two",
        "This is the second best option!!",
        true
    )


    val recyclerViewData: MutableLiveData<List<Any>> by lazy {
        MutableLiveData<List<Any>>()
    }

    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    fun loadRecyclerViewData() {
        viewModelScope.launch {
            recyclerViewData.value = createList()
        }
    }

    @ExperimentalStdlibApi
    private suspend fun createList() = withContext(Dispatchers.Default) {
        listOf(
            "Animals",
            Dog,
            Cat,
            "Vehicles",
            Bus,
            Tram,
            Tram,
            Bus,
            "Devices",
            IPhone,
            Android,
            Android,
            "Options",
            OptionOne,
            OptionTwo
        )
    }

}