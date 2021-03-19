package com.alqudri.sie

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

internal class MyAlertDialogFragment: DialogFragment() {
    var refData: String? =null
    var pathData: String? =null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val title = arguments!!.getString("title")
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context as Context)
        var f: SharedPreferences = context!!.getSharedPreferences("SIEAPP", Context.MODE_PRIVATE)
        refData =  f.getString("user", "No Data")
        pathData =  f.getString("path", "No Data")
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage("Apakah kamu yakin ingin menghapus File ini?")
        alertDialogBuilder.setPositiveButton("Ya",
            DialogInterface.OnClickListener { dialog, which ->
                // on success
                var storage: FirebaseStorage = FirebaseStorage.getInstance()
                var g: StorageReference = storage.getReferenceFromUrl(refData!!)
                g.delete().addOnSuccessListener {
                    FirebaseFirestore.getInstance().collection("test").document("ha").set(Folder("true"))

                }


            })
        alertDialogBuilder.setNegativeButton("Tidak",
            DialogInterface.OnClickListener { dialog, which ->
                if (dialog != null ) {
                    dialog.dismiss()
                }
            })
        return alertDialogBuilder.create()
    }

    companion object {
        fun newInstance(title: String?): MyAlertDialogFragment {
            val frag = MyAlertDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            frag.arguments = args
            return frag
        }
    }

}