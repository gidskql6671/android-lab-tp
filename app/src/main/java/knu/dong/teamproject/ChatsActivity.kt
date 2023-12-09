package knu.dong.teamproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.socket.emitter.Emitter
import knu.dong.teamproject.adapter.ChatsAdapter
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.common.WebSocketHelper
import knu.dong.teamproject.common.getSerializable
import knu.dong.teamproject.databinding.ActivityChatsBinding
import knu.dong.teamproject.dto.Chat
import knu.dong.teamproject.dto.Chatbot
import knu.dong.teamproject.dto.GetChatsDto
import knu.dong.teamproject.dto.SendChatReqDto
import knu.dong.teamproject.dto.SendChatResDto
import knu.dong.teamproject.dto.SendChatUsingSocketReqDto
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
    private lateinit var userInfo: SharedPreferences
    private val chats = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()
        userInfo = getSharedPreferences("user_info", MODE_PRIVATE)

        val userId = userInfo.getLong("id", -1)
        val chatbot = intent.getSerializable("chatbot", Chatbot::class.java)
            ?: run {
                finish()
                return
            }

        binding.titleBar.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding.titleBar.title.text = chatbot.name
        binding.titleBar.btnAccount.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)

            startActivity(intent)
        }

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

//            sendChat(chatbot, message)
            sendChatUsingSocket(chatbot, userId, message)
        }

        getChatbotChats(chatbot, userId)
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

    private var isFirst = true
    private fun sendChatUsingSocket(chatbot: Chatbot, userId: Long, message: String) {
        launch(Dispatchers.Main) {
            try {
                val socket = WebSocketHelper().socket
                socket.connect()

                val chatbotId = chatbot.id
                socket.on("error", onErrorMessage)
                socket.on("chats/messages/$chatbotId", onChatbotMessage)

                isFirst = true
                val sendData = Gson().toJson(SendChatUsingSocketReqDto(chatbotId, userId, message))
                socket.emit("chats/messages", sendData)
            }catch (err: Exception) {
                Log.d("dong", err.stackTraceToString())
            }
        }
    }

    private val onChatbotMessage = Emitter.Listener { args ->
        launch(Dispatchers.Main) {
            if (isFirst) {
                chats.add(Chat(args[0].toString(), false))
                binding.recyclerView.apply {
                    adapter?.notifyItemInserted(chats.size - 1)
                    smoothScrollToPosition(chats.size - 1)
                }
                isFirst = false

                return@launch
            }
            if (args[0].toString().isEmpty()) {
                binding.recyclerView.smoothScrollToPosition(chats.size - 1)

                return@launch
            }

            chats[chats.size - 1].message += args[0]
            binding.recyclerView.adapter?.notifyItemChanged(chats.size - 1)
        }
    }


    private val onErrorMessage = Emitter.Listener { args ->
        launch(Dispatchers.Main) {
            val msg = args[0].toString()
            Log.e("dong", msg)

            Toast.makeText(this@ChatsActivity, msg, Toast.LENGTH_SHORT).show()
            chats.removeLast()
            binding.recyclerView.apply {
                adapter?.notifyItemRemoved(chats.size)
                smoothScrollToPosition(chats.size - 1)
            }
        }
    }
}