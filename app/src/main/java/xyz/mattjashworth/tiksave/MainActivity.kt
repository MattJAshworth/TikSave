package xyz.mattjashworth.tiksave

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.breuhteam.apprate.AppRate
import com.github.stephenvinouze.core.managers.KinAppManager
import com.github.stephenvinouze.core.models.KinAppProductType
import com.github.stephenvinouze.core.models.KinAppPurchase
import com.github.stephenvinouze.core.models.KinAppPurchaseResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_main.*
import xyz.mattjashworth.tiksave.toolbox.TokService

class MainActivity : AppCompatActivity(), RewardedVideoAdListener, KinAppManager.KinAppListener {

    private var adsRemoved = false
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private val billingManager = KinAppManager(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtEpputBD9l2TDpH8v/TAv+gvgG92UJYMphL6Bi8KezWa5/6o5AvxX5ywFPjC4GZTtkZrlCZGhkGBzWBip2/0Y52Ur55CHPt+u8/n/dQ2Z0GgLt2tenZBiPixvA18lvXekOnuZ48pJjKSYqkuggMP2wDSCU6KOQQSn2ecpmg/FvUryzQEvgQRrAhkle8lNKqar4Siw4dROSKlwzLwz5Z0FuHGqkRQs4t4k2yPmoWN6iONGgXlCacl2bhaBqlxD//8CVvqtByE383E1pBnyUVMr2Cu6pNcWkrES3LU6nfmtbUu05K899IfIL1rO9hnNkpzpmprnT4dJE8XUkrmIQYtUQIDAQAB")
    val productID = "remove_ads"
    val PREFS_FILENAME = "xyz.mattjashworth.tiksave.prefs"
    var prefs: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        billingManager.bind(this)


        //Ads
        MobileAds.initialize(this, "ADMOB_ID_HERE")
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this
        loadRewardedVideoAd()

        //Billing
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val ads = prefs!!.getBoolean("ads", false)
        if (ads == true) {
            adsRemoved = true
        } else {
            adsRemoved = false
        }


        //App
        val startKit = Intent(this@MainActivity, TokService::class.java)

        try {
            stopService(startKit)
        } catch (e : Exception) {
            //Blah blah
        }

        //AppRate.app_launched(this@MainActivity, packageName,0,4)

        setSupportActionBar(tolus)



        btn_open.setOnClickListener {

            if (adsRemoved == true) {
                val startKit = Intent(this@MainActivity, TokService::class.java)
                phoneTweet("com.zhiliaoapp.musically")
                ContextCompat.startForegroundService(this@MainActivity, startKit)
            } else {

                if (mRewardedVideoAd.isLoaded) {
                    mRewardedVideoAd.show()
                } else {
                    val startKit = Intent(this@MainActivity, TokService::class.java)
                    phoneTweet("com.zhiliaoapp.musically")
                    ContextCompat.startForegroundService(this@MainActivity,startKit)
                }

            }
        }

        btn_twitter.setOnClickListener {

            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MattJAshworth"))
            startActivity(i)

        }

        btn_ads.setOnClickListener {

            //val products = billingManager.fetchProducts(<remove_ads>, KinAppProductType.INAPP).await()
            billingManager.purchase(this, productID, KinAppProductType.INAPP)
            //val purchases = billingManager.restorePurchases(KinAppProductType.INAPP)

        }


    }

    override fun onRewarded(reward: RewardItem) {

        // Reward the user.
        val startKit = Intent(this@MainActivity, TokService::class.java)
        phoneTweet("com.zhiliaoapp.musically")
        ContextCompat.startForegroundService(this@MainActivity,startKit)
    }

    private fun loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ADMOB_ID_HERE",
                AdRequest.Builder().build())
    }



    private fun phoneTweet(packageN: String) {
        val apppackage = packageN
        try {
            val i = packageManager.getLaunchIntentForPackage(apppackage)
            startActivity(i)
        } catch (e: Exception) {
           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)))
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolx,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.menu_saves ->{


                startActivity(Intent(this@MainActivity,Downloaded::class.java))
            }


            R.id.menu_rate ->{

                AppRate.app_launched(this, packageName)


            }

        }

        return super.onOptionsItemSelected(item)
    }





    override fun onRewardedVideoCompleted() {
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
        val startKit = Intent(this@MainActivity, TokService::class.java)
        phoneTweet("com.zhiliaoapp.musically")
        ContextCompat.startForegroundService(this@MainActivity,startKit)
    }

    override fun onRewardedVideoAdLeftApplication() {
    }


    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
    }

    override fun onRewardedVideoAdLoaded() {
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoStarted() {
    }



    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(this)
    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        billingManager.unbind()
        mRewardedVideoAd.destroy(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingManager.verifyPurchase(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBillingReady() {
        // From this point you can use the Manager to fetch/purchase/consume/restore items
    }


    override fun onPurchaseFinished(purchaseResult: KinAppPurchaseResult, purchase: KinAppPurchase?) {
        // Handle your purchase result here
        when (purchaseResult) {
            KinAppPurchaseResult.SUCCESS -> {
                // Purchase successful with a non-null KinAppPurchase object.
                // You may choose to consume this item right now if you want to be able to re-buy it
                adsRemoved = true
                val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
                val editor = prefs!!.edit()
                editor.putBoolean("ads", true)
                editor.apply()
            }
            KinAppPurchaseResult.ALREADY_OWNED -> {
                // You already own this item. If you need to buy it again, consider consuming it first (you may need to restore your purchases before that)
                adsRemoved = true
                val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
                val editor = prefs!!.edit()
                editor.putBoolean("ads", true)
                editor.apply()
            }
            KinAppPurchaseResult.INVALID_PURCHASE -> {
                // Purchase invalid and cannot be processed
            }
            KinAppPurchaseResult.INVALID_SIGNATURE -> {
                // Marked as success from the Google Store but signature detected as invalid and should not be processed
            }
            KinAppPurchaseResult.CANCEL -> {
                // Manual cancel from the user
            }
        }
    }
}
