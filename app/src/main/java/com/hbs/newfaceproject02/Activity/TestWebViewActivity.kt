package com.hbs.newfaceproject02.Activity

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hbs.newfaceproject02.Model.AuthAccountInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.AccountAsyncHttp
import com.hbs.newfaceproject02.Utils.MakeUserDb
import kotlinx.android.synthetic.main.activity_webview.*
import java.net.URL



/**
 * Created by asmwj on 2018-08-16.
 */
class TestWebViewActivity : AppCompatActivity() {
    // 이미 인증을 했는지 확인
    val userInfoRef = FirebaseDatabase.getInstance().getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("accountInfo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // 새 카드 등록인지 추가 등록인지 판별하는 Boolean
        val firstBool = intent.getBooleanExtra("firstBool", true)

        if(firstBool){
            userInfoRef.addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {}

                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.hasChildren()){
                        val goToMain = Intent(applicationContext, MainActivity::class.java)
                        startActivity(goToMain)
                        finish()
                    }
                    else{
                        loadUrlWebView()
                    }
                }
            })
        }
        else{
            loadUrlWebView()
        }
    }


    private fun loadUrlWebView(){
        // 테스트 금융 API를 WebView에 로드
        val url: URL = URL("https://testapi.open-platform.or.kr/oauth/2.0/authorize2?response_type=code&client_id=l7xx8249e900e6514b56aabf984ce3aca9b1&redirect_uri=http%3A%2F%2Flocalhost%3A8880%2FRETURN&scope=login%20transfer%20inquiry&client_info=asmwj3gmailcom&auth_type=0&bg_color=%23ffffff&txt_color=%23000000&btn1_color=%23000000&btn2_color=%23000000&apiKey=l7xx8249e900e6514b56aabf984ce3aca9b1")
        Log.d("myUrl", url.toString())
        testWebVIew!!.visibility = View.VISIBLE
        testWebVIew!!.clearHistory()
        testWebVIew.clearCache(true)
        testWebVIew.settings.javaScriptEnabled = true
        testWebVIew.settings.javaScriptCanOpenWindowsAutomatically = true
        testWebVIew.settings.loadWithOverviewMode = true
        testWebVIew.settings.domStorageEnabled = true
        testWebVIew.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        testWebVIew.settings.defaultTextEncodingName = "UTF-8"
        testWebVIew.settings.domStorageEnabled = true
        testWebVIew.settings.saveFormData = true
        testWebVIew.settings.allowContentAccess = true
        testWebVIew.settings.databaseEnabled = true
        testWebVIew.settings.allowFileAccess = true
        testWebVIew.settings.allowFileAccessFromFileURLs = true
        testWebVIew.settings.allowUniversalAccessFromFileURLs = true
        testWebVIew.settings.setSupportZoom(true)

        testWebVIew.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view!!.loadUrl(url.toString())
                Log.d("## onPageLoading", "url: $url")
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("## onPageFinished", "url: $url")
                if(url!!.contains("http://localhost:8880/RETURN?code=")){

                    // 응답 메시지 값 가져오기
                    val uri = Uri.parse(url)
                    val codeString = uri.getQueryParameter("code")
                    val clientInfoString = uri.getQueryParameter("client_info")

                    // 데이터베이스 경로에 저장
                    userInfoRef.child(codeString).setValue(AuthAccountInfo(codeString, clientInfoString))
                    val url = URL("https://testapi.open-platform.or.kr/oauth/2.0/token?code=${codeString}&client_id=l7xx8249e900e6514b56aabf984ce3aca9b1&client_secret=f202c455791544b9899b11d0550df297&redirect_uri=http%3A%2F%2Flocalhost%3A8880%2FRETURN&grant_type=authorization_code&apiKey=l7xx9093aa52f0974ec1b0e4133472c61d7f")

                    val accountAsync = AccountAsyncHttp(applicationContext, codeString, "POST", 100, "")
                    accountAsync.execute(url.toString())


/*
                    // MainActivity로 이동
                    val gotoMain = Intent(applicationContext, MainActivity::class.java)
                    startActivity(gotoMain)
                    finish()*/
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler!!.proceed()
            }
        }

        testWebVIew.isClickable = true
        testWebVIew.webChromeClient = WebChromeClient()
        testWebVIew.loadUrl(url.toString())
    }
}