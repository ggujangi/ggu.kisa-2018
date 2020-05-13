package com.hbs.newfaceproject02.Activity

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import com.hbs.newfaceproject02.Control.CvsControl
import com.hbs.newfaceproject02.Model.BikeStopInfo
import com.hbs.newfaceproject02.R
import com.hbs.newfaceproject02.Utils.MakeUserDb
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.END_LAT
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.END_LONG
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.END_NAME
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.START_LAT
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.START_LONG
import com.hbs.newfaceproject02.Utils.StaticVars.Companion.START_NAME
import com.skt.Tmap.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    // FirebaseDatabase 객체 생성
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    private var noticeBool: Boolean = true

    // 뒤로가기를 위한 변수
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0
    private lateinit var prefEditor: SharedPreferences.Editor

    // "cycleStop" 경로를 향하는 DatabaseReference 객체 생성
    private val cycleStopRef: DatabaseReference = firebaseDatabase.getReference().child("cyclestop")
    // TMapView 객체 생성
    private lateinit var tmapview: TMapView
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 위치권한 permission 검사
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1); //위치권한 탐색 허용 관련 내용
            }
            return
        } else {

        }

        questionBtn.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
        val noticePref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        prefEditor = noticePref.edit()


        // TMapView 객체 초기화
        tmapview = TMapView(this)
        mContext = this


        // TMap 띄우는 함수
        initTmap(tmapview)

        noticeBool = noticePref.getBoolean("noticeBool", true)

        if (noticeBool) {
            showNoticeDialog()
        }



        // GPS 함수
        getGps()
        setGps()

        // 네비게이션 메뉴 이동하기
        bottomNav.menu.getItem(0).setChecked(false)
        bottomNav.menu.getItem(1).setChecked(false)
        bottomNav.menu.getItem(2).setChecked(false)
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {/*
                R.id.navFavor -> {
                    val intent = Intent(this, FavorActivity::class.java)
                    startActivity(intent)
                }*/
                R.id.navMypage -> {
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                }/*
                R.id.navQuestion -> {
                    val intent = Intent(this, QuestionActivity::class.java)
                    // val intent = Intent(this, TestWebViewActivity::class.java)
                    startActivity(intent)
                }*/
                R.id.navBuy -> {
                    val intent = Intent(this, BuyTicketActivity::class.java)
                    // val intent = Intent(this, TestWebViewActivity::class.java)
                    startActivity(intent)
                }

                R.id.navCommunity -> {
                    val intent = Intent(this, CommunityActivity::class.java)
                    // val intent = Intent(this, TestWebViewActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        })

        val list = CvsControl().readCvsFile(applicationContext)

        for (stopInfo in list) {
            setStopMap(stopInfo)
        }


        // marker의 오른쪽 이미지를 클릭했을 경우
        tmapview.setOnCalloutRightButtonClickListener(object : TMapView.OnCalloutRightButtonClickCallback {
            override fun onCalloutRightButton(markerItem: TMapMarkerItem?) {

                for (item in list) {
                    if (item.stopName == markerItem!!.id.toString()) {
                        // 대여 Dialog 띄우기
                        showDialog(item.stopName, "5", item.totalCnt, item.latitude, item.longitude)
                    }
                }
            }
        })
        0

        // LinearLayout에 생성한 tmapview를 최종적으로 추가시킨다.
        tmapAPILinear.addView(tmapview)
    }


    fun setStopMap(item: BikeStopInfo) {
        val tmapMarkerItem = TMapMarkerItem() // TMapMarkerItem 객체 생성

        Log.d("itemInfoName", item.stopName)
        Log.d("itemInfoLatitude", item.latitude)
        Log.d("itemInfoLongitude", item.longitude)
        // 가져온 데이터를 통해 TMapPoint 생성
        val tmapPoint = TMapPoint(item.latitude.toDouble(), item.longitude.toDouble())

        // marker 아이콘
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)

        // tmapMarkerItem 꾸미기
        tmapMarkerItem.icon = bitmap // 아이콘
        tmapMarkerItem.setPosition(0.5f, 1.0f)
        tmapMarkerItem.tMapPoint = tmapPoint // 마커의 포인트
        tmapMarkerItem.name = item.stopName as String // 마커 이름


        // 마커의 버블 세팅
        tmapMarkerItem.calloutTitle = item.stopName as String // 버블 이름
        tmapMarkerItem.canShowCallout = true // 버블 보일 것인지 아닌지
        tmapMarkerItem.calloutRightButtonImage = BitmapFactory.decodeResource(resources, R.drawable.right_arrow) // 오른쪽 이미지 세팅 (right_arrow)

        // tmapview 에 markerItem 추가
        tmapview.addMarkerItem(item.stopName, tmapMarkerItem)
    }


    // tmap을 사용하기 위한 초기화
    fun initTmap(tmapview: TMapView) {
        tmapview.setSKTMapApiKey(resources.getString(R.string.tmap_key)) // API key 등록
        tmapview.setCompassMode(true)
        tmapview.setIconVisibility(true)
        tmapview.zoomLevel = 15
        tmapview.mapType = TMapView.MAPTYPE_STANDARD
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN)
        tmapview.setTrackingMode(true)
        tmapview.setSightVisible(true)
    }

    fun getGps() {
        val tmapGps = TMapGpsManager(this)
        tmapGps.minTime = 1000
        tmapGps.minDistance = 5f
        tmapGps.provider = TMapGpsManager.NETWORK_PROVIDER
        tmapGps.OpenGps()
    }

    fun setGps() {
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1f, // 통지사이의 최소 변경거리 (m)
                mLocationListener)
    }

    // 현재 위치의 위경도를 알기 위한 LocationListener
    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {

            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                val latitude = location!!.getLatitude()
                val longitude = location!!.getLongitude()

                // 현재 위치에 초점두기 위함
                tmapview.setLocationPoint(longitude, latitude)
                tmapview.setCenterPoint(longitude, latitude)
            }
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }


    fun showNoticeDialog() {
        val noticeBuilder = AlertDialog.Builder(mContext)
        val noticeView = layoutInflater.inflate(R.layout.dialog_notice, null)

        val noticeCheckBox = noticeView.findViewById<CheckBox>(R.id.noticeCheckBox)
        val okText = noticeView.findViewById<TextView>(R.id.okText)

        noticeCheckBox.setOnClickListener {
            if (noticeCheckBox.isChecked) {
                // 체크가 되어 있다면
                prefEditor.putBoolean("noticeBool", false)
            } else {
                //체크가 아니라면
                prefEditor.putBoolean("noticeBool", true)
            }
        }

        noticeBuilder.setView(noticeView)
        val dialog = noticeBuilder.create()

        dialog.show()
        okText.setOnClickListener {
            prefEditor.commit()
            dialog.cancel()
        }

    }

    // 대여하기, 출발지, 도착지 Dialog
    fun showDialog(name: String, remain: String, total: String, latitude: String, longitude: String) {
        val stopBuilder = AlertDialog.Builder(mContext)

        // layout View 생성
        val view = layoutInflater.inflate(R.layout.dialog_rent, null)

        // TextView 등 View 들 생성
        val nameText = view.findViewById<TextView>(R.id.rentTitle)
        val remainRentalCnt = view.findViewById<TextView>(R.id.remainRentalCnt)
        val totalRentalCnt = view.findViewById<TextView>(R.id.totalRentalCnt)
        val startPoint = view.findViewById<Button>(R.id.startPoint)
        val endPoint = view.findViewById<Button>(R.id.endPoint)
        val favorStar = view.findViewById<ImageView>(R.id.favorStar)
        val reservationBtn = view.findViewById<Button>(R.id.reservationBtn)

        var clickBool = getFavorStatus(name, favorStar)


        // TODO 다시 구현하기
        // clickBool이 true면 이미 즐찾한 상태
        favorStar.setOnClickListener {
            if (clickBool) {
                favorStar.setImageDrawable(resources.getDrawable(R.drawable.favor_star_n))
                firebaseDatabase.getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("favorInfo").child(name).removeValue()
                clickBool = getFavorStatus(name, favorStar)
            } else {
                favorStar.setImageDrawable(resources.getDrawable(R.drawable.favor_star_y))
                firebaseDatabase.getReference().child("userInfo").child(MakeUserDb().makeUserId()).child("favorInfo").child(name).setValue(total)
                clickBool = getFavorStatus(name, favorStar)
            }
        }

        // textView에 text Setting
        nameText.text = name
        remainRentalCnt.text = remain
        totalRentalCnt.text = total


        // alertDialog builder 객체에 view 세팅
        stopBuilder.setView(view)

        // builder를 통해 dialog 생성
        val dialog = stopBuilder.create()


        // 찜하기 버튼을 클릭했을 경우
        reservationBtn.setOnClickListener {
            val rentBuilder = AlertDialog.Builder(this)
            rentBuilder.setMessage("${name} 대여소 자전거를 찜하시겠습니까?")
                    .setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->
                        notificationSomethings(name, "${makeTenMinute()}")

                        // changeBorrowCnt(key, true, borrowed, remain)
                        dialog.dismiss()
                    })
                    .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialog.dismiss()
                    })

            rentBuilder.create().show()
        }

        // 숨겨진 시작 버튼을 눌렀을 경우
        routeStartBtn.setOnClickListener {
            hideRouteLinear.visibility = View.GONE
        }


        // 출발지 버튼을 클릭했을 경우
        startPoint.setOnClickListener {
            // static 변수에 출발지 위,경도 저장
            START_LAT = latitude
            START_LONG = longitude
            START_NAME = name
            hideRouteLinear.visibility = View.GONE
            // dialog 닫음
            dialog.dismiss()
        }


        // 도착지 버튼을 클릭했을 경우
        endPoint.setOnClickListener {
            //static 변수에 도착지 위,경도 저장
            if (START_LAT != "") {
                END_LAT = latitude
                END_LONG = longitude
                END_NAME = name

                hideRouteLinear.visibility = View.VISIBLE
                startLocation.text = START_NAME
                endLocation.text = END_NAME
                // 저장된 static 위,경도 정보를 통해 두 개의 TMapPoint 생성
                val tMapPointStart = TMapPoint(START_LAT.toDouble(), START_LONG.toDouble())
                val tMapPointEnd = TMapPoint(END_LAT.toDouble(), END_LONG.toDouble())
                try {

                    // TMapData 객체 생성
                    val tmapData = TMapData()

                    // 자동차 길찾기 사용, 생성한 두 개의 TMapPoint 넣기
                    tmapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapPointStart, tMapPointEnd, TMapData.FindPathDataListenerCallback { tMapPolyLine ->

                        // 생성된 PolyLine 을 TmapView에 추가
                        tmapview.addTMapPath(tMapPolyLine)
                        tMapPolyLine.lineColor = Color.RED // 빨간색
                        tMapPolyLine.lineWidth = 10f
                    })

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                dialog.dismiss()
            } else {
                dialog.dismiss()
                Toast.makeText(this, "출발지를 설정해주세요.", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }


        // dialog 보여주기
        dialog.show()
    }

    // notification 띄우는 함수
    fun notificationSomethings(name: String, ticker: String) {
        val res = resources

        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this)

        builder.setContentTitle(name)
                .setContentText(ticker)
                .setSmallIcon(R.drawable.cycling)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cycling))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
        }

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1234, builder.build())
    }

    // TODO 다시 구현하기
    fun getFavorStatus(name: String, favorStar: ImageView): Boolean {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val favorRef = firebaseDatabase.getReference().child("userId").child(MakeUserDb().makeUserId()).child("favorInfo")
        var resultBool = false
        favorRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                for (childSnap in p0!!.children) {
                    resultBool = (childSnap.key.toString() == name)
                    Log.d("resultBool", resultBool.toString())
                    if (resultBool) {
                        favorStar.setImageDrawable(resources.getDrawable(R.drawable.favor_star_y))
                    } else {
                        favorStar.setImageDrawable(resources.getDrawable(R.drawable.favor_star_n))
                    }
                }
            }
        })
        return resultBool
    }

    // 10분 후 만드는 함수
    fun makeTenMinute(): String {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MINUTE, 10)
        val result = SimpleDateFormat("HH시 mm분까지 카드를 태깅해주세요.").format(cal.time)

        return result
    }


    // 두번 눌러 뒤로 가기
    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            moveTaskToBack(true)
            finish()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun getCost(position: Int): String {
        when (position) {
            0 -> return "1000"
            1 -> return "2000"
            2 -> return "3000"
            3 -> return "5000"
            4 -> return "15000"
            5 -> return "30000"
            else -> return "0원"
        }
    }
}
