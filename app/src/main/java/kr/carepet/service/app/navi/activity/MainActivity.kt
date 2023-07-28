package kr.carepet.service.app.navi.activity


import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivityMainBinding
import kr.carepet.service.app.navi.singleton.G
import kr.carepet.service.app.navi.singleton.MySharedPreference


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val TAG = "MainActivity"
    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this, "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainTvId.text = G.userId
        binding.mainTvEmail.text = G.userEmail


        weatherCheck()

        binding.subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to weather topic")
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener { task ->
                    var msg = getString(kr.carepet.service.app.navi.R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(kr.carepet.service.app.navi.R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg!!)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }
            // [END subscribe_topics]
        }

        binding.logTokenButton.setOnClickListener {
            // Get token
            // [START log_reg_token]
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener<String?> { task ->
                    if (!task.isSuccessful) {
                        Log.w(
                            TAG,
                            "Fetching FCM registration token failed",
                            task.exception
                        )
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and toast
                    val msg = getString(kr.carepet.service.app.navi.R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                })
        }

        askNotificationPermission()
    }

    private fun weatherCheck() {
        // 당일 처음 접속하면 false를 반환, 이후에는 true를 반환
        var isWeatherCheck = MySharedPreference.getTodayData()

        if(!isWeatherCheck){
            //isWeatherCheck가 false면 실행
            //즉, 하루에 한번만 실행하는 메소드

            //isWeather값을 true로 바꿔줌
            MySharedPreference.setTodayData()

            // 이후 날씨정보를 가져오는 코드 구현
        }

    }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}