package pm.gnosis.heimdall.ui.settings.general

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import pm.gnosis.heimdall.R
import pm.gnosis.heimdall.reporting.ScreenId
import pm.gnosis.heimdall.ui.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import pm.gnosis.heimdall.databinding.ScreenGetInTouchBinding
import pm.gnosis.svalinn.common.utils.snackbar


class GetInTouchActivity : BaseActivity() {

    private val binding by lazy {
        ScreenGetInTouchBinding.inflate(layoutInflater)
    }

    override fun screenId() = ScreenId.GET_IN_TOUCH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            toolbarBackArrow.setOnClickListener {
                onBackPressed()
            }

            telegram.setOnClickListener {
                openTelegramChannel()
            }
            email.setOnClickListener {
                sendEmail()
            }
            gitter.setOnClickListener {
                openGitterChannel()
            }
        }
    }

    private fun openTelegramChannel() {
        var intent = try {
            Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=${getString(R.string.gnosis_telegram_id)}"))
        } catch (e: Exception) { //App not found open in browser
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/${getString(R.string.gnosis_telegram_id)}"))
        }
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/html"
        intent.data = Uri.parse("mailto:${getString(R.string.feedback_email)}")
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject, getString(R.string.app_name)))
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            snackbar(binding.root, getString(R.string.email_chooser_error), Snackbar.LENGTH_SHORT)
        }
    }

    private fun openGitterChannel() {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gnosis_gitter_url)))
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, GetInTouchActivity::class.java)
    }
}
