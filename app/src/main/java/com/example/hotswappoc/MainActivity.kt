package com.example.hotswappoc

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.util.Log
import android.widget.TextView
import com.example.hotswappoc.databinding.ActivityMainBinding
import java.io.*
import java.lang.Exception
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button3.setOnClickListener {
            copyAssetToStorage("libhotswappoc.so")
            copyAssetToStorage("libhotswappoc-1.so")
        }

        binding.button1.setOnClickListener {
            System.load("$filesDir/libhotswappoc2.so")
            binding.sampleText.text = stringFromJNI()
        }

        binding.button2.setOnClickListener {
            System.load("$filesDir/libhotswappoc-sec2.so")
            binding.sampleText.text = secJNIFunc()
        }


        val filePath: String = Environment.getExternalStorageDirectory().absolutePath

        var outputStream: OutputStream
        var inputStream: InputStream

        val filesDir: String = filesDir.absolutePath
        val finalFile: File = File("$filesDir/libhotswappoc2.so")
//        binding.sampleText.text = filePath

        try {
            inputStream = FileInputStream(File("$filePath/Download/libhotswappoc.so"))
            if (finalFile.exists())
                Log.e(TAG, "onCreate: File 1 already exists")
            outputStream = FileOutputStream(File("$filesDir/libhotswappoc2.so"))

            var inChannel: FileChannel = inputStream.getChannel()
            var outChannel: FileChannel = outputStream.getChannel()
            inChannel.transferTo(0, inChannel.size(), outChannel)

            inputStream = FileInputStream(File("$filePath/Download/libhotswappoc-1.so"))
            if (finalFile.exists())
                Log.e(TAG, "onCreate: File 2 already exists")
            outputStream = FileOutputStream(File("$filesDir/libhotswappoc-sec2.so"))

            inChannel= inputStream.getChannel()
            outChannel= outputStream.getChannel()
            inChannel.transferTo(0, inChannel.size(), outChannel)

            inputStream.close()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun copyAssetToStorage(fileName: String) {
        val context: Context = applicationContext
        val path: String = "${Environment.getExternalStorageDirectory().absolutePath}/Download"
        val file: File = File("${path}/$fileName")
        if (!file.exists()) {
            Log.e(TAG, "copyAssetToStorage: File does not exist")
        }

        val assetManager: AssetManager = context.assets
        try {
            val inStr: InputStream = assetManager.open(fileName)
            val out: OutputStream = FileOutputStream("${path}/$fileName")
            val buffer = ByteArray(1024)
            var read: Int = inStr.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
                read = inStr.read(buffer)
            }
        } catch (e: Exception) {
            e.message
        }
    }

    /**
     * A native method that is implemented by the 'hotswappoc' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun secJNIFunc(): String

//    companion object {
//        // Used to load the 'hotswappoc' library on application startup.
//        init {
//            System.loadLibrary("hotswappoc")
//        }
//    }
}