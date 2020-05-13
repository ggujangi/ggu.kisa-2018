package com.hbs.newfaceproject02.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hbs.newfaceproject02.R
import kotlinx.android.synthetic.main.activity_community_detail.*

/**
 * Created by asmwj on 2018-09-30.
 */
class CommunityDetailActivity : AppCompatActivity() {
    private var commuTitle: String = ""
    private var commuCnt: String = ""
    private var commuImage: Int = 0
    private var commuContent:String = ""
    private var commuRecentArray: MutableList<String> = mutableListOf()
    private var commuBesrArray: MutableList<String> = mutableListOf()
    private var commuHotArray: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_detail)
        selectView(intent.getIntExtra("selectInt", 0))

        commuImageDetail.setImageDrawable(resources.getDrawable(commuImage))
        commuTitleText.text = commuTitle
        commuCntText.text = commuCnt
        communityContent.text = commuContent
        backBtn.setOnClickListener {
            finish()
        }

        writeBtn.setOnClickListener {
        }

        newsTitle_0.text = commuRecentArray[0]
        newsTitle_1.text = commuRecentArray[1]
        newsTitle_2.text = commuRecentArray[2]
        newsTitle_3.text = commuRecentArray[3]

        hotTitle_0.text = commuHotArray[0]
        hotTitle_1.text = commuHotArray[1]
        hotTitle_2.text = commuHotArray[2]
        hotTitle_3.text = commuHotArray[3]

        bestTitle_0.text = commuBesrArray[0]
        bestTitle_1.text = commuBesrArray[1]
        bestTitle_2.text = commuBesrArray[2]
        bestTitle_3.text = commuBesrArray[3]
    }


    fun selectView(code: Int) {
        when (code) {
            0 -> {
                commuImage = R.drawable.community_back1
                commuTitle = "우리모두따릉이"
                commuCnt = "글 1,023개"
                commuContent = "글쓰기 수칙\n1. 따릉이 타는 사람들만!\n2. 장난 글/거래 글 사절!\n3. 따릉이 꿀팁 공유하는 곳~"
                commuHotArray.add("따릉이 추천 코스 BEST 10")
                commuHotArray.add("벌써 5년이 되어가네요..")
                commuHotArray.add("출근 같이 하실 분 (상암)")
                commuHotArray.add("헬멧 잃어버림 ㅠㅠ")

                commuBesrArray.add("따릉이 추천 코스 BEST 10")
                commuBesrArray.add("따릉이 저렴하게 대여하는 팁")
                commuBesrArray.add("빠릉이 간편결제 넘나 편리")
                commuBesrArray.add("벌써 5년이 되어가네요..")

                commuRecentArray.add("한강 빠릉이 데이트 ><")
                commuRecentArray.add("안녕 애들아")
                commuRecentArray.add("출근 같이 하실 분 (상암)")
                commuRecentArray.add("이직하는 곳이 넘 멀다.")
            }
            1 -> {
                commuImage = R.drawable.community_back2
                commuTitle = "영화수다방"
                commuCnt = "글 858개"
                commuContent = "영화 좋아하는 사람들"
                commuContent = "요즘 영화, 예전 영화, 나중 영화 글 공유해요~\n** 장난/거래 글 금지!\n** 욕설/타인 비방 금지!"

                commuHotArray.add("이번주 개봉예정작품 Top 5")
                commuHotArray.add("영화 [베놈] IMAX 시사회 리얼 후기")
                commuHotArray.add("@@영화 포스터 팔아요@@")
                commuHotArray.add("내 기준 로맨스 인생 영화")

                commuBesrArray.add("영화 [베놈] IMAX 시사회 리얼 후기")
                commuBesrArray.add("베놈 톰 하디 작품 모음")
                commuBesrArray.add("이번주 개봉예정작품 Top 5")
                commuBesrArray.add ("내 기준 로맨스 인생 영화")

                commuRecentArray.add("영화 추천좀 부탁해요..")
                commuRecentArray.add("왕십리CGV 좋은 자리 발견함!!")
                commuRecentArray.add("@@영화 포스터 팔아요@@")
                commuRecentArray.add("명당 보고 왔어요.(스포 O)")
            }
            2 -> {
                commuImage = R.drawable.community_back3
                commuTitle = "히오스시공좋아"
                commuContent = "히오스 갓겜인 이유\n1. ...\n2. ...\n3. 아무나 채워줘"
                commuCnt = "글 793개"

                commuHotArray.add("화이트메인 특성트리(힐특)")
                commuHotArray.add("히오스가 갓겜인 이유 내가 채워줌")
                commuHotArray.add("피시방에서 히오스 친구 사귐")
                commuHotArray.add("오늘 날씨 덥다..")

                commuBesrArray.add("이번 빛나래 패치 어떻냐")
                commuBesrArray.add("히오스가 갓겜인 이유 내가 채워줌")
                commuBesrArray.add("요즘 데스티니 가디언즈 재밌던데..")
                commuBesrArray.add ("하나무라 맵 괜찮네")

                commuRecentArray.add("히오스 빠대 팟 구함!")
                commuRecentArray.add("가즈로 스킨 좀 만들어줘라")
                commuRecentArray.add("랭겜 돌리실 분?")
                commuRecentArray.add("큐 너무 안잡히네;;")
            }
        }
    }
}