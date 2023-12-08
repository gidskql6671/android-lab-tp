package knu.dong.teamproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import knu.dong.teamproject.common.HttpRequestHelper
import knu.dong.teamproject.databinding.ActivitySignupBinding
import knu.dong.teamproject.dto.EmailVerify
import knu.dong.teamproject.dto.Signup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SignupActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    private lateinit var binding: ActivitySignupBinding
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()

        binding.btnSignup.isEnabled = false

        val signupFormWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.email.text.toString()
                val verifyCode = binding.emailVerify.text.toString()
                val password = binding.password.text.toString()
                val confirmPassword = binding.passwordVerify.text.toString()

                val isEmailValid = isValidEmail(email)
                val isVerifyCodeValid = isValidVerifyCode(verifyCode)
                val isPasswordValid =
                    isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)

                val errorMessage = when {
                    !isEmailValid -> "올바른 이메일 형식이 아닙니다."
                    !isVerifyCodeValid -> "올바른 인증코드 형식이 아닙니다."
                    !isPasswordValid -> "비밀번호가 일치하지 않습니다."
                    else -> null // 폼의 모든 필드의 입력 값이 올바른 경우
                }

                binding.errorTextView.text = errorMessage
                binding.errorTextView.visibility =
                    if (errorMessage != null) View.VISIBLE else View.INVISIBLE

                val isAllFieldValid = isEmailValid && isVerifyCodeValid && isPasswordValid
                binding.btnSignup.isEnabled = isAllFieldValid
            }
        }
        binding.email.addTextChangedListener(signupFormWatcher)
        binding.emailVerify.addTextChangedListener(signupFormWatcher)
        binding.password.addTextChangedListener(signupFormWatcher)
        binding.passwordVerify.addTextChangedListener(signupFormWatcher)

        binding.btnVerifyEmail.setOnClickListener {
            val email = binding.email.text.toString()
            if (isValidEmail(email)) {
                verifyEmail(email)
                Toast.makeText(this@SignupActivity, "입력하신 이메일로 인증코드가 발송 되었습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@SignupActivity, "이메일 형식이 올바른지 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.email.text.toString()
            val verifyCode = binding.emailVerify.text.toString()
            val password = binding.password.text.toString()

            signUp(email, verifyCode, password)
        }

        binding.titleBar.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    private fun signUp(email: String, verify: String, password: String) {
        launch(Dispatchers.Main) {
            val result = HttpRequestHelper(this@SignupActivity)
                .post("api/users")
                {
                    contentType(ContentType.Application.Json)
                    setBody(Signup(email, verify.toInt(), password))
                }
            when (result?.status) {
                HttpStatusCode.Created -> {
                    Toast.makeText(this@SignupActivity, "성공적으로 회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                HttpStatusCode.Unauthorized -> {
                    Log.d("chae", "오류코드: ${result?.status}, 오류메시지: ${result?.toString()}")
                    binding.errorTextView.text = "인증 코드 혹은 이메일 주소 오류"
                    binding.errorTextView.visibility = View.VISIBLE
                    Toast.makeText(this@SignupActivity, "인증 코드 혹은 이메일 주소를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.d("chae", "오류코드: ${result?.status}, 오류메시지: ${result?.toString()}")
                    binding.errorTextView.text = "회원 가입 서버 오류"
                    binding.errorTextView.visibility = View.VISIBLE
                    Toast.makeText(this@SignupActivity, "회원가입 요청이 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isValidEmail(email: String) =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidVerifyCode(verifyCode: String) =
        verifyCode.isNotBlank() && verifyCode.length == 6 && verifyCode.all { it.isDigit() }

    private fun isValidPassword(password: String) =
        password.isNotBlank()

    private fun isValidConfirmPassword(password: String, confirmPassword: String) =
        confirmPassword.isNotBlank() && password == confirmPassword

    private fun verifyEmail(email: String) {
        launch(Dispatchers.Main) {
            val resSendVerify = HttpRequestHelper(this@SignupActivity)
                .post("api/users/sendVerifyEmail") {
                    contentType(ContentType.Application.Json)
                    setBody(EmailVerify(email))
                }
            if (resSendVerify?.status == HttpStatusCode.Created) {
                Toast.makeText(this@SignupActivity, "인증 코드를 입력하신 이메일로 발송했습니다.",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@SignupActivity, "인증 코드 전송이 실패했습니다.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


}