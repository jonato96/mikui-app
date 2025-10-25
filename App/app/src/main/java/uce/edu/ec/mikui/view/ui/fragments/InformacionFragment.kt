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

    val lista: List<PlatilloVersion> = listOf(
        PlatilloVersion("Colada Morada",  "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_colada.jpg?alt=media&token=af6e718a-4902-46b5-9e6b-2cf6cb897469"),
        PlatilloVersion("Cuy asado",      "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_cuy.jpg?alt=media&token=e03fd8b2-7a21-4a4c-8ecd-1a21bdc61c19"),
        PlatilloVersion("Encebollado",    "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_encebollado.jpg?alt=media&token=ece71279-94dd-4fba-9f14-fb5294e81452"),
        PlatilloVersion("Fanesca",        "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_fanesca.jpg?alt=media&token=16548110-ee33-4924-b492-ef3f8ca991dc"),
        PlatilloVersion("Fritada",        "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_fritada.jpg?alt=media&token=6456f638-9f6a-4c25-a592-7d5152c21740"),
        PlatilloVersion("Humitas",        "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_humitas.jpg?alt=media&token=3fa085be-adfc-45cb-bb7f-785840767e3c"),
        PlatilloVersion("Llapingacho",    "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_llapingacho.jpg?alt=media&token=97f9192f-677a-4ffd-9914-f430f005d35e"),
        PlatilloVersion("Quimbolitos",    "https://firebasestorage.googleapis.com/v0/b/mikuiapp.appspot.com/o/versiones%2Fbase_quimbolitos.jpg?alt=media&token=d83b0581-f31e-409b-a3b7-0090b416c4c4"),
        )
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