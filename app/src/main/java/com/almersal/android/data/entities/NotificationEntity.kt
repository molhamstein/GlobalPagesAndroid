package com.almersal.android.data.entities

class NotificationEntity {
    var id: String = ""
    var recipientId: String = ""
    var message: String = ""
    var _type: String = ""
    var seen: Boolean = false
    var clicked: Boolean = false
    var data: DataClass = DataClass()
    var creationDate: String = ""

    class DataClass {
        var volumeId: String = ""
    }

}