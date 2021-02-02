package com.baseandroid.baselibrary.utils

import java.util.regex.Pattern

/**
 * Kiểm tra tên file
 * @param nameFile chuỗi cần kiểm tra
 * @return  true: tên hợp lệ/ false: tên ko hợp lệ
 */
fun isFileNameValid(nameFile: String, extension: String): Boolean {
    val regex = "(((^[^.\\\\/:*?\"<>|])([^\\\\/:*?\"<>|]*))(\\.(?i)($extension))$)"
    val pattern = Pattern.compile(regex)
    val optimalString = optimalString(nameFile)
    val matcher = pattern.matcher(optimalString)
    return matcher.matches()
}


/**
 * Tối ưu chuỗi: loại bỏ các khoảng trắng do tab hoặc new line..
 * @param name  tên cần tối ưu
 * @return  text đã được tối ưu
 */
fun optimalString(name: String): String {
    val strings = name.split(" ")
    var optimalString = ""
    for (str in strings) {
        if (str.trim().isNotEmpty()) {
            optimalString += "$str "
        }
    }
    return optimalString.trim()
}