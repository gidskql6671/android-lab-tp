package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import knu.dong.teamproject.adapter.ChatbotListAdapter
import knu.dong.teamproject.databinding.ActivitySelectChatbotBinding
import knu.dong.teamproject.dto.Chatbot

class SelectChatbotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySelectChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //임시 데이터 입력
        val chatbotItemList = mutableListOf<Chatbot>()
        chatbotItemList.add(Chatbot(1,"lawyer", "변호사"))
        chatbotItemList.add(Chatbot(2, "doctor", "의사"))

        val adapter = ChatbotListAdapter(this, chatbotItemList)
        binding.listView.adapter = adapter

        binding.listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ChatsActivity::class.java)
            intent.putExtra("chatbot", adapter.getItem(position))

            startActivity(intent)
        }

        binding.titleBar.btnBack.visibility = View.INVISIBLE
        binding.titleBar.title.text = "챗봇 선택"
        binding.titleBar.btnAccount.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)

            startActivity(intent)
        }
    }
}