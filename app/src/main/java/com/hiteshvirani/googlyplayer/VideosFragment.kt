package com.hiteshvirani.googlyplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiteshvirani.googlyplayer.databinding.FragmentVideosBinding

class VideosFragment : Fragment(){

    private lateinit var adapter: VideoAdapter
    private lateinit var binding: FragmentVideosBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().theme.applyStyle(MainActivity.themeList[MainActivity.themeIndex], true)
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        binding = FragmentVideosBinding.bind(view)
        binding.idVideosRV.setHasFixedSize(true)
        binding.idVideosRV.setItemViewCacheSize(10)
        binding.idVideosRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = VideoAdapter(requireContext(), MainActivity.videoList)
        binding.idVideosRV.adapter = adapter
        binding.idTotalVideos.text = "Total Videos: ${MainActivity.videoList.size}"

        binding.root.setOnRefreshListener {

            //why i passing is is Refresh false - because if is Refresh true than folders are adding multiple times
            MainActivity.videoList = getAllVideo(requireContext(), isRefresh = false)

            adapter.updateList(MainActivity.videoList)
            binding.idTotalVideos.text = "Total Videos: ${MainActivity.videoList.size}"


            binding.root.isRefreshing = false
        }

        binding.nowPlayingBtn.setOnClickListener {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("class", "NowPlaying")
            startActivity(intent)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SoonBlockedPrivateApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_view, menu)

        val searchView = menu.findItem(R.id.searchView)?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {

                    MainActivity.searchList = ArrayList()
                    for (video in MainActivity.videoList) {
                        if (video.title.lowercase().contains(newText.lowercase()))
                            MainActivity.searchList.add(video)
                    }
                    MainActivity.search = true
                    adapter.updateList(searchList = MainActivity.searchList)
                }
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (PlayerActivity.position != -1) binding.nowPlayingBtn.visibility = View.VISIBLE
        if (MainActivity.adapterChanged) adapter.notifyDataSetChanged()
        MainActivity.adapterChanged = false
    }
}