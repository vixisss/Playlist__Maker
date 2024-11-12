package com.example.playlist__maker


import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var backButton: Toolbar
    private lateinit var artworkUrl312: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genreTrack: TextView
    private lateinit var country: TextView
    private lateinit var albumTrack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        backButton = findViewById(R.id.player_toolbar)
        artworkUrl312 = findViewById(R.id.artworkUrl100)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        collectionName = findViewById(R.id.collectionName)
        releaseDate = findViewById(R.id.releaseDate)
        genreTrack = findViewById(R.id.primaryGenreName)
        country = findViewById(R.id.country)
        albumTrack = findViewById(R.id.player_album)

        exit()
        showPlayer()
    }

    private fun exit(){
        backButton.setOnClickListener {
            finish()
        }
    }


    private fun showPlayer(){
        val intent = intent
        val gson = Gson()
        val trackString = intent.getStringExtra("track")
        val track = gson.fromJson(trackString, Track::class.java)
        val bigUrl = track.getCoverArtwork()

        trackName.text = track.trackName
        artistName.text = track.collectionName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        genreTrack.text = track.primaryGenreName
        country.text = track.country
        releaseDate.text = track.releaseDate.substringBefore('-')

        if (track.collectionName.isNotEmpty()) {
            collectionName.visibility = View.VISIBLE
            albumTrack.visibility = View.VISIBLE
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            albumTrack.visibility = View.GONE
        }


        Glide.with(this)
            .load(bigUrl)
            .placeholder(R.drawable.big_placeholder)
            .error(R.drawable.big_placeholder)
            .transform(RoundedCorners(dpToPx(8F, this)))
            .into(artworkUrl312)
    }


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}