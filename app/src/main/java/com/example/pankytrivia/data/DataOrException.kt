package com.example.pankytrivia.data

data class DataOrException<T, Boolean, E:Exception> (
    var data: T? = null,
    var boolean: Boolean? = null,
    var e:E? = null
)