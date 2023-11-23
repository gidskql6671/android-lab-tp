package knu.dong.teamproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.databinding.ActivityLoginBinding
import knu.dong.teamproject.dto.LoginRequestDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                login(email, password)
            }
        }

        binding.titleBar.btnBack.visibility = View.INVISIBLE
        binding.titleBar.title.visibility = View.INVISIBLE

        initTextWatcher()
    }

    private suspend fun login(email: String, password: String) {
        if (validateLoginFormat(email, password)) {
            val response = HttpRequestHelper(this).post("api/users/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(email, password))
            }

            if (response?.status?.value?.div(100) == 2) {
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@LoginActivity, SelectChatbotActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


    private fun initTextWatcher() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.editEmail.text.toString()
                val password = binding.editPassword.text.toString()
                binding.btnLogin.isEnabled = validateEmail(email) && validatePassword(password)
            }
        }
        binding.editEmail.addTextChangedListener(textWatcher)
        binding.editPassword.addTextChangedListener(textWatcher)
    }

    private fun validatePassword(password: String): Boolean {
        return password.isNotBlank();
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateLoginFormat(email: String, password: String): Boolean {
        return validateEmail(email) && validatePassword(password)
    }
}