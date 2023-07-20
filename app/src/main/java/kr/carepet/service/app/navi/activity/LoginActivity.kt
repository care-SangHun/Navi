package kr.carepet.service.app.navi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kr.carepet.service.app.navi.G
import kr.carepet.service.app.navi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.loginTvSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginBtnKakao.setOnClickListener { kakaoClick() }
        var keyHash:String = Utility.getKeyHash(this);
        Log.i("keyHash", keyHash)
    }

    private fun kakaoClick() {
        // Kakao Login API를 이용하여 사용자 정보 취득

        // 로그인 시도한 결과를 받았을때 발동하는 콜백함수를 별도로 만들기
        val callback:(OAuthToken?, Throwable?)->Unit = { token, error ->
            if( error != null ) {
                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()

                //사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if(user!=null){
                        var id:String = user.id.toString()
                        var email:String = user.kakaoAccount?.email ?: ""  //혹시 null이면 이메일의 기본값 ""

                        Toast.makeText(this, "사용자 이메일 정보 : $email", Toast.LENGTH_SHORT).show()
                        G.userId =id
                        G.userEmail =email

                        //로그인이 성공했으니.. Main 화면으로 전환
                        startActivity( Intent(this, MainActivity::class.java) )
                        finish()
                    }
                }
            }
        }

        // 카카오톡 로그인을 권장하지만 설치가 되어 있지 않다면 카카오계정으로 로그인 시도.
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback= callback )
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback )
        }
    }
}