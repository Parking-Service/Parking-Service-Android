package com.app.service.parking.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
data class ParkingLot(
    @Expose
    @SerializedName("response")
    val response: Response
)

data class Response(
    @Expose
    @SerializedName("header")
    val header: Header,
    @Expose
    @SerializedName("body")
    val body: Body
)

data class Header(
    @Expose
    @SerializedName("resultCode")
    val resultcode: String,
    @Expose
    @SerializedName("resultMsg")
    val resultmsg: String
)

data class Body(
    @Expose
    @SerializedName("items")
    val items: List<Lot>,
    @Expose
    @SerializedName("totalCount")
    val totalcount: String
)
*/

data class Lot(
    @Expose
    @SerializedName("prkplce_no") // 주차장관리번호 (eg. 156-2-000043)
    val parkCode: String,
    @Expose
    @SerializedName("prkplce_nm") // 주차장명
    val parkName: String,
    @Expose
    @SerializedName("prkplce_se") // 주차장 구분 (공영 / 민영)
    val parkSep: String,
    @Expose
    @SerializedName("prkplce_type") // 주차장 유형 (노외 / 노상 / 부설)
    val parkType: String,
    @Expose
    @SerializedName("rdnmadr")
    val newAddr: String?,
    @Expose
    @SerializedName("lnmadr")
    val oldAddr: String?,
    @Expose
    @SerializedName("prkcmprt") // 주차 구획수
    val parkCmprt: String,
    @Expose
    @SerializedName("feeding_se") // 급지 구분
    val feedingSep: String,
    @Expose
    @SerializedName("enforce_se") // 부제시행구분
    val enforceSep: String,
    @Expose
    @SerializedName("oper_day")
    val operDay: String,
    @Expose
    @SerializedName("weekday_oper_open_hhmm")
    val weekdyOpenTime: String,
    @Expose
    @SerializedName("weekday_oper_close_hhmm")
    val weekdayCloseTime: String,
    @Expose
    @SerializedName("sat_oper_oper_open_hhmm")
    val saturdayOpenTime: String,
    @Expose
    @SerializedName("sat_oper_close_hhmm")
    val saturdayCloseTime: String,
    @Expose
    @SerializedName("holiday_oper_open_hhmm")
    val holidayOpenTime: String,
    @Expose
    @SerializedName("holiday_close_open_hhmm")
    val holidayCloseTime: String,
    @Expose
    @SerializedName("parkingchrge_info") // 요금 정보 (무료/유료)
    val feeType: String,
    @Expose
    @SerializedName("basic_time") // 주차기본시간
    val basicParkTime: String,
    @Expose
    @SerializedName("basic_charge") // 주차기본요금
    val basicFee: String,
    @Expose
    @SerializedName("add_unit_time") // 추가단위시간
    val addUnitTime: String?,
    @Expose
    @SerializedName("add_unit_charge") // 추가단위요금
    val addUnitFee: String?,
    @Expose
    @SerializedName("day_cmmtkt_adj_time") // 1일주차권요금적용시간
    val parkTimePerDay: String?,
    @Expose
    @SerializedName("day_cmmtkt") // 1일주차권요금
    val feePerDay: String?,
    @Expose
    @SerializedName("month_cmmtkt") // 월정기권요금
    val feePerMonth: String,
    @Expose
    @SerializedName("metpay") // 결제방법 (현금 / 현금, 카드 / 카드)
    val payType: String,
    @Expose
    @SerializedName("spcmnt") // 특기사항(특이사항)
    val uniqueness: String,
    @Expose
    @SerializedName("institution_nm") // 기관명
    val institutionName: String,
    @Expose
    @SerializedName("phone_number") // 전화번호
    val phoneNumber: String,
    @Expose
    @SerializedName("latitude") // 위도
    val latitude: String,
    @Expose
    @SerializedName("longitude") // 경도
    val longitude: String,
    @Expose
    @SerializedName("reference_date")
    val referencedate: String,
    @Expose
    @SerializedName("instt_code") // 기관 코드
    val insttcode: String,
    @Expose
    @SerializedName("instt_nm") // 기관명
    val insttNm: String
)