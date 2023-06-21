package com.example.qrcodetest

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.qrcodetest.databinding.ActivityMainBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.IOException




class MainActivity : AppCompatActivity() {
    private var requestCamera: ActivityResultLauncher<String>? =null
    private lateinit var binding: ActivityMainBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(),)
        {
            if (it){
                var intent = Intent (this, MainActivity :: class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_SHORT).show()
            }
        }
        binding.scanButton.setOnClickListener(){
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        }
    }
    private fun iniBC(){
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        cameraSource =CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setAutoFocusEnabled(true)
            //.setFacing(CameraSource.CAMERA_FACING_FRONT)
            .build()

        binding.surfaceView.addCallback(object : SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
               try{
                    cameraSource.start(binding.surfaceView!!.)
               }catch (e:IOException){
                   e.printStackTrace()
               }

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
               cameraSource.stop()
            }

        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(applicationContext,"barcode scanner has been stopped",Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if(barcodes.size()!=0){
                    binding.barcodeResultView!!.post{
                        binding.scanButton!!.text = "SEARCH ITEM"
                        intentData = barcodes.valueAt(0).displayValue.toString()
                        binding.barcodeResultView.setText(intentData)
                        //finish()
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        iniBC()
    }
}

/*class BarcodeScan {

}

private fun BarcodeDetector.setProcessor(processor: Detector.Processor<Barcode>) {

}*/
