package dev.ronnie.imageloaderdagger2.utils

import android.content.Context
import android.widget.Toast


/**
 *created by Ronnie Otieno on 04-Apr-21.
 **/
fun Context.toast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}