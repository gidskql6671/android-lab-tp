package knu.dong.teamproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import knu.dong.teamproject.adapter.ChatsAdapter
import knu.dong.teamproject.common.getSerializable
import knu.dong.teamproject.databinding.ActivityChatsBinding
import knu.dong.teamproject.dto.Chat
import knu.dong.teamproject.dto.Chatbot

class ChatsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding
    private val chats = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatbot = intent.getSerializable("chatbot", Chatbot::class.java)
        if (chatbot == null) {
            finish()
            return
        }

        chats.addAll(0,
            listOf(
                Chat("안녕", true),
                Chat("안녕하세요", false)
            )
        )

        initRecyclerView()

        binding.titleBar.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding.titleBar.title.text = chatbot.name
    }


    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = ChatsAdapter(chats)
            it.layoutManager = LinearLayoutManager(this)
        }
    }

}