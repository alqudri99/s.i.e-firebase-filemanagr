package com.alqudri.sie

import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.*
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import java.io.File


class PDFViwerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)
        pdfView = findViewById(R.id.PDFView)
        progressBar = findViewById(R.id.pb)
        val intent = intent
        val position = intent.getIntExtra("position", 0)
        progressBar.setVisibility(View.VISIBLE)
        FileLoader.with(this)
            .load("MainActivity.list!!.get(position).pdfUrl")
            .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
            .asFile(object : FileRequestListener<File?> {
                override fun onLoad(
                    request: FileLoadRequest,
                    response: FileResponse<File?>
                ) {
                    progressBar.setVisibility(View.GONE)
                    val pdfFile: File? = response.getBody()
                    pdfView.fromUri(Uri.parse("MainActivity!!.list!!.get(position).pdfUrl"))
                        .password(null)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .onDraw(object : OnDrawListener {
                            override fun onLayerDrawn(
                                canvas: Canvas?,
                                pageWidth: Float,
                                pageHeight: Float,
                                displayedPage: Int
                            ) {
                            }
                        }).onDrawAll(object : OnDrawListener {
                            override fun onLayerDrawn(
                                canvas: Canvas?,
                                pageWidth: Float,
                                pageHeight: Float,
                                displayedPage: Int
                            ) {
                            }
                        }).onPageError(OnPageErrorListener { page, t ->
                            Toast.makeText(
                                this@PDFViwerActivity,
                                "Error:" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }).onPageChange(object : OnPageChangeListener {
                            override fun onPageChanged(page: Int, pageCount: Int) {}
                        }).onTap(OnTapListener { true })
                        .onRender(OnRenderListener { nbPages, pageWidth, pageHeight -> pdfView.fitToWidth() })
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.WHITE)
                        .load()
                }

                override fun onError(
                    request: FileLoadRequest,
                    t: Throwable
                ) {
                    Toast.makeText(this@PDFViwerActivity, "" + t.message, Toast.LENGTH_SHORT)
                        .show()
                    progressBar.setVisibility(View.GONE)
                }
            })
    }
}