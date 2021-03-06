package com.app.service.parking.model.dto

import com.app.service.parking.R
import com.app.service.parking.global.App
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Lot(
    @Expose
    @SerializedName("prkplceNo") // 주차장관리번호 (eg. 156-2-000043)
    val parkCode: String,
    @Expose
    @SerializedName("prkplceNm") // 주차장명
    val parkName: String,
    @Expose
    @SerializedName("prkplceSe") // 주차장 구분 (공영 / 민영)
    val parkSep: String,
    @Expose
    @SerializedName("prkplceType") // 주차장 유형 (노외 / 노상 / 부설)
    val parkType: String,
    @Expose
    @SerializedName("rdnmadr")
    val newAddr: String? = App.context?.getString(R.string.no_info),
    @Expose
    @SerializedName("lnmadr")
    val oldAddr: String? = App.context?.getString(R.string.no_info),
    @Expose
    @SerializedName("prkcmprt") // 주차 구획수
    val parkCmprt: String,
    @Expose
    @SerializedName("feedingSe") // 급지 구분
    val feedingSep: String,
    @Expose
    @SerializedName("enforceSe") // 부제시행구분
    val enforceSep: String,
    @Expose
    @SerializedName("operDay")
    val operDay: String,
    @Expose
    @SerializedName("weekdayOperOpenHhmm")
    val weekdayOpenTime: String,
    @Expose
    @SerializedName("weekdayOperCloseHhmm")
    val weekdayCloseTime: String,
    @Expose
    @SerializedName("satOperOperOpenHhmm")
    val saturdayOpenTime: String,
    @Expose
    @SerializedName("satOperCloseHhmm")
    val saturdayCloseTime: String,
    @Expose
    @SerializedName("holidayOperOpenHhmm")
    val holidayOpenTime: String,
    @Expose
    @SerializedName("holidayCloseOpenHhmm")
    val holidayCloseTime: String,
    @Expose
    @SerializedName("parkingchrgeInfo") // 요금 정보 (무료/유료)
    val feeType: String,
    @Expose
    @SerializedName("basicTime") // 주차기본시간
    val basicParkTime: String,
    @Expose
    @SerializedName("basicCharge") // 주차기본요금
    val basicFee: String = "0",
    @Expose
    @SerializedName("addUnitTime") // 추가단위시간
    val addUnitTime: String?,
    @Expose
    @SerializedName("addUnitCharge") // 추가단위요금
    val addUnitFee: String?,
    @Expose
    @SerializedName("dayCmmtktAdjTime") // 1일주차권요금적용시간
    val parkTimePerDay: String?,
    @Expose
    @SerializedName("dayCmmtkt") // 1일주차권요금
    val feePerDay: String?,
    @Expose
    @SerializedName("monthCmmtkt") // 월정기권요금
    val feePerMonth: String,
    @Expose
    @SerializedName("metpay") // 결제방법 (현금 / 현금, 카드 / 카드)
    val payType: String,
    @Expose
    @SerializedName("spcmnt") // 특기사항(특이사항)
    val uniqueness: String,
    @Expose
    @SerializedName("institutionNm") // 기관명
    val institutionName: String,
    @Expose
    @SerializedName("phoneNumber") // 전화번호
    val phoneNumber: String,
    @Expose
    @SerializedName("latitude") // 위도
    val latitude: String,
    @Expose
    @SerializedName("longitude") // 경도
    val longitude: String,
    @Expose
    @SerializedName("referenceDate")
    val referencedate: String,
    @Expose
    @SerializedName("insttCode") // 기관 코드
    val insttcode: String,
    @Expose
    @SerializedName("insttNm") // 기관명
    val insttNm: String
) : Serializable {

    // *영업시간
    val weekdayOperTime: String // 평일
        get() = "$weekdayOpenTime ~ $weekdayCloseTime"
    val saturOperTime: String // 토요일
        get() = "$saturdayOpenTime ~ $saturdayCloseTime"
    val weekendOperTime: String // 토요일
        get() = "$holidayOpenTime ~ $holidayCloseTime"
    val basicFeeWon: String //주차기본요금
        get() {
            return if (basicFee?.isNullOrEmpty()) {
                "${basicFee}원"
            } else {
                "0원"
            }
        }
    val additionalFee: String // 추가비용
        get() {
            return if (addUnitTime.isNullOrEmpty()) {
                "- 분당 - 원"
            } else {
                "${addUnitTime}분당 ${addUnitFee}원"
            }
        }
    val oneDayParkTime: String // 1일 주차권 요금 시간
        get() {
            return if (parkTimePerDay.isNullOrBlank()) {
                "- 시간"
            } else {
                "${parkTimePerDay}시간"
            }
        }
    val oneDayParkFee: String // 1일 주차권 요금 비용
        get() {
            return if (feePerDay.isNullOrBlank()) {
                "0원"
            } else {
                "${feePerDay}원"
            }
        }
    val oneMonthParkFee: String // 월 정기권 요금
        get() {
            return if (feePerMonth.isNullOrBlank()) {
                "0원"
            } else {
                "${feePerMonth}원"
            }
        }
}