package com.montreal.wtm.api

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.montreal.wtm.model.Day
import com.montreal.wtm.model.Location
import com.montreal.wtm.model.Partner
import com.montreal.wtm.model.Session
import com.montreal.wtm.model.Speaker
import com.montreal.wtm.model.Timeslot
import com.montreal.wtm.utils.NetworkUtils
import com.montreal.wtm.utils.Utils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
//import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

object FirebaseData {
  private val SCHEDULE = "schedule"
  private val MY_RATINGS_JSON = "myRatings.json"
  private val MY_SESSION_JSON = "mySession.json"
  private val SESSIONS_JSON = "Sessions.json"
  private val SPEAKERS_JSON = "Speakers.json"
  private val SCHEDULE_JSON = "Schedule.json"
  private val USER_SESSIONS = "userSessions"
  private val RATINGS = "ratings"
  private val SPEAKERS = "speakers"
  private val SESSIONS = "sessions"
  private val PARTNERS = "partners"

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
    val myRef = getReference(
        Utils.getLanguage() + "/" + SPEAKERS + "/" + speakerId)
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

  //TODO in next version speakerId and global array id should match
  fun getSpeakerByInnerSpeakerId(activity: Activity, requestListener: RequestListener<Speaker>,
      speakerId: Int) {
    val fileName = "Speaker_$speakerId.json"
    firebaseConnected(activity, requestListener, fileName, Speaker::class.java)
    val reference = FirebaseDatabase.getInstance().reference.child(Utils.getLanguage()).child(
        SPEAKERS).orderByChild("id").equalTo(speakerId.toDouble())
    reference?.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          val speaker = dataSnapshot.children.first().getValue(Speaker::class.java)

          if (speaker != null) {
            requestListener.onDataChange(speaker)
            saveInFile(activity, fileName, speaker)
          } else {
            if (NetworkUtils.isNetworkAvailable(activity.baseContext)) {
              requestListener.onCancelled(ErrorFirebase.network)
            }
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

  private fun getReference(reference: String): DatabaseReference {
    return FirebaseDatabase.getInstance().getReference(reference)
  }

  fun getSpeakers(activity: Activity,
      requestListener: RequestListener<HashMap<Integer, Speaker?>>) {

    val fileName = SPEAKERS_JSON
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<Integer, Speaker>>() {
        }.type)
    val speakers = Utils.getLanguage() + "/" + SPEAKERS

    getReference(speakers)?.addValueEventListener(object : ValueEventListener {
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
    val fileName = SCHEDULE_JSON
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<ArrayList<Timeslot>>() {

        }.type)

    val ref = Utils.getLanguage() + "/" + SCHEDULE
    getReference(ref)?.addValueEventListener(object : ValueEventListener {
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
    val fileName = MY_SESSION_JSON
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();

    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<String, Boolean>>() {

        }.type)

    val myRef = getReference("/" + USER_SESSIONS + "/" + uid)
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

  fun getMyRatings(activity: Activity, requestListener: RequestListener<HashMap<String, Long>>) {
    val fileName = MY_RATINGS_JSON
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();

    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<String, Boolean>>() {

        }.type)

    val myRef = getReference("/" + RATINGS + "/" + uid + "/" + SESSIONS)
    myRef?.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.children != null) {
          val ratings = HashMap<String, Long>()
          for (children in dataSnapshot.children) {
            ratings.put(children.key, children.getValue(Long::class.java)!!)
          }
          saveInFile(activity, fileName, ratings)
          requestListener.onDataChange(ratings)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get ratings failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getSession(activity: Activity, requestListener: RequestListener<Session>, sessionId: Int) {
    firebaseConnected(activity, requestListener, SESSIONS_JSON, Speaker::class.java)
    val session = getReference(Utils.getLanguage() + "/" + SESSIONS + "/" + sessionId)
    session.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          val session = dataSnapshot.getValue(Session::class.java)
          requestListener.onDataChange(session)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        Crashlytics.log("Get session failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getMySessionState(activity: Activity, requestListener: RequestListener<Pair<String, Boolean>>,
      sessionId: Int) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();

    getReference(
        "/" + USER_SESSIONS + "/" + uid + "/" + sessionId)?.addValueEventListener(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
              val mySessionState = Pair(dataSnapshot.key ?: "",
                  dataSnapshot.getValue(Boolean::class.java) ?: false)
              requestListener.onDataChange(mySessionState)
            }
          }

          override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Crashlytics.log("Get My session stats failed =" + error.message)
            requestListener.onCancelled(ErrorFirebase.firebase)
          }
        })
  }

