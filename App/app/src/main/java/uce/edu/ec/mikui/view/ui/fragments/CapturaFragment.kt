package uce.edu.ec.mikui.view.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uce.edu.ec.mikui.databinding.FragmentCapturaBinding
import java.io.File
import java.io.IOException
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import uce.edu.ec.mikui.viewmodel.MainViewModel
import uce.edu.ec.mikui.viewmodel.MainViewModelFactory
import uce.edu.ec.mikui.data.modelo.Post
import uce.edu.ec.mikui.data.dominio.Repositorio
import uce.edu.ec.mikui.view.ui.activity.TestActivity
import java.io.ByteArrayOutputStream
import java.util.*


// TODO: Rename parameter arguments, choose names that match
/**
 * A simple [Fragment] subclass.
 * Use the [CapturaFragment.newInstance] factory method to
 * create an instance of this fragment.a
 */

private const val ARG_PARAM1 = "key"

class CapturaFragment : Fragment() {

    private var miPath: String? = null

    val bundle = Bundle()
    lateinit var photoURI: Uri
    var flagApi: Boolean = false
    var flagStorage: Boolean = false

    //api
    val repositorio = Repositorio()
    val viewModelFactory = MainViewModelFactory(repositorio)
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentCapturaBinding
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    private val pickImage = 100
    lateinit var currentPhotoPath: String

    //firesore
    private val storage = Firebase.storage
    private val storageRef = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            miPath = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCapturaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicio()
        //setupListener()
    }

    private fun setupListener() {

//        binding.galeria.setOnClickListener {
//           galleryAddPic()
//
//        }

        binding.subir.setOnClickListener {
            //subirFirebase()

        }
    }

//    private fun galleryAddPic() {
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//
//        val photoFile: File? = try {
//            createImageFile()
//        } catch (ex: IOException) {
//            // Error occurred while creating the File
//            null
//        }
//        // Continue only if the File was successfully created
//        photoFile?.also {
//            photoURI = FileProvider.getUriForFile(
//                requireActivity().applicationContext,
//                "uce.edu.ec.mikui.fileprovider",
//                it
//            )}
//
//        getAction.launch(gallery)
//        //startActivityForResult(gallery, pickImage)
//
////        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
////            val f = File(currentPhotoPath)
////            mediaScanIntent.data = Uri.fromFile(f)
////            sendBroadcast(mediaScanIntent)}
//        }


    //tomar la foto
//    private fun dispatchTakePictureIntent() {
//
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e: ActivityNotFoundException) {
//            // display error state to the user
//        }
//    }

    //obtener la foto
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            data?.extras?.let {
//                val imageBitmap = it.get("data") as Bitmap
//                binding.shapeableImageView.setImageBitmap(imageBitmap)
//            }
//
//        }
//    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            // Ensure that there's a camera activity to handle the intent
//            activity?.let {
//                takePictureIntent.resolveActivity(it.packageManager)?.also {
//                    // Create the File where the photo should go
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        null
//                    }
//                    // Continue only if the File was successfully created
//                    photoFile?.also {
//                        photoURI = FileProvider.getUriForFile(
//                            requireActivity().applicationContext,
//                            "uce.edu.ec.mikui.fileprovider",
//                            it
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                        //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
//                        getAction.launch(takePictureIntent)
//                    }
//                }
//            }
//        }
//    }

    fun inicio(){
        //val myImg: Bitmap = BitmapFactory.decodeFile(miPath)
        binding.shapeableImageView.setImageURI(miPath?.toUri())
        binding.dotsLoading.visibility = View.VISIBLE

        val myImg: Bitmap = (binding.shapeableImageView.drawable as BitmapDrawable).bitmap
        val myImg64: String = convertBitmapToBase64(myImg)

        val myPost = Post(myImg64)
        viewModel.pushPost(myPost)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val dato = response.body()
                if (dato != null) {
                    //buscarDato(dato.id)
                    bundle.putString("id", response.body()?.id.toString())
                    flagApi = true
                    readyState()
                    //binding.dotsLoading.visibility = View.GONE
                    //binding.subir.visibility = View.VISIBLE
                    Log.d("Response", response.body().toString())
                    Log.d("Response", response.body()?.id.toString())
                    Log.d("Response", response.code().toString())
                    Log.d("Response", response.message())
                    Log.d(
                        "Response",
                        "El # es: ${bundle.get("id")}"
                    )
                    //verificacion con toast
                    //Toast.makeText(activity, "Todo listo", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show()
            }
        })

        subirFirebase(myImg)

    }
