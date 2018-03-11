package com.google.firebase.auth

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class FirebaseWrapper {
  companion object {
    fun isLogged(): Boolean {
      return FirebaseAuth.getInstance().currentUser != null
    }

    fun getRemoteVersion(): Int {
      return Integer.parseInt(
          FirebaseRemoteConfig.getInstance().getString("current_version"))
    }

    fun getMinVersion(): Int {
      return Integer.parseInt(FirebaseRemoteConfig.getInstance().getString("min_version"))
    }
  }
}
