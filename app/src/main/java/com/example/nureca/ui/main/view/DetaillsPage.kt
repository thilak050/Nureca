package com.example.nureca.ui.main.view

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nureca.R
import com.example.nureca.data.model.StateDataKotlin
import com.example.nureca.databinding.ActivityDetaillsPageBinding
import io.realm.Realm
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import io.realm.mongodb.mongo.result.InsertOneResult
import org.bson.Document
import org.json.JSONArray
import org.json.JSONException


class DetaillsPage : AppCompatActivity() {
    var positive = ""
    var negative = ""
    var death = ""
    var hospitalized = ""
    var realm: Realm? = null


    private lateinit var binding: ActivityDetaillsPageBinding


    var Appid = "nureca-xqbrz"
    private var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaills_page)
        binding = ActivityDetaillsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        Realm.init(this)

        app = App(AppConfiguration.Builder(Appid).build())

        realm = Realm.getDefaultInstance()


        positive = intent.getStringExtra("positive").toString()
        negative = intent.getStringExtra("negative").toString()
        death = intent.getStringExtra("death").toString()
        hospitalized = intent.getStringExtra("hospitalized").toString()

        binding.tvPositive.text = "Positive: " + positive;
        binding.tvNegative.text = "Negative: " + negative;
        binding.tvDeath.text = "Death: " + death;
        binding.tvHospitalized.text = "Hospitalized: " + hospitalized;

        binding.btnRemovedata.setOnClickListener {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            realm.deleteAll()


            realm.commitTransaction()


            val results: RealmResults<StateDataKotlin> =
                realm.where(StateDataKotlin::class.java).findAll()


            Log.e("allstoreddat", results.asJSON())


            val arrayAdapter: ArrayAdapter<*>


            val list = ArrayList<String>()
            val mArray: JSONArray
            try {
                mArray = JSONArray(results.asJSON())
                for (i in 0 until mArray.length()) {
                    val mJsonObject = mArray.getJSONObject(i)

                    list.add(
                        "State Id: " + mJsonObject.getString("state_id") + "\n" +
                                "Positive: " + mJsonObject.getString("positive") + "\n" +
                                "Negative: " + mJsonObject.getString("negative") + "\n" +
                                "Death: " + mJsonObject.getString("death") + "\n" +
                                "Hospitalized: " + mJsonObject.getString("hospitalized") + "\n"
                    )

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            var mListView = findViewById<ListView>(R.id.userlist)
            arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, list
            )
            mListView.adapter = arrayAdapter
        }
        savedata()

    }




    private fun sync()
    {
        var mongoDatabase: MongoDatabase? = null
        var mongoClient: MongoClient? = null
        val user: User = app!!.currentUser()!!
        mongoClient = user.getMongoClient("mongodb-atlas")
        mongoDatabase = mongoClient.getDatabase("NurecaDB")
        val mongoCollection: MongoCollection<Document> = mongoDatabase.getCollection("StateDataInputs")

        mongoCollection.insertOne(
            Document("userid", user.id).append(
                "data",
                "Positive: "+positive+"\n"+
                "Negative: "+negative+"\n"+
                "Death: "+death+"\n"+
                "Hospitalized: "+hospitalized
            )
        ).getAsync { result: App.Result<InsertOneResult?> ->
            if (result.isSuccess) {
                Log.v("Data", "Data Inserted Successfully")
                Toast.makeText(
                    this, "Data Inserted Successfully to real-MongoDB !!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.v("Data", "Error:" + result.error.toString())
            }
        }
    }

    private fun savedata() {
        realm!!.executeTransactionAsync({ bgRealm ->
            val maxId = bgRealm.where(StateDataKotlin::class.java).max("state_id")
            val newkey = if (maxId == null) 1 else maxId.toInt() + 1
            val stateData = bgRealm.createObject(StateDataKotlin::class.java, newkey)
            stateData.positive = positive
            stateData.negative = negative
            stateData.death = death
            stateData.hospitalized = hospitalized
        },
            {
                sync()
                Toast.makeText(this@DetaillsPage, "Data Saved to realm !!", Toast.LENGTH_SHORT)
                    .show()
                val realm = Realm.getDefaultInstance()
                val results: RealmResults<StateDataKotlin> =
                    realm.where(StateDataKotlin::class.java).findAll()


                Log.e("allstoreddat", results.asJSON())


                val arrayAdapter: ArrayAdapter<*>


                val list = ArrayList<String>()
                val mArray: JSONArray
                try {
                    mArray = JSONArray(results.asJSON())
                    for (i in 0 until mArray.length()) {
                        val mJsonObject = mArray.getJSONObject(i)

                        list.add(
                            "State Id: " + mJsonObject.getString("state_id") + "\n" +
                                    "Positive: " + mJsonObject.getString("positive") + "\n" +
                                    "Negative: " + mJsonObject.getString("negative") + "\n" +
                                    "Death: " + mJsonObject.getString("death") + "\n" +
                                    "Hospitalized: " + mJsonObject.getString("hospitalized") + "\n"
                        )

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                var mListView = findViewById<ListView>(R.id.userlist)
                arrayAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1, list
                )
                mListView.adapter = arrayAdapter
            }
        ) { Toast.makeText(this@DetaillsPage, "Failed to save !!", Toast.LENGTH_SHORT).show() }
    }
}

