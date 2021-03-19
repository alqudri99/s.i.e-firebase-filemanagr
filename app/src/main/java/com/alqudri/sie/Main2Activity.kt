package com.alqudri.sie

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.lang.*

class Main2Activity : AppCompatActivity() {
    var folderList = arrayListOf<Folder>()
    var itemsList = arrayListOf<Items>()
    lateinit var listRef1: StorageReference
    private var nama: String? = null
    private var hal: String? = null
    var i = 0
    var ha = FirebaseFirestore.getInstance().collection("test").document("ha")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        rv1.layoutManager = LinearLayoutManager(this)
        pb1.visibility = View.GONE

        //make a function call for the first time
        checkData(null)
        ha.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
                Log.d("testing", "${p0?.get("nama")}")

                if (p0?.get("nama").toString() == "true") {
                    checkData(listRef1.path)
                }
            }
        })


    }
//Check for newes data from the internet, data will passing trhough this function when it trigger by another function
    fun checkData(data: String?) {
        if (data == null) {
            listRef1 = FirebaseStorage.getInstance().getReference("/")
        } else {
            listRef1 = FirebaseStorage.getInstance().getReference("/" + data)
        }
        readData()

    }

    fun readData() {

        try {
            listRef1.listAll().addOnSuccessListener(object : OnSuccessListener<ListResult> {
                override fun onSuccess(p0: ListResult) {
                    FirebaseFirestore.getInstance().collection("test").document("ha")
                        .set(Folder("false"))
                    folderList.clear()
                    itemsList.clear()

                    for (prefix in p0.prefixes) {
                        folderList.add(Folder(prefix.name, prefix.path))
                    }

                    for (item in p0.items) {
                        item.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {

                            override fun onSuccess(p0: Uri?) {
                                itemsList.add(Items(item.name, p0.toString(), p0!!.path))
                                (rv1.adapter as ItemAdapter).notifyDataSetChanged()
                            }

                        })

                    }
                    Log.d("ref3", "${folderList.toString()}  ${itemsList.toString()}")

                    val adapter = ItemAdapter(itemsList, object : ItemAdapter.OnItemClickListener {
                        override fun OnItemClick(items: Items, folder: Folder) {
                            hal = folder.path

                            Log.d("refff", hal)
                            checkData(folder.path)
                        }
                    }, folderList, object : ItemAdapter.OnLongClickListener {
                        override fun OnLongClick(items: Items) {
                            var preferences = getSharedPreferences("SIEAPP", Context.MODE_PRIVATE)
                            var reference = preferences.edit()

                            reference.putString("user", items.url)
                            reference.putString("path", listRef1.path)
                            reference.apply()
                            reference.commit()

                            val fm = supportFragmentManager
                            val alertDialog = MyAlertDialogFragment.newInstance("Konfirmasi")
                            alertDialog.show(fm, "fragment_alert")
                        }
                    }, this@Main2Activity)

                    rv1.adapter = adapter
                    (rv1.adapter as ItemAdapter).notifyDataSetChanged()
                }
            })
        } catch (e: NullPointerException) {
            Log.d("da", e.toString())
        }
    }


    override fun onBackPressed() {
//        super.onBackPressed()

        if (hal != null) {
            var uri = listRef1.path!!.replace(listRef1.name!!, "")
            var navigation = uri.removeRange(uri.lastIndex..uri.lastIndex)
            if(listRef1.path == "/"){
                if(i == 1){
                    super.onBackPressed()
                }else{
                    i++
                    Toast.makeText(this, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT).show()
                }
            }
            checkData(navigation)
            folderList.clear()
            itemsList.clear()
        } else {
            super.onBackPressed()
//            Toast.makeText(this, "kk" + hal + nama, Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }

    }

//open file and get metada from it
    fun buka() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            return
        }
        //creating an intent for file chooser
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 22)
    }

//receive the metadata from buka() function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Toast.makeText(this, "File Dipilih", Toast.LENGTH_SHORT).show()
            //if a file is selected
            if (data.data != null)
                uploadFile(data.data as Uri, listRef1.path)
        } else {
            Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show();
            pb1.visibility = View.GONE
        }
    }


    private fun uploadFile(data: Uri, path: String) {
        pb1.visibility = View.VISIBLE
        var storage: FirebaseStorage = FirebaseStorage.getInstance()
        var f = getFileName(data)
        var g: StorageReference = storage.getReference("${listRef1.path}/$f")
        var uploadTask = g.putFile(data)
        Log.d("filename", f)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "Gagal", Toast.LENGTH_LONG).show()
        }
            .addOnSuccessListener { taskSnapshot -> // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Toast.makeText(this, "Berhasil", Toast.LENGTH_LONG).show()
                pb1.visibility = View.GONE
                checkData(listRef1.path)
            }
    }

    //get filename from metadata that was choosen before
    fun getFileName(data: Uri): String {
        val cursor = this.contentResolver.query(data, null, null, null, null)
        var fileName = ""
        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).let { nameIndex ->
            cursor.moveToFirst()

            fileName = cursor.getString(nameIndex)
            cursor.close()
        }
        return fileName
    }

    //to trigger buka() function *this the first time function that trigger by open Button
    fun openFile(view: View) {
        buka()
    }

    // button trigger to make log out function
    fun keluar(view: View) {
        var pre = getSharedPreferences("SIEAPP", Context.MODE_PRIVATE)
        pre.edit().remove("saver").apply()
        pre.edit().commit()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}
