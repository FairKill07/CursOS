package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val imagePickerRequestCode = 123
    private val cameraRequestCode = 124
    private lateinit var selectedImage: Bitmap
    private lateinit var imageView: ImageView
    private lateinit var smileResultTextView: TextView
    private lateinit var detectButton: Button
    private lateinit var faceDetector: FaceDetector

    private val MAX_HISTORY_SIZE = 10
    private val smileResultsHistory = mutableListOf<String>()
    private lateinit var historyAdapter: ArrayAdapter<String>

    private var Path:String = "";
    private  var Result:String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ініціалізація елементів інтерфейсу
        initializeUI()

        // Ініціалізація детектора обличчя
        initializeFaceDetector()

        // Налаштування історії результатів
        setupHistory()

        // Обробник натискання на кнопку вибору зображення
        findViewById<Button>(R.id.pickImageButton).setOnClickListener {
            pickImage()
        }

        // Обробник натискання на кнопку детекції посмішки
        detectButton.setOnClickListener {
            detectSmile()

        }
    }

    private  fun allDb()
    {
        val myDbManager = MyDbManager(this)
        myDbManager.openDb()
        myDbManager.insertToDb(this.Result, this.Path)
        myDbManager.readDb()
    }

    private fun initializeUI() {
        imageView = findViewById(R.id.imageView)
        smileResultTextView = findViewById(R.id.smileResultTextView)
        detectButton = findViewById(R.id.detectButton)
    }

    private fun initializeFaceDetector() {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        faceDetector = FaceDetection.getClient(options)
    }

    private fun setupHistory() {
        historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, smileResultsHistory)
        findViewById<ListView>(R.id.historyListView).adapter = historyAdapter
    }

    private fun pickImage() {
        val pickImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickImageIntent.type = "image/*"
        startActivityForResult(pickImageIntent, imagePickerRequestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri : Uri? = null;
        uri = data?.data;
        Path = uri.toString();

        when (requestCode) {
            imagePickerRequestCode, cameraRequestCode -> {
                handleImageSelection(requestCode, resultCode, data)
            }
        }
    }

    private fun handleImageSelection(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                when (requestCode) {
                    imagePickerRequestCode -> {
                        handleImageSelectionFromGallery(data)
                    }
                    cameraRequestCode -> {
                        handleImageSelectionFromCamera(data)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun handleImageSelectionFromGallery(data: Intent?) {
        data?.data?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            selectedImage = android.graphics.BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(selectedImage)
        }
    }

    private fun handleImageSelectionFromCamera(data: Intent?) {
        selectedImage = data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(selectedImage)
    }

    private fun detectSmile() {
        if (::selectedImage.isInitialized) {
            val inputImage = InputImage.fromBitmap(selectedImage, 0)
            faceDetector.process(inputImage)
                .addOnSuccessListener { faces ->
                    handleSmileDetectionSuccess(faces)
                }
                .addOnFailureListener { e ->
                    handleSmileDetectionFailure(e)
                }
        }
    }

    private fun handleSmileDetectionSuccess(faces: List<Face>) {
        if (faces.isNotEmpty()) {
            val smileProbability = faces[0].getSmilingProbability()
            val resultText = "Посмішку виявлено: $smileProbability"
            this.Result = smileProbability.toString()
            allDb();
            updateSmileResult(resultText)
            addToSmileResultsHistory(resultText)
        } else {
            val resultText = "На жаль, обличчя не виявлено."
            updateSmileResult(resultText)
            addToSmileResultsHistory(resultText)
        }
    }

    private fun handleSmileDetectionFailure(e: Exception) {
        if (e is MlKitException) {
            val resultText = "Помилка: ${e.message}"
            updateSmileResult(resultText)
            addToSmileResultsHistory(resultText)
        }
    }

    private fun updateSmileResult(resultText: String) {
        smileResultTextView.text = resultText
    }

    private fun addToSmileResultsHistory(result: String) {
        if (smileResultsHistory.size >= MAX_HISTORY_SIZE) {
            smileResultsHistory.removeAt(0)
        }
        smileResultsHistory.add(result)
        historyAdapter.notifyDataSetChanged()
    }
}
