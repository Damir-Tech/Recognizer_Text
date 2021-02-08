package Recognizetext.scan.com

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import  com.google.firebase.ml.vision.text.FirebaseVisionText

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        editText = findViewById(R.id.editText)
    }

    fun selectImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }
    override fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data!!.data)
        }
}
    fun startRecognizing(v: View) {
    if (imageView.drawable !=null) {
        editText.setText("")
        v.isEnabled = false
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    v.isEnabled = true
                    editText.setText("Не успешно")
                }
        } else {
            Toast.makeText(this, "Select an Image First", Toast.LENGTH_LONG).show()
        }
    }
    private fun processResultText(resultText: FirebaseVisionText) {
         if (resultText.textBlocks.size == 0) {
             editText.setText("Текст не обнаружен")
             return
    }
    for(block in resultText.textBlocks) {
                val blockText = block.text
                editText.append(blockText + "\n")
             }
        }
}