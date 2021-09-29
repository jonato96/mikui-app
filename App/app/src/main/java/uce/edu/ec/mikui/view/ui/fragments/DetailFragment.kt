package uce.edu.ec.mikui.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import uce.edu.ec.mikui.databinding.FragmentDetailBinding
import uce.edu.ec.mikui.data.dominio.Repositorio
import uce.edu.ec.mikui.viewmodel.MainViewModel
import uce.edu.ec.mikui.viewmodel.MainViewModelFactory

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 */

private const val ARG_PARAM1 = "key"

class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters

    val repositorio = Repositorio()
    val viewModelFactory = MainViewModelFactory(repositorio)
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    private var id: String? = null
    //FIREBASE
//    private val database = Firebase.database
//    private val myRef = database.reference.child("recognizer")

//    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        observeData()
        //id?.let { buscarDato(it) }
    }

    fun observeData() {
        id?.let {
            viewModel.obtenerPlatillo(it).observe(viewLifecycleOwner, Observer { resultado ->
                binding.tvNombre.text = resultado.nombre
                binding.tvPlato.text = resultado.titulo
                binding.tvDescripcion.text = resultado.descripcion
                binding.cdOrigen.text = resultado.origen
                binding.tvSabiasQue.text = resultado.shortd
                images(resultado.url)
            })
        }
    }

    private  fun images(url: String?){
        Glide.with(this)
            .load(url)
            .into(binding.backgroundImageView)
    }
}