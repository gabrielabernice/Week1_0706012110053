package com.example.week1_0706012110053

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import Database.GlobalVar
import Model.User
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week1_0706012110053.databinding.ActivityAddEditBinding

class AddEdit : AppCompatActivity() {
    private lateinit var viewBind: ActivityAddEditBinding
    private lateinit var hewan: User
    var position = -1
    var image: String = ""

    private val GetResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val uri = it.data?.data
            if(uri != null){
                baseContext.getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            viewBind.cardimage.setImageURI(uri)
            image = uri.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        supportActionBar?.hide()
        getintent()
        listener()
    }
    private fun getintent(){
        position = intent.getIntExtra("position", -1)
        if(position != -1){
            val hewan = GlobalVar.listDataHewan[position]
            viewBind.toolbar2.title = "Edit Hewan"
            viewBind.addAnimal.text = "SIMPAN"
            viewBind.cardimage.setImageURI(Uri.parse(GlobalVar.listDataHewan[position].imageUri))
            viewBind.nama.editText?.setText(hewan.nama)
            viewBind.jenis.editText?.setText(hewan.jenis)
            viewBind.usia.editText?.setText(hewan.usia.toString())
        }
    }

    private fun listener(){
        viewBind.cardimage.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myIntent.type = "image/*"
            GetResult.launch(myIntent)
        }

        viewBind.addAnimal.setOnClickListener{
            var nama = viewBind.nama.editText?.text.toString().trim()
            var jenis = viewBind.jenis.editText?.text.toString().trim()
            var usia = viewBind.usia.editText?.text.toString().trim()

            hewan = User(nama, jenis, usia)
            checker()
        }

        viewBind.toolbar2.getChildAt(1).setOnClickListener {
            finish()
        }
    }

    private fun checker()
    {
        var isCompleted:Boolean = true

        hewan.imageUri = image

        if(hewan.nama!!.isEmpty()){
            viewBind.nama.error = "GA BOLEH KOSONG BESTIE"
            isCompleted = false
        }else{
            viewBind.nama.error = ""
        }

        if(hewan.jenis!!.isEmpty()){
            viewBind.jenis.error = "GA BOLEH KOSONG BESTIE"
            isCompleted = false
        }else{
            viewBind.jenis.error = ""
        }

        if(viewBind.usia.editText?.text.toString().isEmpty() || viewBind.usia.editText?.text.toString().toInt() < 0)
        {
            viewBind.usia.error = "GA BOLEH KOSONG BESTIE"
            isCompleted = false
        }

        if(isCompleted == true)
        {
            if(position == -1)
            {
//                hewan.usia = viewBind.usia.editText?.text.toString()
                GlobalVar.listDataHewan.add(hewan)

            }else
            {
//                hewan.usia = viewBind.usia.editText?.text.toString()
                GlobalVar.listDataHewan[position] = hewan
            }
            finish()
        }
    }
}