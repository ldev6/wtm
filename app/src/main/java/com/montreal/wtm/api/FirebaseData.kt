package com.montreal.wtm.api

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.montreal.wtm.model.Day
import com.montreal.wtm.model.Location
import com.montreal.wtm.model.PartnerCategory
import com.montreal.wtm.model.Session
import com.montreal.wtm.model.Speaker
import com.montreal.wtm.model.Sponsor
import com.montreal.wtm.model.Timeslot
import com.montreal.wtm.utils.NetworkUtils
import com.montreal.wtm.utils.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

object FirebaseData {

  private val TAG = FirebaseData::class.java.simpleName

  enum class ErrorFirebase {
    network,
    firebase
  }

  interface RequestListener<T> {
    fun onDataChange(data: T?)

    fun onCancelled(errorType: ErrorFirebase)
  }

  fun getSpeaker(activity: Activity, requestListener: RequestListener<Speaker>,
      speakerId: Int) {
    val fileName = "Speaker_$speakerId.json"
    firebaseConnected(activity, requestListener, fileName, Speaker::class.java)
    val myRef = FirebaseDatabase.getInstance().getReference(
        Utils.getLanguage() + "/speakers/" + speakerId)
    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val speaker = dataSnapshot.getValue(Speaker::class.java)
        if (speaker != null) {
          requestListener.onDataChange(speaker)
          saveInFile(activity, fileName, speaker)
        } else {
          if (NetworkUtils.isNetworkAvailable(activity.baseContext)) {
            requestListener.onCancelled(ErrorFirebase.network)
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get Speaker failed" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getSpeakers(activity: Activity,
      requestListener: RequestListener<HashMap<Integer, Speaker?>>) {

    val fileName = "Speakers.json"
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<Integer, Speaker>>() {

        }.type)

    val myRef = FirebaseDatabase.getInstance().getReference(Utils.getLanguage() + "/speakers")

    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val map = HashMap<Integer, Speaker?>()
        for (children in dataSnapshot.children) {
          val speaker = children.getValue(Speaker::class.java)
          map[Integer(children.key)] = speaker
        }
        requestListener.onDataChange(map)
        saveInFile(activity, fileName, map)
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get Speakers failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getSchedule(activity: Activity, requestListener: RequestListener<ArrayList<Day>>) {
    val fileName = "Schedule.json"
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<ArrayList<Timeslot>>() {

        }.type)

    val myRef = FirebaseDatabase.getInstance().getReference(Utils.getLanguage() + "/schedule")
    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.children != null) {
          val days = ArrayList<Day>()
          for (children in dataSnapshot.children) {
            days.add(children.getValue(Day::class.java)!!)
          }
          saveInFile(activity, fileName, days)
          requestListener.onDataChange(days)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get Schedule failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getMyShedule(activity: Activity, requestListener: RequestListener<HashMap<String, Boolean>>) {
    val fileName = "mySession.json"
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();

    val myRef = FirebaseDatabase.getInstance().getReference("/userSessions/" + uid)
    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.children != null) {
          val mySessions = HashMap<String, Boolean>()
          for (children in dataSnapshot.children) {
            mySessions.put(children.key, children.getValue(Boolean::class.java)!!)
          }
          saveInFile(activity, fileName, mySessions)
          requestListener.onDataChange(mySessions)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get My Schedule failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })

  }

