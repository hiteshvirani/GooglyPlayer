package com.hiteshvirani.googlyplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hiteshvirani.googlyplayer.databinding.DetailsViewBinding
import com.hiteshvirani.googlyplayer.databinding.VideoItemViewBinding
import com.hiteshvirani.googlyplayer.databinding.VideoMoreFeaturesBinding
import kotlin.collections.ArrayList

class VideoAdapter(
    private val context: Context,
    private var videoList: ArrayList<Video>,
    private val isFolder: Boolean = false
) : RecyclerView.Adapter<VideoAdapter.MyHolder>() {
    class MyHolder(binding: VideoItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.idVideoName
        val folder = binding.idFolderName
        val duration = binding.idDuration
        val thumbnail = binding.idVideoThumbnail
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoItemViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration / 1000)
        Glide.with(context)
            .asBitmap()
            .load(videoList[position].artUri)
            .apply(RequestOptions().placeholder(R.color.thum_back_color).centerCrop())
            .into(holder.thumbnail)
        holder.root.setOnClickListener {
            when {
                videoList[position].id == PlayerActivity.nowPlayingId -> {
                    sendIntentData(pos = position, ref = "NowPlaying")
                }
                isFolder -> {
                    PlayerActivity.pipStatus = 1
                    sendIntentData(pos = position, ref = "FolderVideos")
                }
                MainActivity.search -> {
                    PlayerActivity.pipStatus = 2
                    sendIntentData(pos = position, ref = "SearchVideos")
                }
                else -> {
                    PlayerActivity.pipStatus = 3
                    sendIntentData(pos = position, ref = "AllVideos")
                }
            }
        }

        holder.root.setOnLongClickListener {

            val customDialog = LayoutInflater.from(context)
                .inflate(R.layout.video_more_features, holder.root, false)
            val bindingMF = VideoMoreFeaturesBinding.bind(customDialog)
            val dialog = MaterialAlertDialogBuilder(context).setView(customDialog)
                .create()
            dialog.show()

//            bindingMF.renameBtn.setOnClickListener{
//                requestPermissionR()
//                dialog.dismiss()
//                val customDialogRF = LayoutInflater.from(context).inflate(R.layout.rename_field, holder.root, false)
//                val bindingRF = RenameFieldBinding.bind(customDialogRF)
//                val dialogRF = MaterialAlertDialogBuilder(context).setView(customDialogRF)
//                    .setCancelable(false)
//                    .setPositiveButton("Rename"){self, _ ->
//                        val currentFile = File(videoList[position].path)
//                        val newName = bindingRF.renameFieldText.text
//                        if (newName != null && currentFile.exists() && newName.toString().isNotEmpty()){
//                            val newFile = File(currentFile.parentFile, newName.toString()+"."+currentFile.extension)
//                            if (currentFile.renameTo(newFile)){
//                                MediaScannerConnection.scanFile(context, arrayOf(newFile.toString()), arrayOf("video/*"), null)
//                                when{
//                                    MainActivity.search -> {
//                                        MainActivity.searchList[position].title = newName.toString()
//                                        MainActivity.searchList[position].path = newFile.path
//                                        MainActivity.searchList[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                    }
//                                    isFolder -> {
//                                        FolderActivity.currentFolderVideos[position].title = newName.toString()
//                                        FolderActivity.currentFolderVideos[position].path = newFile.path
//                                        FolderActivity.currentFolderVideos[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                        MainActivity.dataChanged = true
//                                    }
//                                    else ->{
//                                        MainActivity.videoList[position].title = newName.toString()
//                                        MainActivity.videoList[position].path = newFile.path
//                                        MainActivity.videoList[position].artUri = Uri.fromFile(newFile)
//                                        notifyItemChanged(position)
//                                    }
//                                }
//                            }else{
//                                Toast.makeText(context,"Permission Denied !!",Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                        self.dismiss()
//                    }
//                    .setNegativeButton("Cancel"){self, _ ->
//                        self.dismiss()
//                    }
//                    .create()
//                dialogRF.show()
//                bindingRF.renameFieldText.text = SpannableStringBuilder(videoList[position].title)
//                dialogRF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.CYAN)
//                )
//                dialogRF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.CYAN)
//                )
//            }

            bindingMF.shareBtn.setOnClickListener {
                dialog.dismiss()
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "video/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoList[position].path))
                ContextCompat.startActivity(
                    context,
                    Intent.createChooser(shareIntent, "Sharing Video File !!"),
                    null
                )
            }

            bindingMF.infoBtn.setOnClickListener {
                dialog.dismiss()
                val customDialogIF =
                    LayoutInflater.from(context).inflate(R.layout.details_view, holder.root, false)
                val bindingIF = DetailsViewBinding.bind(customDialogIF)
                val dialogIF =
                    MaterialAlertDialogBuilder(context, R.style.MyDialogTheme2).setView(
                        customDialogIF
                    )
                        .setCancelable(false)
                        .setPositiveButton("OK") { self, _ ->
                            self.dismiss()
                        }
                        .create()
                dialogIF.show()
                val infoText = SpannableStringBuilder().bold { append("DETAILS\n\nName: ") }
                    .append(videoList[position].title)
                    .bold { append("\n\nDuration: ") }
                    .append(DateUtils.formatElapsedTime(videoList[position].duration / 1000))
                    .bold { append("\n\nFile Size: ") }.append(
                        Formatter.formatShortFileSize(
                            context,
                            videoList[position].size.toLong()
                        )
                    )
                    .bold { append("\n\nLocation: ") }.append(videoList[position].path)
                bindingIF.detailTV.text = infoText
//                dialogIF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, R.attr.themeColor)
//                )
            }

//            bindingMF.deleteBtn.setOnClickListener{
//                requestPermissionR()
//                dialog.dismiss()
//                val dialogDF = MaterialAlertDialogBuilder(context)
//                    .setTitle("Delete Video?")
//                    .setMessage(videoList[position].title)
//                    .setPositiveButton("Yes"){self, _ ->
//                        val file = File(videoList[position].path)
//                        if(file.exists() && file.delete()){
//                            MediaScannerConnection.scanFile(context, arrayOf(file.path), arrayOf("video/*"), null)
//                            when{
//                                MainActivity.search -> {
//                                    MainActivity.dataChanged = true
//                                    videoList.removeAt(position)
//                                    notifyDataSetChanged()
//                                }
//                                isFolder -> {
//                                    MainActivity.dataChanged = true
//                                    FolderActivity.currentFolderVideos.removeAt(position)
//                                    notifyDataSetChanged()
//                                }
//                                else -> {
//                                    MainActivity.videoList.removeAt(position)
//                                    notifyDataSetChanged()
//                                }
//                            }
//                        }else{
//                            Toast.makeText(context, "permission Denied !!", Toast.LENGTH_SHORT).show()
//                        }
//                        self.dismiss()
//                    }
//                    .setNegativeButton("No"){self, _ ->
//                        self.dismiss()
//                    }
//                    .create()
//                dialogDF.show()
//                dialogDF.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.CYAN)
//                )
//                dialogDF.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
//                    MaterialColors.getColor(context, R.attr.themeColor, Color.CYAN)
//                )
//            }

            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    private fun sendIntentData(pos: Int, ref: String) {
        PlayerActivity.position = pos
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(searchList: ArrayList<Video>) {
        videoList = ArrayList()
        videoList.addAll(searchList)
        notifyDataSetChanged()
    }

    //for requesting android 11 or higher storage permission
//    private fun requestPermissionR(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if(!Environment.isExternalStorageManager()){
//                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                intent.addCategory("android.intent.category.DEFAULT")
//                intent.data = Uri.parse("package:${context.applicationContext.packageName}")
//                ContextCompat.startActivity(context, intent, null)
//            }
//        }
//    }
}