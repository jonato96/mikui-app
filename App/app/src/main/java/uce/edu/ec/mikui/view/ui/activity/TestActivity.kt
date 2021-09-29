package uce.edu.ec.mikui.view.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import uce.edu.ec.mikui.data.modelo.Platillo
import uce.edu.ec.mikui.data.modelo.PlatilloBase
import uce.edu.ec.mikui.databinding.ActivityTestBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity() {

    //Firestore RealTime Database
    private val database = Firebase.database
    private val myRef = database.reference.child("platillos")
    //binding
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = Date()
        val dateInString = SimpleDateFormat("dd/MM/yyyy")
        val bundle = intent.getBundleExtra("registro")
        var categoria: String? = bundle?.getString("id")

        binding.btMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (categoria != null) {
            myRef.child(categoria).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val platillo: PlatilloBase? = dataSnapshot.getValue(PlatilloBase::class.java)
                    if (platillo != null) {
                        val registro = Platillo(platillo.nombre, platillo.descripcion,
                            bundle?.getString("url"), dateInString.format(date), platillo.titulo, platillo.shortd, platillo.origen)
                        //Picasso.get().load(registro.url).into(binding.ivCapturado)
                        val myRef = database.reference.child("recognizer")
                        myRef.child(myRef.push().key.toString()).setValue(registro)

                        images(registro.url!!)
                        binding.tvNombre.text = registro.nombre
                        binding.tvTitulo.text = registro.titulo
                        binding.tvShort.text = registro.shortd


                    }else{
                        Log.d("valor", "El plato es : nulo")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("valor", "Failed to read value.", error.toException())
                }
            })
        }

        binding.fabCompartiR.setOnClickListener {
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey, descubrí un nuevo platillo con Mikui app")
            intent.type="text/plain"
            val bitmap = screenShot(binding.root)
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap!!))
            intent.type="image/jpeg"
            startActivity(Intent.createChooser(intent,"Compartir vía:"))
        }
    }

    fun screenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height-500, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private  fun images(url: String){
        Glide.with(this)
            .load(url)
            .into(binding.ivCapturado)
    }
}