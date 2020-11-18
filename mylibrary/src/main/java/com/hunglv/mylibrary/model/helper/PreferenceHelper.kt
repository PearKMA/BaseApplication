package com.hunglv.mylibrary.model.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * pref: SharedPreferences
 * pref = App.prefHelper.pref(requireContext(), COMPASS)
 * pref.get..
 * pref.edit {
 *      this.put....
 * }
 */
class PreferenceHelper {
    fun pref(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }
}