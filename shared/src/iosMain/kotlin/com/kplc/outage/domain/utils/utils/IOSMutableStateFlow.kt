package com.kplc.outage.domain.utils.utils

import kotlinx.coroutines.flow.MutableStateFlow

class IOSMutableStateFlow<T>(
    initialValue: T,
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))
