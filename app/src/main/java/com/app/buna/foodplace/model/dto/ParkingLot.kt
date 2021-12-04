package com.app.buna.foodplace.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

data class Body(
    @Expose
    @SerializedName("items")
    val items: List<Items>,
    @Expose
    @SerializedName("totalCount")
    val totalcount: String,
    @Expose
    @SerializedName("numOfRows")
    val numofrows: String,
    @Expose
    @SerializedName("pageNo")
    val pageno: String
)

data class Items(
    @Expose
    @SerializedName("prkplceNo")
    val prkplceno: String,
    @Expose
    @SerializedName("prkplceNm")
    val prkplcenm: String,
    @Expose
    @SerializedName("prkplceSe")
    val prkplcese: String,
    @Expose
    @SerializedName("prkplceType")
    val prkplcetype: String,
    @Expose
    @SerializedName("rdnmadr")
    val rdnmadr: String,
    @Expose
    @SerializedName("lnmadr")
    val lnmadr: String,
    @Expose
    @SerializedName("prkcmprt")
    val prkcmprt: String,
    @Expose
    @SerializedName("feedingSe")
    val feedingse: String,
    @Expose
    @SerializedName("enforceSe")
    val enforcese: String,
    @Expose
    @SerializedName("operDay")
    val operday: String,
    @Expose
    @SerializedName("weekdayOperOpenHhmm")
    val weekdayoperopenhhmm: String,
    @Expose
    @SerializedName("weekdayOperColseHhmm")
    val weekdayopercolsehhmm: String,
    @Expose
    @SerializedName("satOperOperOpenHhmm")
    val satoperoperopenhhmm: String,
    @Expose
    @SerializedName("satOperCloseHhmm")
    val satoperclosehhmm: String,
    @Expose
    @SerializedName("holidayOperOpenHhmm")
    val holidayoperopenhhmm: String,
    @Expose
    @SerializedName("holidayCloseOpenHhmm")
    val holidaycloseopenhhmm: String,
    @Expose
    @SerializedName("parkingchrgeInfo")
    val parkingchrgeinfo: String,
    @Expose
    @SerializedName("basicTime")
    val basictime: String,
    @Expose
    @SerializedName("basicCharge")
    val basiccharge: String,
    @Expose
    @SerializedName("addUnitTime")
    val addunittime: String,
    @Expose
    @SerializedName("addUnitCharge")
    val addunitcharge: String,
    @Expose
    @SerializedName("dayCmmtktAdjTime")
    val daycmmtktadjtime: String,
    @Expose
    @SerializedName("dayCmmtkt")
    val daycmmtkt: String,
    @Expose
    @SerializedName("monthCmmtkt")
    val monthcmmtkt: String,
    @Expose
    @SerializedName("metpay")
    val metpay: String,
    @Expose
    @SerializedName("spcmnt")
    val spcmnt: String,
    @Expose
    @SerializedName("institutionNm")
    val institutionnm: String,
    @Expose
    @SerializedName("phoneNumber")
    val phonenumber: String,
    @Expose
    @SerializedName("latitude")
    val latitude: String,
    @Expose
    @SerializedName("longitude")
    val longitude: String,
    @Expose
    @SerializedName("referenceDate")
    val referencedate: String,
    @Expose
    @SerializedName("insttCode")
    val insttcode: String
)

data class Header(
    @Expose
    @SerializedName("resultCode")
    val resultcode: String,
    @Expose
    @SerializedName("resultMsg")
    val resultmsg: String,
    @Expose
    @SerializedName("type")
    val type: String
)