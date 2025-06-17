package com.vaultin

import android.os.Bundle
import android.util.Log
import android.content.Intent
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.ReactInstanceManager

class MainActivity : ReactActivity() {

    private var cachedIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cachedIntent = intent // 앱이 처음 실행될 때 인텐트 저장

        val reactNativeHost = (application as MainApplication).reactNativeHost
        reactNativeHost.reactInstanceManager.addReactInstanceEventListener(
            object : ReactInstanceManager.ReactInstanceEventListener {
                override fun onReactContextInitialized(reactContext: ReactContext) {
                    Log.d("VaultIn", "✅ ReactContext 초기화 완료됨")
                    cachedIntent?.let {
                        sendIntentToJS(reactContext, it)
                        cachedIntent = null
                    }
                }
            }
        )

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("VaultIn", "🔁 onNewIntent 호출됨")
        if (intent != null) {
            cachedIntent = intent
            val context = reactNativeHost.reactInstanceManager.currentReactContext
            if (context != null) {
                sendIntentToJS(context, intent)
                cachedIntent = null
            }
        }
    }

    private fun sendIntentToJS(reactContext: ReactContext, intent: Intent) {
        Log.d("VaultIn", "📨 sendIntentToJS 호출됨")
        if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = "test" // 추후 실제 값으로 교체
            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onSharedText", sharedText)
            Log.d("VaultIn", "📤 이벤트 JS로 전송됨: $sharedText")
        }
    }

    override fun getMainComponentName(): String = "vaultin"

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, /* fabricEnabled = */ true)
}
