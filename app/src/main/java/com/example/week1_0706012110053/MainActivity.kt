package com.example.week1_0706012110053

import Adapter.ListDataRVAdapter
import Database.GlobalVar
import Interface.CardListener
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week1_0706012110053.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CardListener {

    private lateinit var viewBind: ActivityMainBinding
    private val adapter = ListDataRVAdapter(GlobalVar.listDataHewan, this)
    private var listHewan: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        supportActionBar?.hide()
        permission()
        setupRecyclerView()
        listener()
    }

    override fun onResume() {
        super.onResume()
        listHewan = GlobalVar.listDataHewan.size
        if (listHewan == 0) {
            //1f = transparancy lvl
            viewBind.blank.alpha = 1f
        } else {
            //0f = ilang tulisannya
            viewBind.blank.alpha = 0f
        }
        //buat ngecheck buat update database kalo misal ada data yang ganti
        adapter.notifyDataSetChanged()
    }

    private fun listener() {
        viewBind.button.setOnClickListener {
            val myIntent = Intent(this, AddEdit::class.java)
            startActivity(myIntent)
        }
    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                GlobalVar.STORAGE_PERMISSION_CODE
            )
        } else {
            Toast.makeText(this, "Storage Permission already granted", Toast.LENGTH_SHORT).show()
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GlobalVar.STORAGE_PERMISSION_CODE
            )
        } else {
            Toast.makeText(this, "Storage Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GlobalVar.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false)
        viewBind.recyclerView.layoutManager = layoutManager   // Set layout
        viewBind.recyclerView.adapter = adapter   // Set adapter
    }

    override fun onCardClick(position: Int) {
        val myIntent = Intent(this, AddEdit::class.java).apply {
            putExtra("position", position)
        }
        startActivity(myIntent)
    }
}