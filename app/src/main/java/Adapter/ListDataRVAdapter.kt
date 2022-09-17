package Adapter

import Database.GlobalVar
import Interface.CardListener
import Model.User
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.week1_0706012110053.AddEdit
import com.example.week1_0706012110053.R
import com.example.week1_0706012110053.databinding.CardanimalBinding

class ListDataRVAdapter(val listUser: ArrayList<User>, val cardListener: CardListener) :
    RecyclerView.Adapter<ListDataRVAdapter.viewHolder>() {

    class viewHolder(val itemView: View, val cardListener: CardListener) :
        RecyclerView.ViewHolder(itemView) {

        val binding = CardanimalBinding.bind(itemView)

        fun setData(data: User) {
            binding.cardnama.text = data.nama
            binding.cardjenis.text = data.jenis
            binding.cardusia.text = "Usia : "+ data.usia + " tahun"
            if (data.imageUri!!.isNotEmpty()) {
                binding.cardimage.setImageURI(Uri.parse(data.imageUri))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cardanimal, parent, false)
        return viewHolder(view, cardListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.setData(GlobalVar.listDataHewan[position])
        with(holder) {
            //kalo pencet button edit, bakal ke class addedit
            binding.editbtn.setOnClickListener {
                val myintent = Intent(it.context, AddEdit::class.java).apply {
                    putExtra("position", position)
                }
                it.context.startActivity(myintent)
            }
            //kalo pencet button delete, bakal ngeshow alert buat confirm delete
            binding.delbtn.setOnClickListener {
                val builder = AlertDialog.Builder(it.context)
                builder.setTitle("AWAS NYESEL")
                builder.setMessage("YAKIN TA MBO HAPUS?")

                builder.setPositiveButton(android.R.string.yes) { function, which ->

                    Toast.makeText(it.context,"Yah kehapus, byebye", Toast.LENGTH_SHORT).show()

                    GlobalVar.listDataHewan.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemChanged(position, itemCount)
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(it.context,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }
                builder.show()

            }
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

}