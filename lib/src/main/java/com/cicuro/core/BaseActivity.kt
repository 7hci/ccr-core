package com.cicuro.core

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cicuro.core.models.Config
import com.cicuro.core.models.EnrollmentInfo
import com.cicuro.core.models.LoginResult
import com.cicuro.core.models.RequestHeaders
import com.cicuro.core.utils.disposeWith
import com.cicuro.core.utils.getFile
import com.cicuro.core.utils.readData
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.i18next.android.I18Next
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BaseActivity<T : BaseContext> : AppCompatActivity() {
  abstract val layoutId: Int
  abstract val fragmentHolderId: Int
  abstract val statusBarColorId: Int
  abstract val defaultFragment: Fragment
  abstract val translationsResourceMap: Map<String, Int>

  val enterAnimation: Int = android.R.anim.fade_in
  val exitAnimation: Int = android.R.anim.fade_out

  val disposable = CompositeDisposable()
  var cicuroContext: T? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    EventBus.register<GenericErrorEvent>()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        showServerErrorAlert()
      }, Timber::e)
      .disposeOnDestroy()

    askPermission(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE) {
      try {
        val config = readData<Config>(getFile(CONFIG_FILENAME))
        val enrollmentInfo = readData<EnrollmentInfo>(getFile(ENROLLMENT_FILENAME))
        val requestHeaders = readData<RequestHeaders>(getFile(HEADERS_FILENAME))
        val loginResult = readData<LoginResult>(getFile(LOGIN_RESULT_FILENAME))
        val app = (application as BaseApplication)

        cicuroContext = createCicuroContext(config, enrollmentInfo, requestHeaders, loginResult)
        configureI18Next(translationsResourceMap, loginResult?.language)

        app.initializeSentry(config?.sentryDSN, enrollmentInfo?.id, loginResult)

        setContentView(layoutId)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(statusBarColorId)

        navigate(defaultFragment, false)
        handleEvents()
      } catch (e: Throwable) {
        Timber.e(e)
        showServerErrorAlert()
      }
    }
  }

  /**
   * Handle any events from the bus here
   */
  abstract fun handleEvents()

  /**
   * Create the context object that will be used throughout the rest of the app
   */
  abstract fun createCicuroContext(
    config: Config?,
    enrollmentInfo: EnrollmentInfo?,
    requestHeaders: RequestHeaders?,
    loginResult: LoginResult?
  ): T

  override fun onStart() {
    super.onStart()
    // The first time the app runs after being installed, this will fail
    // because permissions have not been granted
    try {
      val loginResult = readData<LoginResult>(getFile(LOGIN_RESULT_FILENAME))
      if (loginResult?.language != cicuroContext?.loginResult?.language) {
        cicuroContext?.loginResult = loginResult
        I18Next.getInstance().options.language = loginResult?.language ?: "en"
        EventBus.post(LanguageChangedEvent())
      }
    } catch (e: Throwable) {
      // Do nothing
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.clear()
  }

  private fun configureI18Next(resourceMap: Map<String, Int>, language: String?) {
    val i18n = I18Next.getInstance()
    i18n.options.language = language ?: "en"
    i18n.options.defaultNamespace = "translation"
    i18n.options.keySeparator = "."
    i18n.options.nsSeparator = ":"
    i18n.options.interpolationPrefix = "{{"
    i18n.options.interpolationSuffix = "}}"

    for (entry in resourceMap.entries) {
      i18n.loader()
        .namespace("translation")
        .lang(entry.key)
        .from(application.applicationContext, entry.value)
        .load()
    }
  }


  private fun showServerErrorAlert() {
    val title = I18Next.getInstance().t("server_error_title")
    val message = I18Next.getInstance().t("server_error_message")
    showAlert(title, message)
  }

  protected fun showAlert(title: String, message: String) {
    val okText = I18Next.getInstance().t("ok")
    AlertDialog.Builder(this)
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton(okText) { dialog, _ ->
        dialog.dismiss()
      }
      .create()
      .show()
  }

  /**
   * Replace the currently shown main fragment
   */
  fun navigate(
    fragment: Fragment,
    addToBackStack: Boolean = true
  ) {
    val transaction = supportFragmentManager.beginTransaction()
      .setCustomAnimations(enterAnimation, exitAnimation)
      .replace(fragmentHolderId, fragment)

    if (addToBackStack) {
      transaction.addToBackStack(fragment::javaClass.name)
    }

    transaction.commitAllowingStateLoss()
  }

  fun Disposable.disposeOnDestroy(): Disposable = apply {
    this.disposeWith(disposable)
  }
}