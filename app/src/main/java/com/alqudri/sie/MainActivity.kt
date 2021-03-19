package com.alqudri.sie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.daftar.*
import kotlinx.android.synthetic.main.masuk.*


class MainActivity : AppCompatActivity() {
    var give = true
    var nav = true
    lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_1.text = "Masuk"
        preferences = getSharedPreferences("SIEAPP", Context.MODE_PRIVATE)
        masuk.visibility = View.VISIBLE
        daftar.visibility = View.GONE
        var checKData = preferences.getInt("saver", 0)

        if (checKData == 1) {
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            startActivity(intent)
        }

        btn_1.setOnClickListener {
            if (nav) {
                if (edt_nama_masuk.text.isNotEmpty() && edt_pasword_masuk.text.isNotEmpty()) {
                    var password = preferences.getString("password", "kosong")
                    var email = preferences.getString("email", "kosong")
                    if (!password.isNullOrEmpty() && !email.isNullOrEmpty()) {
                        if (email == edt_nama_masuk.text.toString() && password == edt_pasword_masuk.text.toString()) {
                            masuk(true)
                        }else{
                            Toast.makeText(this, "Email atau Password yang Anda masukkan salah", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (edt_email_daftar.text.isNotEmpty() && edt_jabatan_daftar.text.isNotEmpty() && edt_pasword_daftar.text.isNotEmpty() && edt_nama_Daftar.text.isNotEmpty()) {

                    var nama = preferences.getString("nama", "kosong")
                    var email = preferences.getString("email", "kosong")
                    var password = preferences.getString("password", "kosong")
                    var jabatan = preferences.getString("jabatan", "kosong")


                    preferences.edit().putString("nama", "${edt_nama_Daftar.text}").apply()
                    preferences.edit().putString("jabatan", "${edt_jabatan_daftar.text}").apply()
                    preferences.edit().putString("password", "${edt_pasword_daftar.text}").apply()
                    preferences.edit().putString("email", "${edt_email_daftar.text}").apply()
                    preferences.edit().commit()
                    masuk.visibility = View.VISIBLE
                    daftar.visibility = View.GONE
                    nav = true
                    btn_1.text = "Masuk"
                    btn_1.isClickable = true
                    Toast.makeText(this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }


        btn_daftar.setOnClickListener {
            if (masuk.visibility == View.GONE) {
                masuk.visibility = View.VISIBLE
                daftar.visibility = View.GONE
                tv_keterangan.text = "Belum Mempunyai Akun?"
                btn_1.text = "Masuk"
                btn_daftar.text = "Daftar"
                nav = true
            } else {
                masuk.visibility = View.GONE
                daftar.visibility = View.VISIBLE
                tv_keterangan.text = "Sudah Mempunyai Akun?"
                btn_daftar.text = "Masuk"
                btn_1.text = "Daftar"
                nav = false
            }
        }

    }


    fun masuk(data: Boolean) {
        var reference = preferences.edit()
        reference.putInt("saver", 1)
        reference.apply()
        reference.commit()
        val intent =
            Intent(this@MainActivity, Main2Activity::class.java)
        Toast.makeText(
            this@MainActivity,
            "Login...",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
    }


}

