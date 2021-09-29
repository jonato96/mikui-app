package uce.edu.ec.mikui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Response
import uce.edu.ec.mikui.data.modelo.Get
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.data.modelo.Post
import uce.edu.ec.mikui.data.dominio.Repositorio

class MainViewModel(private val repositorio: Repositorio): ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    val myGetResponse: MutableLiveData<Response<Get>> = MutableLiveData()

    fun getPing(){
        viewModelScope.launch {
            val response = repositorio.getPing()
            myGetResponse.value = response
        }
    }

    fun pushPost(post: Post){
        viewModelScope.launch {
            val response = repositorio.pushPost(post)
            myResponse.value = response
        }
    }

    fun obtenerPlatillos():LiveData<MutableList<Platillo>>{
        val mutableData = MutableLiveData<MutableList<Platillo>>()
        repositorio.getPlatilloData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun obtenerPlatillosBusqueda(cadena:String):LiveData<MutableList<Platillo>>{
        val mutableData = MutableLiveData<MutableList<Platillo>>()
        repositorio.getPlatilloBusqueda(cadena).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun obtenerPlatillo(categoria: String):LiveData<Platillo>{
        val mutablePlatillo = MutableLiveData<Platillo>()
        repositorio.getPlatillo(categoria).observeForever {
            mutablePlatillo.value = it
        }
        return mutablePlatillo
    }

    fun deleteSwipe(recyclerView: RecyclerView, lista: MutableList<Platillo> = ArrayList()) {
        repositorio.deleteSwipe(recyclerView, lista)
    }



}