//    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        //aqui las acciones
//        //val myImg: Bitmap = BitmapFactory.decodeFile(photoURI.toString())
//        if (it.resultCode == RESULT_OK && it.data?.data == null) {
//            Log.d("MI PATH", photoURI.toString())
//            binding.shapeableImageView.setImageURI(photoURI)
//            binding.dotsLoading.visibility = View.VISIBLE
//
//        }else{
//            val imageUri = it.data?.data
//            binding.shapeableImageView.setImageURI(imageUri)
//        }
//        //Base 64
//            Log.d("MI PATH", currentPhotoPath)
//            val myImg: Bitmap = (binding.shapeableImageView.drawable as BitmapDrawable).bitmap
//            //val myImg: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//            val myImg64: String = convertBitmapToBase64(myImg)
//            val myPost = Post(myImg64)
//
//        //api
//            viewModel.pushPost(myPost)
//            viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
//                if (response.isSuccessful) {
//                    val dato = response.body()
//                    if (dato != null) {
//                        //buscarDato(dato.id)
//                        bundle.putString("id", response.body()?.id.toString())
//                        binding.dotsLoading.visibility = View.GONE
//                        binding.subir.visibility = View.VISIBLE
//                        Log.d("Response", response.body().toString())
//                        Log.d("Response", response.body()?.id.toString())
//                        Log.d("Response", response.code().toString())
//                        Log.d("Response", response.message())
//                        Log.d(
//                            "Response",
//                            "El # es: ${bundle.get("id")} y su link es ${bundle.get("url")}"
//                        )
//                        Toast.makeText(activity, "Todo listo", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show()
//                }
//            })
//
//
//    }

    //la app de camara codifica la foto y la devuelve en este metodo como bitmap
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            binding.shapeableImageView.setImageURI(photoURI)
//            Log.d("MI PATH", photoURI.toString())
////            BitmapFactory.decodeFile(currentPhotoPath)?.also { bitmap ->
////                binding.shapeableImageView.setImageBitmap(bitmap)
////            }
////            val imageBitmap = data?.extras?.get("data") as Bitmap
////            binding.shapeableImageView.setImageBitmap(imageBitmap)
//
//            //api
//            Log.d("MI PATH", currentPhotoPath)
//            val myImg: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//            val myImg64: String = convertBitmapToBase64(myImg)
//            val myPost = Post(myImg64)
//            viewModel.pushPost(myPost)
//            viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
//                if (response.isSuccessful) {
//                    val dato = response.body()
//                    if (dato != null) {
//                        //buscarDato(dato.id)
//                        bundle.putString("id", response.body()?.id.toString())
//                        binding.subir.visibility = View.VISIBLE
//                        Log.d("Response", response.body().toString())
//                        Log.d("Response", response.body()?.id.toString())
//                        Log.d("Response", response.code().toString())
//                        Log.d("Response", response.message())
//                        Log.d(
//                            "Response",
//                            "El # es: ${bundle.get("id")} y su link es ${bundle.get("url")}"
//                        )
//                        Toast.makeText(activity, "Todo listo", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(activity, response.code(), Toast.LENGTH_SHORT).show()
//                }
//            })
//
////            val uri:Uri = currentPhotoPath.toUri()
////            Log.d("Response", currentPhotoPath)
////            val uriString= uri.lastPathSegment
////            Log.d("Response", "$uriString")
////            Log.d("Response", "$uri")
////            var imagesRef: StorageReference =  storageRef.child("images").child(uri.lastPathSegment!!)
////            Log.d("Response", imagesRef.path)
////
////            var uploadTask = imagesRef.putFile(uri)
////            uploadTask?.addOnFailureListener {
////                Toast.makeText(activity, "aldo salio mal lokita", Toast.LENGTH_SHORT).show()
////            }?.addOnSuccessListener { taskSnapshot ->
////                Toast.makeText(activity, "revisa el storage lokita", Toast.LENGTH_SHORT).show()
////            }
//        }
//    }

    fun subirFirebase(bitmap: Bitmap){
        //enviar a Storage
        //binding.shapeableImageView.isDrawingCacheEnabled = true
        //binding.shapeableImageView.buildDrawingCache()
        //binding.subir.visibility = View.GONE
        //binding.dotsLoading.visibility = View.VISIBLE
        //val bitmap = (binding.shapeableImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 45, baos)
        val data = baos.toByteArray()
        val uri:Uri = miPath!!.toUri()
        val imagesRef: StorageReference =  storageRef.child("images").child(uri.lastPathSegment!!)
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(activity, "Ups, algo salio mal", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            //Verificacion con toast
            //Toast.makeText(activity, "Carga correcta", Toast.LENGTH_SHORT).show()
            imagesRef.downloadUrl.addOnCompleteListener {task ->
                if(task.isSuccessful){
                    bundle.putString("url", task.result.toString())
                    //Una vez cargada la imagen la mandamos los datos al siguiente activity
                    flagStorage = true
                    readyState()
//                    val intent = Intent (context, TestActivity::class.java)
//                    intent.putExtra("registro",bundle)
//                    startActivity(intent)
//                    activity?.finish()
                    //Log.d("Response", task.result.toString())

                }
            }
        }

    }

    private fun readyState(){

        if(flagApi && flagStorage){
            val intent = Intent (context, TestActivity::class.java)
            intent.putExtra("registro",bundle)
            startActivity(intent)
            activity?.finish()
        }

    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = "platillo_"
//        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//            Log.d("MI RESPUESTA", currentPhotoPath)
//        }
//    }

}