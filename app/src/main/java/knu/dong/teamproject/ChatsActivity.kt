package knu.dong.teamproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import knu.dong.teamproject.adapter.ChatsAdapter
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.common.getSerializable
import knu.dong.teamproject.databinding.ActivityChatsBinding
import knu.dong.teamproject.dto.Chat
import knu.dong.teamproject.dto.Chatbot
import knu.dong.teamproject.dto.GetChatsDto
import knu.dong.teamproject.dto.SendChatReqDto
import knu.dong.teamproject.dto.SendChatResDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChatsActivity: AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var binding: ActivityChatsBinding
    private lateinit var chatbot: Chatbot
    private val chats = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()

        chatbot = intent.getSerializable("chatbot", Chatbot::class.java)
            ?: run {
                finish()
                return
            }

        binding.titleBar.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding.titleBar.title.text = chatbot.name

        initRecyclerView()

        binding.btnSend.setOnClickListener {
            val message = binding.editQuestion.text.toString()
            if (message.isBlank()) {
                return@setOnClickListener
            }
            binding.editQuestion.setText("")
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )


            chats.add(Chat(message, true))
            binding.recyclerView.apply {
                adapter?.notifyItemInserted(chats.size - 1)
                smoothScrollToPosition(chats.size - 1)
            }

            sendChat(chatbot, message)
        }

        getChatbotChats(chatbot, 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = ChatsAdapter(chats)
            it.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun getChatbotChats(chatbot: Chatbot, userId: Long) {
        launch(Dispatchers.Main) {
            val resChats =
                HttpRequestHelper(this@ChatsActivity)
                    .get("api/chatbots/chats", GetChatsDto::class.java) {
                        url {
                            parameters.append("chatbotId", chatbot.id.toString())
                            parameters.append("userId", userId.toString())
                        }
                    }
                    ?: GetChatsDto()

            chats.addAll(resChats.chats)
            if (chats.isNotEmpty()) {
                binding.recyclerView.apply {
                    adapter?.notifyItemRangeInserted(0, chats.size)
                    smoothScrollToPosition(chats.size - 1)
                }
            }

        }
    }


    private fun sendChat(chatbot: Chatbot, message: String) {
        launch(Dispatchers.Main) {
            val resChat = HttpRequestHelper(this@ChatsActivity).post("api/chatbots/chats", SendChatResDto::class.java) {
                contentType(ContentType.Application.Json)
                setBody(SendChatReqDto(chatbot.id, message))
            }

            chats.add(Chat(resChat?.reply ?: "다시 시도해주세요.", false))
            binding.recyclerView.apply {
                adapter?.notifyItemInserted(chats.size - 1)
                smoothScrollToPosition(chats.size - 1)
            }
        }
    }
}