package uce.edu.ec.mikui.view.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uce.edu.ec.mikui.R
import uce.edu.ec.mikui.databinding.ActivityDetailBinding
import uce.edu.ec.mikui.view.ui.fragments.DetailFragment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    //private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val key = intent.getStringExtra("key")


        binding.btnPrincipal2.setOnClickListener {

            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey, mira este dato que encontre en Mikui app:")
            intent.type="text/plain"
            val bitmap = screenShot(binding.root)
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap!!))
            intent.type="image/jpeg"
            startActivity(Intent.createChooser(intent,"Compartir vía:"))
        }

        val bundle = Bundle()
        bundle.putString("key", key)
        val fragmento: DetailFragment = DetailFragment()
        fragmento.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.host_fragment_detail, fragmento).commit()

//        findNavController(R.id.host_fragment_detail)
//            .setGraph(R.navigation.nav_graph_detail, args.toBundle())

        initPlatillo()

    }

    fun screenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
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


    fun initPlatillo(){

        //binding.navigationIcon.setOnClickListener { onBackPressed() }

//        val name = intent.getStringExtra("nombre")
//        val fecha = intent.getStringExtra("fecha")
//        val descripcion = intent.getStringExtra("descripcion")
//        val url = intent.getStringExtra("url")

//        binding.tvNombre.text = name
//        binding.tvPlato.text = fecha
//        binding.tvDescripcion.text = descripcion
        //Picasso.get().load(url).into(binding.backgroundImageView)

    }

}