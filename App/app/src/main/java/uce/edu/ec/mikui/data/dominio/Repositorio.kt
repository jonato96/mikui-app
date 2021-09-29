package uce.edu.ec.mikui.data.dominio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import retrofit2.Response
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.core.RetrofitInstance
import uce.edu.ec.mikui.data.DataSource
import uce.edu.ec.mikui.data.modelo.Get
import uce.edu.ec.mikui.data.modelo.Post

class Repositorio() {

    private val dataSource = DataSource()

    suspend fun getPing(): Response<Get> {
        return dataSource.getPing()
    }

    suspend fun pushPost(post: Post): Response<Post> {
        return RetrofitInstance.api.pushPost(post)
    }

    fun getPlatilloData():LiveData<MutableList<Platillo>>{
        val mutableData = MutableLiveData<MutableList<Platillo>>()
        Firebase.database.reference.child("recognizer").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listaPlatillo = mutableListOf<Platillo>()
                dataSnapshot.children.reversed().forEach {child ->
                    val platillo: Platillo? =
                        Platillo(child.child("nombre").getValue<String>(),
                            child.child("descripcion").getValue<String>(),
                            child.child("url").getValue<String>(),
                            child.child("fecha").getValue<String>(),
                            child.child("titulo").getValue<String>(),
                            child.child("shortd").getValue<String>(),
                            child.child("origen").getValue<String>(),
                            child.key)
                    if (platillo != null) {
                        listaPlatillo.add(platillo)
                    }else{
                        Log.d("TAG", "Error en getPlatillo, Repositorio")
                    }
                }
                mutableData.value = listaPlatillo
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        })
        return mutableData
    }

    fun getPlatilloBusqueda(cadena:String):LiveData<MutableList<Platillo>>{
        val mutableData = MutableLiveData<MutableList<Platillo>>()
        Firebase.database.reference.child("recognizer").orderByChild("nombre")
            .startAt(cadena)
            .endAt("$cadena\uf8ff")
            .addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listaPlatillo = mutableListOf<Platillo>()
                dataSnapshot.children.forEach {child ->
                    val platillo: Platillo? =
                        Platillo(child.child("nombre").getValue<String>(),
                            child.child("descripcion").getValue<String>(),
                            child.child("url").getValue<String>(),
                            child.child("fecha").getValue<String>(),
                            child.child("titulo").getValue<String>(),
                            child.child("shortd").getValue<String>(),
                            child.child("origen").getValue<String>(),
                            child.key)
                    if (platillo != null) {
                        listaPlatillo.add(platillo)
                    }else{
                        Log.d("TAG", "Error en getPlatillo, Repositorio")
                    }
                }
                mutableData.value = listaPlatillo
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        })
        return mutableData
    }

    fun getPlatillo(categoria: String):LiveData<Platillo>{
        val mutablePlatillo = MutableLiveData<Platillo>()
        Firebase.database.reference.child("recognizer").child(categoria).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mutablePlatillo.value = dataSnapshot.getValue(Platillo::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("valor", "Failed to read value.", error.toException())
            }
        })
        return mutablePlatillo
    }

    fun deleteSwipe(recyclerView: RecyclerView, lista: MutableList<Platillo> = ArrayList()) {
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                lista[viewHolder.adapterPosition].key?.let { Firebase.database.reference.child("recognizer").child(it).setValue(null) }
                lista.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}