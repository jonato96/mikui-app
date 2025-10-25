package uce.edu.ec.mikui.data.modelo

import com.google.firebase.database.Exclude

data class Nutri(
    val media:String? = null,
    val cal:String? = null,
    val car:String? = null,
    val gr:String? = null,
    val pro:String? = null,
    @Exclude val key: String? = null)
