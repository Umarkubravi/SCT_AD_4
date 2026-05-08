package com.example.qrcodeapp

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter

class MainActivity : AppCompatActivity() {

    private lateinit var etText: EditText
    private lateinit var btnGenerate: Button
    private lateinit var btnScan: Button
    private lateinit var ivQR: ImageView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etText = findViewById(R.id.etText)
        btnGenerate = findViewById(R.id.btnGenerate)
        btnScan = findViewById(R.id.btnScan)
        ivQR = findViewById(R.id.ivQR)
        tvResult = findViewById(R.id.tvResult)

        btnGenerate.setOnClickListener {

            val text = etText.text.toString()

            if (text.isNotEmpty()) {
                generateQRCode(text)
            } else {
                Toast.makeText(this, "Enter text first", Toast.LENGTH_SHORT).show()
            }
        }

        btnScan.setOnClickListener {

            val integrator = IntentIntegrator(this)

            integrator.setPrompt("Scan a QR Code")
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }
    }

    private fun generateQRCode(text: String) {

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)

        val width = bitMatrix.width
        val height = bitMatrix.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {

                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }

        ivQR.setImageBitmap(bitmap)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (result != null) {

            if (result.contents != null) {
                tvResult.text = "Result: ${result.contents}"
            } else {
                tvResult.text = "Scan Cancelled"
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}