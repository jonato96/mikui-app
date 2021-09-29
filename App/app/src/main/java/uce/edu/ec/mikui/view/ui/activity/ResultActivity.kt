package uce.edu.ec.mikui.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityResultBinding
    //firebase
    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myRef = database.reference.child("recognizer")

        val bundle = intent.getBundleExtra("registro")
        val registro = Platillo(bundle?.getString("nombre"), bundle?.getString("descripcion"),
            bundle?.getString("url"), bundle?.getString("fecha"), bundle?.getString("titulo"), bundle?.getString("origen"))

        myRef.child(myRef.push().key.toString()).setValue(registro)
        finish()
    }

}