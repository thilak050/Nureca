package com.example.nureca.ui.main.view

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nureca.R
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User


class MainActivity : AppCompatActivity() {
    val appID = "nureca-xqbrz"
    var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()


        Realm.init(this)
        val app = App(
           AppConfiguration.Builder(appID)
               .build()
       )


        val anonymousCredentials: Credentials = Credentials.anonymous()
        var user: User?
        app.loginAsync(anonymousCredentials) {
            if (it.isSuccess) {
                Log.v("AUTH", "Successfully authenticated anonymously.")
                Toast.makeText(
                    this, "Successfully authenticated anonymously.",
                    Toast.LENGTH_SHORT
                ).show()
                user = app.currentUser()
            } else {
                Log.e("AUTH", it.error.toString())
            }
        }

        val configuration = RealmConfiguration.Builder().name("realmdata.db").build()
        Realm.setDefaultConfiguration(configuration)
        realm = Realm.getDefaultInstance()


        Handler().postDelayed({
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)
            finish()

        }, 3500)
    }
}