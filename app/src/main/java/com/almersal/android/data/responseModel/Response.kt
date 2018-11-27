package com.almersal.android.data.responseModel

/**
 * Created by Adhamkh on 2018-06-15.
 */
class Response<T> {
    var error: ErrorMessage? = null
    var body: T? = null

}