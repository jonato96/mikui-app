package uce.edu.ec.mikui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uce.edu.ec.mikui.view.ui.fragments.HomeFragment
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.R
import uce.edu.ec.mikui.databinding.PlatilloItemBinding


class PlatilloAdapter(val platillos: List<Platillo>, val listener: HomeFragment):RecyclerView.Adapter<PlatilloAdapter.PlatilloHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatilloHolder {
        val layoudInflater = LayoutInflater.from(parent.context)
        return PlatilloHolder(layoudInflater.inflate(R.layout.platillo_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlatilloHolder, position: Int) {
        holder.render(platillos[position])
    }

    override fun getItemCount(): Int {
        return platillos.size
    }

    inner class PlatilloHolder(val view: View):RecyclerView.ViewHolder(view), View.OnClickListener{
        val binding = PlatilloItemBinding.bind(view)
        fun render(platillo: Platillo){
            binding.tvNombre.text = platillo.nombre
            binding.tvFecha.text = platillo.fecha
            binding.tvDescripcion.text = platillo.titulo
            Picasso.get().load(platillo.url).into(binding.ivPoster)

        }
//        init{
//            itemView.setOnClickListener(this)
//        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                //listener.onItemClick(position)
            }

        }
//        init{
//            itemView.setOnClickListener { v: View ->
//                val position: Int = adapterPosition
//                Toast.makeText(itemView.context,"Pinchaste el platillo # ${position+1}", Toast.LENGTH_SHORT).show()
//
//
//            }
//        }

    }

    interface OnItemCLickListener{
        fun onItemClick(position: Int)
    }
}