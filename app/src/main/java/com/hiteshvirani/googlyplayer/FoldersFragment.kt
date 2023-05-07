package com.hiteshvirani.googlyplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiteshvirani.googlyplayer.databinding.FragmentFoldersBinding

class FoldersFragment : Fragment() {

    private lateinit var binding: FragmentFoldersBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().theme.applyStyle(MainActivity.themeList[MainActivity.themeIndex], true)
        val view = inflater.inflate(R.layout.fragment_folders, container, false)
        binding = FragmentFoldersBinding.bind(view)
        binding.idFolderRV.setHasFixedSize(true)
        binding.idFolderRV.setItemViewCacheSize(10)
        binding.idFolderRV.layoutManager = LinearLayoutManager(requireContext())
        binding.idFolderRV.adapter = FoldersAdapter(requireContext(), MainActivity.folderList)
        binding.idTotalFolders.text = "Total Folders: ${MainActivity.folderList.size}"

        return view
    }

}