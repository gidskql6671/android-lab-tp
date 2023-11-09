package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import knu.dong.teamproject.adapter.ChatbotListAdapter
import knu.dong.teamproject.databinding.ActivityMainBinding
import knu.dong.teamproject.dto.Chatbot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this, ChatsActivity::class.java))
//        startActivity(Intent(this, SelectChatbotActivity::class.java))

    }
}