  fun getMySessionRating(activity: Activity, requestListener: RequestListener<Pair<String, Long>>,
      sessionId: Int) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
    if (uid == null) {
      return
    }

    val sessionRating = FirebaseDatabase.getInstance().reference
        .child(RATINGS)
        .child(uid)
        .child(SESSIONS)
        .child(sessionId.toString())

    sessionRating.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.exists()) {
          val mySessionRating = Pair(dataSnapshot.key ?: "",
              dataSnapshot.getValue(Long::class.java) ?: 0)
          requestListener.onDataChange(mySessionRating)
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get My session stats failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  private fun getUserData(uid: String, userField: String): DatabaseReference? {

    return FirebaseDatabase.getInstance().reference.child(userField).child(uid)
  }

  fun saveSession(sessionId: Int, save: Boolean) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
//    Timber.d("Session id: " + sessionId)
    uid?.let {
      getUserData(uid, USER_SESSIONS)?.child(sessionId?.toString())?.setValue(save)
    }
  }

  fun saveSessionRating(sessionId: Int, rating: Int) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
//    Timber.d("Session id: " + sessionId + ", rating: " + rating)
    uid?.let {
      getUserData(uid, RATINGS)?.child(SESSIONS)?.child(sessionId?.toString())?.setValue(rating)
    }

  }

  fun getSessions(activity: Activity,
      requestListener: RequestListener<HashMap<String, Session>>) {
    val fileName = SESSIONS_JSON

    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<ArrayList<Timeslot>>() {

        }.type)

    val myRef = getReference(Utils.getLanguage() + "/" + SESSIONS)
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

  fun getPartners(activity: Activity,
      requestListener: RequestListener<HashMap<Integer, Partner?>>) {
    val fileName = "Sponsors.json"
    firebaseConnected(activity, requestListener, fileName,
        object : TypeToken<HashMap<String, ArrayList<Partner>>>() {

        }.type)
    val ref = Utils.getLanguage() + "/" + PARTNERS
    getReference(ref)?.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val map = HashMap<Integer, Partner?>()
        for (children in dataSnapshot.children) {
          val partnerCategory = children.getValue(Partner::class.java)
          map[Integer(children.key)] = partnerCategory
        }
        saveInFile(activity, fileName, map)
        requestListener.onDataChange(map)
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
        Crashlytics.log("Get partners failed =" + error.message)
        requestListener.onCancelled(ErrorFirebase.firebase)
      }
    })
  }

  fun getLocation(activity: Activity, requestListener: RequestListener<Location>) {
    val fileName = "Location.json"
    firebaseConnected(activity, requestListener, fileName, Location::class.java as Type)
    val myRef = getReference(Utils.getLanguage()).child("location")
    myRef.addListenerForSingleValueEvent(object : ValueEventListener {

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
    getReference(".info/connected")?.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val connected = snapshot.getValue(Boolean::class.java)!!
        if (!connected) {
          if (!NetworkUtils.isNetworkAvailable(activity.baseContext)) {
            ReadFile(activity, requestListener, type).execute(nameFile)
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {
//        Timber.v("Listener was cancelled")
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
          } catch (e: Exception) {
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
