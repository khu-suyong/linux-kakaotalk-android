package io.suyong.kakaobridge

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.kakaobridge.logger.Log
import io.suyong.kakaobridge.logger.LogAdapter
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init logger
        val adapter = LogAdapter()
        list_log.adapter = adapter
        list_log.layoutManager = LinearLayoutManager(this)
        list_log.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        Logger.setOnLogAddListener {
            adapter.notifyDataSetChanged()
        }

        // init main layout
        textinput_server.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    URL(textinput_server.text.toString())

                    textinput_server.error = null
                    connect_server.isEnabled = true
                } catch (err: MalformedURLException) {
                    textinput_server.error = getString(R.string.invalid_format_url)
                    connect_server.isEnabled = false
                }
            }
        })

        connect_server.setOnClickListener {
            isConnected = !isConnected

            connect_load.visibility = View.VISIBLE
            textfield_server.isEnabled = false
            connect_server.isEnabled = false

            Handler().postDelayed({
                connect_load.visibility = View.INVISIBLE

                when(isConnected) {
                    true -> {
                        connect_image.setImageResource(R.drawable.ic_cloud_done_white_24dp)
                        connect_image.setColorFilter(Color.rgb(255, 235, 59))
                        connect_text.text = getString(R.string.ok_connect)
                    }
                    false -> {
                        connect_image.setImageResource(R.drawable.ic_cloud_off_white_24dp)
                        connect_image.setColorFilter(Color.rgb(0, 0, 0))
                        connect_text.text = getString(R.string.not_connect)
                    }
                }

                textfield_server.isEnabled = true
                connect_server.isEnabled = true
            }, 1000)
        }

        // init etc
        fab_power.setOnClickListener {
            Logger.add(
                LogType.DEBUG,
                "afhaerharhrharh",
                "asdgarhahawh"
            )
        }

        Logger.add(
            LogType.INFO,
            "Start Application",
            "Main Activity started"
        )
    }
}
