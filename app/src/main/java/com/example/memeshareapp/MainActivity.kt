package com.example.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var current_memeurl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme()//fuction to call
    {
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        progressBar.visibility = View.VISIBLE // creating progress bar visible
        val url = "https://meme-api.com/gimme" // url of api
        val memeImage: ImageView = findViewById(R.id.memeImage)
// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(//Api call request
            Request.Method.GET, url, null,
            { response ->
                current_memeurl = response.getString("url")//to get the url of current meme from Api
                Glide.with(this).load(current_memeurl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(//if image loading failed
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE//to remove the visiblity of progress bar
                        return false
                    }

                    override fun onResourceReady(//if
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImage)
            },
            { error ->
                // TODO: Handle error
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)//adding request to queue
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)/// creating a intent to share meme
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this cool meme \n$current_memeurl")//sending text through intent
        val chooser = Intent.createChooser(intent, "Share using...")
        startActivity(chooser)
    }
}