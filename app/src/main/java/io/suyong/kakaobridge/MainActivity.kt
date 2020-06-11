package io.suyong.kakaobridge

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.socket.client.Socket
import io.suyong.kakaobridge.logger.LogAdapter
import io.suyong.kakaobridge.logger.Logger
import io.suyong.kakaobridge.network.NetworkManager
import io.suyong.kakaobridge.network.NetworkService
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

        Logger.activity = this
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

                    fab_power.show()
                    textinput_server.error = null
                } catch (err: MalformedURLException) {
                    fab_power.hide()
                    textinput_server.error = getString(R.string.invalid_format_url)
                }
            }
        })

        // init etc
        fab_power.setOnClickListener {
            val intent = Intent(this, NetworkService::class.java)
            intent.action = NetworkService.CONNECT
            intent.putExtra("url", textinput_server.text)

            when (NetworkService.isRunning) {
                true -> stopService(intent)
                false -> startService(intent)
            }
        }

        debug_button.setOnClickListener {
            val intent = Intent(this, NetworkService::class.java)
            intent.action = NetworkService.EMIT
            intent.putExtra("emit", "message")

            startService(intent)
        }

        NetworkManager.on(Socket.EVENT_CONNECT) {
            runOnUiThread {
                change(true)
            }
        }
        NetworkManager.on(Socket.EVENT_DISCONNECT) {
            runOnUiThread {
                change(false)
            }
        }
    }

    fun change(enable: Boolean) { // TODO: rename
        if (enable) {
            connect_load.visibility = View.INVISIBLE

            textfield_server.isEnabled = true

            connect_image.setImageResource(R.drawable.ic_cloud_done_white_24dp)
            connect_image.setColorFilter(Color.rgb(255, 235, 59))
            connect_text.text = getString(R.string.ok_connect)
        } else {
            connect_load.visibility = View.VISIBLE

            textfield_server.isEnabled = false

            connect_image.setImageResource(R.drawable.ic_cloud_off_white_24dp)
            connect_image.setColorFilter(Color.rgb(0, 0, 0))
            connect_text.text = getString(R.string.not_connect)
        }
    }
}
