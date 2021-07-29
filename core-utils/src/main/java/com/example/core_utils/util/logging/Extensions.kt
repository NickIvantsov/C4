package com.example.core_utils.util.logging

import android.text.Editable


fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)