  fun saveSession(sessionId: String, save: Boolean) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
    if(save) {
      FirebaseDatabase.getInstance().reference.child("userSessions").child(uid).child(
          sessionId).setValue(save)
    } else {
      FirebaseDatabase.getInstance().reference.child("userSessions").child(uid).child(
          sessionId).setValue(null)
    }
  }

  fun getSessions(activity: Activity, requestListener: RequestListener<HashMap<String, Session>>) {
    val fileName = "Sessions.json"

    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<ArrayList<Timeslot>>() {

        }.type)

    val myRef = FirebaseDatabase.getInstance().getReference(Utils.getLanguage() + "/sessions")
    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.children != null) {
          val sessions = HashMap<String, Session>()
          for (children in dataSnapshot.children) {
            sessions.put(children.key, children.getValue(Session::class.java)!!)
          }
          saveInFile(activity, fileName, sessions)
          requestListener.onDataChange(sessions)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get Session failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })

  }

  fun getSponsors(activity: Activity,
      requestListener: RequestListener<HashMap<Integer, PartnerCategory?>>) {
    val fileName = "Sponsors.json"
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<String, ArrayList<Sponsor>>>() {

        }.type)
    val myRef = FirebaseDatabase.getInstance().getReference(Utils.getLanguage() + "/partners")
    myRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val map = HashMap<Integer, PartnerCategory?>()
        for (children in dataSnapshot.children) {
          val partnerCategory = children.getValue(PartnerCategory::class.java)
          map[Integer(children.key)] = partnerCategory
        }
        saveInFile(activity, fileName, map)
        requestListener.onDataChange(map)
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get Sponsors failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getLocation(activity: Activity, requestListener: RequestListener<Location>) {
    val fileName = "Location.json"
    firebaseConnected(activity, requestListener, fileName, Location::class.java as Type)
    val myRef = FirebaseDatabase.getInstance().getReference("location")
    myRef.addValueEventListener(object : ValueEventListener {

      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val location = dataSnapshot.getValue(Location::class.java)
        if (location != null) {
          requestListener.onDataChange(location)
          saveInFile(activity, fileName, location)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Crashlytics.log("Get Location failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun firebaseConnected(activity: Activity, requestListener: RequestListener<*>,
      nameFile: String, type: Type) {
    val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
    connectedRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val connected = snapshot.getValue(Boolean::class.java)!!
        if (!connected) {
          if (!NetworkUtils.isNetworkAvailable(activity.baseContext)) {
            ReadFile(activity, requestListener, type).execute(nameFile)
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Log.v(TAG, "Listener was cancelled")
      }
    })
  }

  private fun saveInFile(context: Context, nameFile: String, `object`: Any?) {
    val gson = Gson()
    val json = gson.toJson(`object`)

    saveFile(context, nameFile, json).observeOn(AndroidSchedulers.mainThread()).subscribe()
  }

  private class ReadFile internal constructor(private val mActivity: Activity,
      private val mRequestListener: RequestListener<*>,
      private val mType: Type) : AsyncTask<String, Void, Void>() {

    override fun doInBackground(vararg params: String): Void? {
      val nameFile = params[0]
      try {
        val fis = mActivity.openFileInput(nameFile)
        val reader = BufferedReader(InputStreamReader(fis, "UTF-8"))

        var charByte: Int
        var dataFile = ""

        do {
          charByte = reader.read()
          if (charByte == -1)
            break
          dataFile = dataFile + Character.toString(charByte.toChar())
        } while (true)


        fis.close()
        val jsonFile = dataFile

        mActivity.runOnUiThread {
          try {
            val gson = Gson()
            val data = gson.fromJson<Nothing?>(jsonFile, mType)
            TODO("Check how to fix this")
            mRequestListener.onDataChange(data)
          } catch (e: JsonSyntaxException) {
            mRequestListener.onCancelled(ErrorFirebase.network)
          }
        }
      } catch (e: IOException) {
        mActivity.runOnUiThread { mRequestListener.onCancelled(ErrorFirebase.network) }
      }

      return null
    }
  }

  private fun saveFile(context: Context, nameFile: String, file: String): Completable {
    return Completable.fromRunnable({
      try {
        val fos = context.openFileOutput(nameFile, Context.MODE_PRIVATE)
        fos.write(file.toByteArray())
        fos.close()
      } catch (e: IOException) {
        Crashlytics.log("Error saving file=" + e.message)
      }
    }).subscribeOn(Schedulers.io())
  }

}
