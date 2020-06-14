package io.suyong.kakaobridge

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.provider.Settings
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
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        var uuid: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        uuid = preference.getString("uuid", "") ?: ""

        if (uuid == "") {
            val editor = preference.edit()
            editor.putString("uuid", Util.Base62.encoding(Date().time))
            editor.apply()

            uuid = preference.getString("uuid", "") ?: ""
        }

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

            changeStatus(2)
        }

        debug_button.setOnClickListener {
            NetworkManager.emit(
                "message",
                mapOf(
                    "room" to "Test room name",
                    "sender" to "Test Sender name",
                    "text" to "anything else?"
                )
            )
        }

        NetworkManager.on(Socket.EVENT_CONNECT) {
            runOnUiThread {
                changeStatus(0)
            }
        }
        NetworkManager.on(Socket.EVENT_DISCONNECT) {
            runOnUiThread {
                changeStatus(1)
            }
        }

        id_view.text = getString(R.string.my_id, uuid)
    }

    private fun changeStatus(enable: Int) = when(enable) {
        0 -> {
            connect_load.visibility = View.INVISIBLE

            textfield_server.isEnabled = true

            connect_image.setImageResource(R.drawable.ic_cloud_done_white_24dp)
            connect_image.setColorFilter(Color.rgb(255, 235, 59))
            connect_text.text = getString(R.string.ok_connect)
        }
        1 -> {
            connect_load.visibility = View.INVISIBLE

            textfield_server.isEnabled = true

            connect_image.setImageResource(R.drawable.ic_cloud_off_white_24dp)
            connect_image.setColorFilter(Color.rgb(0, 0, 0))
            connect_text.text = getString(R.string.not_connect)
        }
        else -> {
            connect_load.visibility = View.VISIBLE

            textfield_server.isEnabled = false

            connect_image.setImageResource(R.drawable.ic_cloud_off_white_24dp)
            connect_image.setColorFilter(Color.rgb(0, 0, 0))
            connect_text.text = getString(R.string.connecting)
        }
    }
}
