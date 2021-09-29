package uce.edu.ec.mikui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uce.edu.ec.mikui.R
import uce.edu.ec.mikui.data.modelo.PlatilloVersion
import uce.edu.ec.mikui.databinding.PlatilloVersionItemBinding

//El adaptador va a recibir un arreglo de datos y se lo va a poner al RV
//El adaptador debe extender de RecyclerView.Adapter
//VH ViewHolder se encarga de inicializar las vistas del Platillo Item para ponerle datos

class VersionAdapter(val platillos: List<PlatilloVersion>):RecyclerView.Adapter<VersionAdapter.VersionHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionHolder {
        val layoudInflater = LayoutInflater.from(parent.context)
        return VersionHolder(layoudInflater.inflate(R.layout.platillo_version_item, parent, false))
    }

    override fun onBindViewHolder(holder: VersionHolder, position: Int) {
        holder.render(platillos[position])

    }

    override fun getItemCount(): Int {
        return platillos.size
    }

    //inner class tiene la ventaja de ser una clase hija, muere cuando la clase padre muere
    //PrediccionHolder debe extender de ViewHolder
    inner class VersionHolder(val view: View):RecyclerView.ViewHolder(view){
        val binding = PlatilloVersionItemBinding.bind(view)
        fun render(platillo: PlatilloVersion){
            binding.tvNombre.text = platillo.nombre
            binding.attachmentImageView?.let {
                Glide.with(view.context)
                    .load(platillo.img)
                    .into(it)
            }
        }
    }

}