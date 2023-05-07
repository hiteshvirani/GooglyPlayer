package com.hiteshvirani.googlyplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hiteshvirani.googlyplayer.databinding.FolderItemViewBinding

class FoldersAdapter(private val context: Context, private var foldersList: ArrayList<Folder>) :
    RecyclerView.Adapter<FoldersAdapter.MyHolder>() {

    class MyHolder(binding: FolderItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val folderName = binding.idFolderNameFV
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(FolderItemViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.folderName.text = foldersList[position].folderName
        holder.root.setOnClickListener {
            val intent = Intent(context, FolderActivity::class.java)
            intent.putExtra("position", position)
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }


}