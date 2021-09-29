package uce.edu.ec.mikui.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import uce.edu.ec.mikui.viewmodel.MainViewModel
import uce.edu.ec.mikui.view.adapter.PrediccionAdapter
import uce.edu.ec.mikui.databinding.FragmentHomeBinding
import uce.edu.ec.mikui.data.dominio.Repositorio
import uce.edu.ec.mikui.viewmodel.MainViewModelFactory

/**
 * Home Fragment
 */
class HomeFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener{

    // TODO: Rename and change types of parameters

    //Injeccion
    val repositorio = Repositorio()
    val viewModelFactory = MainViewModelFactory(repositorio)
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    //firebase
//    private val database = Firebase.database
//    private val myRef = database.reference.child("recognizer")
    //viewBinding
    private lateinit var binding: FragmentHomeBinding

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        //nos indica que vamos a poder hacer scroll sobre el RV
        //Al ser linear le decimos que nos desplazamos verticalmente
        //Sin esto no se puede scrollear
        binding.rvPlatillos.layoutManager = LinearLayoutManager(activity)
        binding.svPlatillos.setOnQueryTextListener(this)
        //lista.clear()
        observeData()
    }

    fun observeData() {
        binding.shimmerViewContainer.startShimmer()
        viewModel.obtenerPlatillos().observe(viewLifecycleOwner, Observer { resultado ->
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.rvPlatillos.adapter = PrediccionAdapter(resultado)
            viewModel.deleteSwipe(binding.rvPlatillos,resultado)
            //deleteSwipe(binding.rvPlatillos, resultado)
        })
    }

    fun observeSearch(cadena: String) {
        if (cadena != ""){
            viewModel.obtenerPlatillosBusqueda(cadena).observe(viewLifecycleOwner, Observer { resultado ->
                binding.rvPlatillos.adapter = PrediccionAdapter(resultado)
                //deleteSwipe(binding.rvPlatillos, resultado)
            })
        }else{
            observeData()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        observeSearch(query!!.replaceFirstChar{
            it.uppercase()
        })
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        observeSearch(newText!!.replaceFirstChar{
            it.uppercase()
        })
        return false
    }

    //    private fun deleteSwipe(
//        recyclerView: RecyclerView,
//        lista: MutableList<Platillo> = ArrayList()
//    ) {
//        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                lista.get(viewHolder.adapterPosition).key?.let { myRef.child(it).setValue(null) }
//                lista.removeAt(viewHolder.adapterPosition)
//                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
//                recyclerView.adapter?.notifyDataSetChanged()
//            }
//        }
//        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

}