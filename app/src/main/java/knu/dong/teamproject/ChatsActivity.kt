package knu.dong.teamproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import knu.dong.teamproject.adapter.ChatsAdapter
import knu.dong.teamproject.databinding.ActivityChatsBinding
import knu.dong.teamproject.dto.Chat

class ChatsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding
    private val chats = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chats.addAll(0,
            listOf(
                Chat("안녕", true),
                Chat("안녕하세요", false)
            )
        )

        initRecyclerView()
    }


    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = ChatsAdapter(chats)
            it.layoutManager = LinearLayoutManager(this)
        }
    }

}