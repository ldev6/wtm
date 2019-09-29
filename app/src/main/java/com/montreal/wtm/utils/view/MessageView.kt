package com.montreal.wtm.utils.view

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.montreal.wtm.R


class MessageView : RelativeLayout {

  private var messageViewInterface: MessageViewInterface? = null

  private var progressBar: ProgressBar? = null
  private var textViewErrorMessage: TextView? = null
  private var textViewInformation: TextView? = null
  private var btRetry: Button? = null


  @ColorRes
  private var infoBoxColor: Int = 0
  @ColorRes
  private var backgroundViewColor: Int = 0
  @ColorRes
  private var textColor: Int = 0
  @ColorRes
  private var textErrorColor: Int = 0

  interface MessageViewInterface {
    fun retry()
  }

  constructor(context: Context) : super(context) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs,
      defStyle) {
    init(context)
  }

  fun init(context: Context) {
    LayoutInflater.from(context).inflate(R.layout.message_view, this, true)
    progressBar = findViewById<View>(R.id.pgLoading) as ProgressBar
    textViewErrorMessage = findViewById<View>(R.id.tvError) as TextView
    btRetry = findViewById<View>(R.id.btRetry) as Button
    textViewErrorMessage!!.visibility = View.GONE
    textViewInformation = findViewById<View>(R.id.tvInformation) as TextView

    infoBoxColor = R.color.colorPrimary
    backgroundViewColor = R.color.transGrey
    textColor = android.R.color.white
    textErrorColor = android.R.color.white

    btRetry!!.setBackgroundColor(ContextCompat.getColor(context, infoBoxColor))
    this.setBackgroundColor(ContextCompat.getColor(context, R.color.transGrey))
  }

  fun setProperties(@ColorRes infoBoxColor: Int, @ColorRes backgroundColor: Int, @ColorRes textColor: Int, @ColorRes textErrorColor: Int) {
    this.infoBoxColor = infoBoxColor
    this.backgroundViewColor = backgroundColor
    this.textColor = textColor
    this.textErrorColor = textErrorColor

    textViewInformation!!.setTextColor(ContextCompat.getColor(context, textColor))
    textViewErrorMessage!!.setTextColor(ContextCompat.getColor(context, textErrorColor))
    setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
    btRetry!!.setBackgroundColor(ContextCompat.getColor(context, infoBoxColor))
  }

  fun setMessageViewInterface(messageViewInterface: MessageViewInterface) {
    this.messageViewInterface = messageViewInterface
    btRetry!!.setOnClickListener { messageViewInterface.retry() }
  }

  fun setMessageInformation(text: String) {
    this.visibility = View.VISIBLE
    textViewInformation!!.visibility = View.VISIBLE
    textViewInformation!!.text = text
  }

  fun setMessageError(text: String) {
    this.visibility = View.VISIBLE
    hideProgessBar()
    this.textViewInformation!!.visibility = View.GONE
    textViewErrorMessage!!.visibility = View.VISIBLE
    textViewErrorMessage!!.text = text
    if (messageViewInterface != null) {
      btRetry!!.visibility = View.VISIBLE
    }
  }

  fun showProgressBar() {
    hideMessageError()
    progressBar!!.visibility = View.VISIBLE
    progressBar!!.animate()
  }

  fun hideMessageError() {
    textViewErrorMessage!!.visibility = View.GONE
    btRetry!!.visibility = View.GONE
  }

  private fun hideProgessBar() {
    progressBar!!.visibility = View.GONE
    progressBar!!.clearAnimation()
  }

  companion object {

    fun hideLoadingView(messageView: MessageView?) {
      if (messageView != null) {
        messageView.hideProgessBar()
        messageView.visibility = View.GONE
      }
    }
  }

}