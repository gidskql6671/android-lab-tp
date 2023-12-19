package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import knu.dong.teamproject.adapter.ChatbotListAdapter
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.databinding.ActivitySelectChatbotBinding
import knu.dong.teamproject.dto.Chatbot
import knu.dong.teamproject.dto.GetChatbotsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SelectChatbotActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var binding: ActivitySelectChatbotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        binding.titleBar.btnBack.visibility = View.INVISIBLE
        binding.titleBar.title.text = "챗봇 선택"
        binding.titleBar.btnAccount.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)

            startActivity(intent)
        }

        getChatbotRoles()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun getChatbotRoles() {
        launch(Dispatchers.Main) {
            val res = HttpRequestHelper(this@SelectChatbotActivity)
                .get("api/chatbots", GetChatbotsDto::class.java)
                ?: GetChatbotsDto()

            val adapter = ChatbotListAdapter(this@SelectChatbotActivity, res.chatbots)
            binding.listView.adapter = adapter

            binding.listView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val intent = Intent(this@SelectChatbotActivity, ChatsActivity::class.java)
                    intent.putExtra("chatbot", adapter.getItem(position))

                    startActivity(intent)
                }
        }
    }
}