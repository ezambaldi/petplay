package com.example.myapplicationtest.utils;

import android.content.Context
import android.content.SharedPreferences
import java.util.HashSet

object AppPreferences {

        var preferences: SharedPreferences? = null
            private set

        private val editor: SharedPreferences.Editor
            get() = preferences!!.edit()

        fun init(name: String, context: Context) {
            preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }


        fun put(name: String, value: String) {
            val editor = editor
            editor.putString(name, value)
            editor.apply()
            editor.commit()
        }

        fun put(name: String, value: Float) {
            val editor = editor
            editor.putFloat(name, value)
            editor.apply()
            editor.commit()
        }

        fun put(name: String, value: kotlin.collections.Set<String>) {
            val editor = editor
            editor.putStringSet(name, value)
            editor.apply()
            editor.commit()
        }

        fun put(name: String, value: Int) {
            val editor = editor
            editor.putInt(name, value)
            editor.apply()
            editor.commit()
        }

        fun put(name: String, value: Boolean) {
            val editor = editor
            editor.putBoolean(name, value)
            editor.apply()
            editor.commit()
        }

        fun put(name: String, value: Long) {
            val editor = editor
            editor.putLong(name, value)
            editor.apply()
            editor.commit()
        }

        fun getBoolean(name: String): Boolean {
            return preferences!!.getBoolean(name, false)
        }

        fun getString(name: String): String? {
            return preferences!!.getString(name, "")
        }

        fun getFloat(name: String): Float {
            return preferences!!.getFloat(name, -1f)
        }

        fun getLong(name: String): Long {
            return preferences!!.getLong(name, 0)
        }

        fun getInt(name: String): Int {
            return preferences!!.getInt(name, -1)
        }

        fun getStrings(name: String): kotlin.collections.Set<String>? {
            return preferences!!.getStringSet(name, HashSet())
        }

        operator fun contains(key: String): Boolean {
            return preferences!!.contains(key)
        }

        fun remove(name: String) {
            val editor = editor
            editor.remove(name)
            editor.apply()
            editor.commit()
        }

        fun clear() {
            val editor = editor
            editor.clear()
            editor.apply()
            editor.commit()

        }

}
