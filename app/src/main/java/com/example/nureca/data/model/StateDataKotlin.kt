package com.example.nureca.data.model
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey



open class StateDataKotlin : RealmObject() {
    @PrimaryKey
    var state_id = 0
    var positive: String? = null
    var negative: String? = null
    var death: String? = null
    var hospitalized: String? = null
}