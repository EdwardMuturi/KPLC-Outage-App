package com.kplc.outage.presentation.utils

import com.kplc.outage.domain.utils.Platform

class Greeting {
    fun greet(): String {
        return "Hello, ${Platform().platform}!"
    }
}
