package io.suyong.kakaobridge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.suyong.kakaobridge.logger.Log
import io.suyong.kakaobridge.logger.LogAdapter
import io.suyong.kakaobridge.logger.LogType
import io.suyong.kakaobridge.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = LogAdapter()
        list_log.adapter = adapter
        list_log.layoutManager = LinearLayoutManager(this)

        Logger.setOnLogAddListener {
            adapter.notifyDataSetChanged()
        }

        fab_power.setOnClickListener {
            Logger.add(
                LogType.WARN,
                "Test",
                "test warn"
            )
        }

        Logger.add(
            LogType.INFO,
            "Start Application",
            "Main Activity started"
        )
    }
}
