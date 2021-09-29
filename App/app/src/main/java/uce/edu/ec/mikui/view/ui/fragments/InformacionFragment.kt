package uce.edu.ec.mikui.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uce.edu.ec.mikui.data.modelo.PlatilloVersion
import uce.edu.ec.mikui.databinding.FragmentInformacionBinding
import uce.edu.ec.mikui.view.adapter.VersionAdapter

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [InformacionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformacionFragment : Fragment() {

    val lista: List<PlatilloVersion> = listOf(PlatilloVersion("Encebollado", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"),
        PlatilloVersion("Locro de papa", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"),
        PlatilloVersion("Cuy asado", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"),
        PlatilloVersion("Maito de pescado", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"),
        PlatilloVersion("Humitas", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"),
        PlatilloVersion("Fritada", "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/images%2Fplatillo.png?alt=media&token=a1cbbbfa-bba2-4075-9fff-db3ffea3b798"))
        // TODO: Rename and change types of parameters
        private lateinit var binding: FragmentInformacionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformacionBinding.inflate(inflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.attachmentRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.attachmentRecyclerView.adapter = VersionAdapter(lista)

    }
}