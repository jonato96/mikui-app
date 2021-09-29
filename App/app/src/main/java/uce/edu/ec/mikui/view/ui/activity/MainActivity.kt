package uce.edu.ec.mikui.view.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uce.edu.ec.mikui.viewmodel.MainViewModel
import uce.edu.ec.mikui.R
import uce.edu.ec.mikui.databinding.ActivityMainBinding
import uce.edu.ec.mikui.data.dominio.Repositorio
import uce.edu.ec.mikui.view.ui.fragments.*
import uce.edu.ec.mikui.viewmodel.MainViewModelFactory
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(){

    lateinit var currentPhotoPath: String
    val bundle = Bundle()

    //Injeccion
    val repositorio = Repositorio()
    val viewModelFactory = MainViewModelFactory(repositorio)
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    //CONFIGURACION BINDING
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //inflamos
        binding = ActivityMainBinding.inflate(layoutInflater)
        //pasamos la vista
        setContentView(binding.root)

        binding.btnPrincipal.setOnClickListener {
            requestCameraPermission()
            //dispatchTakePictureIntent()
            //supportFragmentManager.beginTransaction().replace(R.id.host_fragment, CapturaFragment()).commit()
            //binding.btnNavegacion.set
        }
        //Eescuchamos el evento long preset de nuestro FAB
        binding.btnPrincipal.setOnLongClickListener {
            requestPermission()
            //Toast.makeText(this, "Que onda", Toast.LENGTH_SHORT).show()
            true
        }

        binding.btnNavegacion.background = null

        binding.btnNavegacion.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    currentFragment = HomeFragment()
                }
//                R.id.place -> {
//                    currentFragment = LocationFragment()
//                }
//                R.id.search -> {
//                    currentFragment = LocationFragment()
//                }
                R.id.info -> {
                    currentFragment = InformacionFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.host_fragment, currentFragment).commit()
            true
        }

        viewModel.getPing()
        viewModel.myGetResponse.observe(this, Observer { response ->
            //usamos el try catch en caso de que heroku se encuentre suspendido
            try {
                if(response.isSuccessful){
                    Log.d("Response", "Ping "+ response.body()?.messaje.toString())
                    Toast.makeText(this, "Servicios en linea", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Response", response.errorBody().toString())//
                    Toast.makeText(this, "Ups", Toast.LENGTH_SHORT).show()
                }
            }catch (e: SocketTimeoutException){
                e.printStackTrace()
            }
        })
    }

    private fun requestCameraPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    dispatchTakePictureIntent()
                }

                else -> requestPermissionCamLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }else{
            dispatchTakePictureIntent()
        }
    }

    private val requestPermissionCamLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->
        if (isGranted){
            dispatchTakePictureIntent()
        }else{
            Toast.makeText(this, "Se necesita habilitar los permisos de camara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI = FileProvider.getUriForFile(
                            this.applicationContext,
                            "uce.edu.ec.mikui.fileprovider",
                            it
                        )
                        currentPhotoPath = photoURI.toString()
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                        getAction.launch(takePictureIntent)
                    }
                }
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //aqui las acciones
        //val myImg: Bitmap = BitmapFactory.decodeFile(photoURI.toString())
        if (it.resultCode == RESULT_OK) {
            val bundle = Bundle()
            bundle.putString("key", currentPhotoPath)
            val fragmento: CapturaFragment = CapturaFragment()
            fragmento.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragmento).commit()

        }
    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            //currentPhotoPath = absolutePath
        }
    }

    //TODO Galleria
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    seleccionarFotoGaleria()
                }

                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }else{
            seleccionarFotoGaleria()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted->
        if (isGranted){
            seleccionarFotoGaleria()
        }else{
            Toast.makeText(this, "Se necesita habilitar los permisos de galeria", Toast.LENGTH_SHORT).show()
        }
    }

    private val starForActivityResultGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data

            bundle.putString("key", data.toString())

            val fragmento: CapturaFragment = CapturaFragment()
            fragmento.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragmento).commit()

        }

    }
    private fun seleccionarFotoGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        starForActivityResultGalery.launch(intent)
    }

}