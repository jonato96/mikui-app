package uce.edu.ec.mikui.data.modelo

import com.google.firebase.database.Exclude

data class Platillo(
    val nombre:String? = null,
    val descripcion:String? = null,
    val url:String? = null,
    val fecha:String? = null,
    val titulo:String? = null,
    val shortd:String? = null,
    val origen:String? = null,
    @Exclude val key: String? = null)
