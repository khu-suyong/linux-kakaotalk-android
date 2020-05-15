package io.suyong.kakaobridge.logger

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.suyong.kakaobridge.R
import kotlinx.android.synthetic.main.item_log.view.*
import java.text.SimpleDateFormat

class LogAdapter : RecyclerView.Adapter<LogViewHolder>() {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val ctx = parent.context
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.item_log, parent, false)

        return LogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Logger.list.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.title.text = Logger.list[position].title
        holder.icon.setImageResource(
            when(Logger.list[position].type) {
                LogType.INFO -> R.drawable.ic_log_info
                LogType.WARN -> R.drawable.ic_log_warn
                LogType.ERROR -> R.drawable.ic_log_error
                LogType.DEBUG -> R.drawable.ic_log_debug
                else -> R.drawable.ic_log_error
            }
        )
        holder.content.text = Logger.list[position].content

        val format = SimpleDateFormat("yyyy.MM.dd hh:mm:ss")
        holder.date.text = format.format(Logger.list[position].date)
    }
}

class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.log_title
    val icon: ImageView = itemView.log_image
    val content: TextView = itemView.log_content
    val date: TextView = itemView.log_date
}