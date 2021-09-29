package uce.edu.ec.mikui.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uce.edu.ec.mikui.R
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.databinding.PlatilloItemBinding
import uce.edu.ec.mikui.view.ui.activity.DetailActivity

//El adaptador va a recibir un arreglo de datos y se lo va a poner al RV
//El adaptador debe extender de RecyclerView.Adapter
//VH ViewHolder se encarga de inicializar las vistas del Platillo Item para ponerle datos

class PrediccionAdapter(val platillos: List<Platillo>):RecyclerView.Adapter<PrediccionAdapter.PrediccionHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrediccionHolder {
        val layoudInflater = LayoutInflater.from(parent.context)
        return PrediccionHolder(layoudInflater.inflate(R.layout.platillo_item, parent, false))
    }

    override fun onBindViewHolder(holder: PrediccionHolder, position: Int) {
        holder.render(platillos[position])
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, DetailActivity::class.java).apply {
                putExtra("key", platillos[position].key)
            }
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return platillos.size
    }

    //inner class tiene la ventaja de ser una clase hija, muere cuando la clase padre muere
    //PrediccionHolder debe extender de ViewHolder
    inner class PrediccionHolder(val view: View):RecyclerView.ViewHolder(view){
        val binding = PlatilloItemBinding.bind(view)
        fun render(platillo: Platillo){
            binding.tvNombre.text = platillo.nombre
            binding.tvFecha.text = platillo.fecha
            binding.tvDescripcion.text = platillo.titulo
            binding.ivPoster?.let {
                Glide.with(view.context)
                    .load(platillo.url)
                    .into(it)
            }
        }
    }

}