package comm.xuanthuan.watchanime.Activity

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import comm.xuanthuan.watchanime.R
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext


class MainActivity : AppCompatActivity() {
    val messageDialog: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    FirebaseMessaging.getInstance().isAutoInitEnabled = true
    val intent = intent
    if (intent != null)
    {
        try {
            val key = intent.extras!!.getString("package")
            if (key != null) {
                try {
                    startActivity(
                            Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$key")
                            )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                            Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$key")
                            )
                    )
                }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    val sharedPreferences = getSharedPreferences("checkLogin", MODE_PRIVATE)
    if (!sharedPreferences.contains("checklogin"))
    {
        sharedPreferences.edit().putInt("checklogin", 1).apply()
        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.dialog_privacy)
        val txtConfirm = dialog.findViewById<TextView>(R.id.confrim_privacy)
        val txt_policy = dialog.findViewById<TextView>(R.id.txt_policy)
        txt_policy.text = "Anime Tv - Watch Anime Online\n" +
                "Privacy Policy of Anime Tv - Watch Anime Online\n" +
                "Privacy Policy\n" +
                "Effective date: January 24, 2019\n" +
                "Anime Tv - Watch Anime Online (\"us\", \"we\", or \"our\") operates the Anime Tv - Watch Anime Online mobile application (hereinafter referred to as the \"Service\").\n" +
                "This page informs you of our policies regarding the collection, use, and disclosure of personal data when you use our Service and the choices you have associated with that data. Our Privacy Policy for Anime Tv - Watch Anime Online is created with the help of the PrivacyPolicies.com Privacy Policy Generator.\n" +
                "We use your data to provide and improve the Service. By using the Service, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, the terms used in this Privacy Policy have the same meanings as in our Terms and Conditions.\n" +
                "Information Collection And Use\n" +
                "We collect several different types of information for various purposes to provide and improve our Service to you.\n" +
                "Types of Data Collected\n" +
                "Personal Data\n" +
                "While using our Service, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you (\"Personal Data\"). Personally identifiable information may include, but is not limited to:\n" +
                "●\tCookies and Usage Data\n" +
                "Usage Data\n" +
                "When you access the Service with a mobile device, we may collect certain information automatically, including, but not limited to, the type of mobile device you use, your mobile device unique ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browser you use, unique device identifiers and other diagnostic data (\"Usage Data\").\n" +
                "Tracking & Cookies Data\n" +
                "We use cookies and similar tracking technologies to track the activity on our Service and hold certain information.\n" +
                "Cookies are files with small amount of data which may include an anonymous unique identifier. Cookies are sent to your browser from a website and stored on your device. Tracking technologies also used are beacons, tags, and scripts to collect and track information and to improve and analyze our Service.\n" +
                "You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Service. You can learn more how to manage cookies in the Browser Cookies Guide.\n" +
                "Examples of Cookies we use:\n" +
                "●\tSession Cookies. We use Session Cookies to operate our Service.\n" +
                "●\tPreference Cookies. We use Preference Cookies to remember your preferences and various settings.\n" +
                "●\tSecurity Cookies. We use Security Cookies for security purposes.\n" +
                "Use of Data\n" +
                "Anime Tv - Watch Anime Online uses the collected data for various purposes:\n" +
                "●\tTo provide and maintain the Service\n" +
                "●\tTo notify you about changes to our Service\n" +
                "●\tTo allow you to participate in interactive features of our Service when you choose to do so\n" +
                "●\tTo provide customer care and support\n" +
                "●\tTo provide analysis or valuable information so that we can improve the Service\n" +
                "●\tTo monitor the usage of the Service\n" +
                "●\tTo detect, prevent and address technical issues\n" +
                "Transfer Of Data\n" +
                "Your information, including Personal Data, may be transferred to — and maintained on — computers located outside of your state, province, country or other governmental jurisdiction where the data protection laws may differ than those from your jurisdiction.\n" +
                "If you are located outside United States and choose to provide information to us, please note that we transfer the data, including Personal Data, to United States and process it there.\n" +
                "Your consent to this Privacy Policy followed by your submission of such information represents your agreement to that transfer.\n" +
                "Anime Tv - Watch Anime Online will take all steps reasonably necessary to ensure that your data is treated securely and in accordance with this Privacy Policy and no transfer of your Personal Data will take place to an organization or a country unless there are adequate controls in place including the security of your data and other personal information.\n" +
                "Disclosure Of Data\n" +
                "Legal Requirements\n" +
                "Anime Tv - Watch Anime Online may disclose your Personal Data in the good faith belief that such action is necessary to:\n" +
                "●\tTo comply with a legal obligation\n" +
                "●\tTo protect and defend the rights or property of Anime Tv - Watch Anime Online\n" +
                "●\tTo prevent or investigate possible wrongdoing in connection with the Service\n" +
                "●\tTo protect the personal safety of users of the Service or the public\n" +
                "●\tTo protect against legal liability\n" +
                "Security Of Data\n" +
                "The security of your data is important to us, but remember that no method of transmission over the Internet, or method of electronic storage is 100% secure. While we strive to use commercially acceptable means to protect your Personal Data, we cannot guarantee its absolute security.\n" +
                "Service Providers\n" +
                "We may employ third party companies and individuals to facilitate our Service (\"Service Providers\"), to provide the Service on our behalf, to perform Service-related services or to assist us in analyzing how our Service is used.\n" +
                "These third parties have access to your Personal Data only to perform these tasks on our behalf and are obligated not to disclose or use it for any other purpose.\n" +
                "Analytics\n" +
                "We may use third-party Service Providers to monitor and analyze the use of our Service.\n" +
                "●\tGoogle Analytics\n" +
                "Google Analytics is a web analytics service offered by Google that tracks and reports website traffic. Google uses the data collected to track and monitor the use of our Service. This data is shared with other Google services. Google may use the collected data to contextualize and personalize the ads of its own advertising network.\n" +
                "You may opt-out of certain Google Analytics features through your mobile device settings, such as your device advertising settings or by following the instructions provided by Google in their Privacy Policy: https://policies.google.com/privacy?hl=en\n" +
                "For more information on the privacy practices of Google, please visit the Google Privacy & Terms web page: https://policies.google.com/privacy?hl=en\n" +
                "●\tFirebase\n" +
                "Firebase is analytics service provided by Google Inc.\n" +
                "You may opt-out of certain Firebase features through your mobile device settings, such as your device advertising settings or by following the instructions provided by Google in their Privacy Policy: https://policies.google.com/privacy?hl=en\n" +
                "We also encourage you to review the Google's policy for safeguarding your data: https://support.google.com/analytics/answer/6004245. For more information on what type of information Firebase collects, please visit please visit the Google Privacy & Terms web page: https://policies.google.com/privacy?hl=en\n" +
                "Links To Other Sites\n" +
                "Our Service may contain links to other sites that are not operated by us. If you click on a third party link, you will be directed to that third party's site. We strongly advise you to review the Privacy Policy of every site you visit.\n" +
                "We have no control over and assume no responsibility for the content, privacy policies or practices of any third party sites or services.\n" +
                "Children's Privacy\n" +
                "Our Service does not address anyone under the age of 18 (\"Children\").\n" +
                "We do not knowingly collect personally identifiable information from anyone under the age of 18. If you are a parent or guardian and you are aware that your Children has provided us with Personal Data, please contact us. If we become aware that we have collected Personal Data from children without verification of parental consent, we take steps to remove that information from our servers.\n" +
                "Changes To This Privacy Policy\n" +
                "We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page.\n" +
                "We will let you know via email and/or a prominent notice on our Service, prior to the change becoming effective and update the \"effective date\" at the top of this Privacy Policy.\n" +
                "You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.\n" +
                "Contact Us\n" +
                "If you have any questions about this Privacy Policy, please contact us:\n" +
                "●\tBy email: animenetsupport@gmail.com\n" +
                "\n" +
                "\n"
        txtConfirm.setOnClickListener {
            dialog.dismiss()
            val dialogStore = AlertDialog.Builder(this@MainActivity)
            dialogStore.setMessage("We need to access your external storate to save favourite anime, please turn on.")
            dialogStore.setNegativeButton(
                    "OK"
            ) { dialog1, which ->
                dialog1.dismiss()
                isStoragePermissionGranted()
                sharedPreferences.edit().putString("firtstart", "1").apply()
            }
            dialogStore.setCancelable(false)
            dialogStore.show()
        }
        dialog.setCancelable(false)
        dialog.show()
    } else
    {
        isStoragePermissionGranted()
    }

}

fun loadStart() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val database = firebaseDatabase.reference
    database.child("anime3").child("v1").addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                val domain: String = snapshot.child("domain").value.toString()
                val check: String = snapshot.child("check").value.toString()
                val domainNewSeason: String = snapshot.child("domainNewSeason").value.toString()
                val domainMovie: String = snapshot.child("domainMovie").value.toString()
                val domainPopular: String = snapshot.child("domainPopular").value.toString()
                val domainAnimeList: String = snapshot.child("domainAnimeList").value.toString()
                val domainChapter: String = snapshot.child("domainChapter").value.toString()
                val domainPageHome: String = snapshot.child("domainPageHome").value.toString()
                val domainSearch: String = snapshot.child("domainSearch").value.toString()
                val messageDialog: String = snapshot.child("messageDialog").value.toString()
                val domainGenre: String = snapshot.child("domainGenre").value.toString()
                val idFan: String = snapshot.child("idFan").value.toString()
                val namePackage: String = snapshot.child("namePackage").value.toString()
                val imgnamePackage: String = snapshot.child("imgnamePackage").value.toString()

                val sharedPreferences: SharedPreferences = getSharedPreferences(
                        "Confix",
                        MODE_PRIVATE
                )
                sharedPreferences.edit().putString("domain", domain).apply()
                sharedPreferences.edit().putString("check", check).apply()
                sharedPreferences.edit().putString("domainNewSeason", domainNewSeason).apply()
                sharedPreferences.edit().putString("domainMovie", domainMovie).apply()
                sharedPreferences.edit().putString("domainPopular", domainPopular).apply()
                sharedPreferences.edit().putString("domainAnimeList", domainAnimeList).apply()
                sharedPreferences.edit().putString("domainChapter", domainChapter).apply()
                sharedPreferences.edit().putString("domainPageHome", domainPageHome).apply()
                sharedPreferences.edit().putString("domainSearch", domainSearch).apply()
                sharedPreferences.edit().putString("messageDialog", messageDialog).apply()
                sharedPreferences.edit().putString("domainGenre", domainGenre).apply()
                sharedPreferences.edit().putString("idFan", idFan).apply()
                sharedPreferences.edit().putString("namePackage", namePackage).apply()
                sharedPreferences.edit().putString("imgnamePackage", imgnamePackage).apply()

                Log.d("zzz", "onDataChange: " + domain + check)
                if (check.toInt() == 1) {
                    val intent = Intent(this@MainActivity, Activity_Home3012::class.java)
                    startActivity(intent)
                } else if (check.toInt() == 2) {
                    val dialogStart = AlertDialog.Builder(this@MainActivity)
                    dialogStart.setTitle("Notification")
                    dialogStart.setMessage(messageDialog)
                    dialogStart.setNegativeButton(
                            "Cancel"
                    ) { dialog, which -> finish() }
                    dialogStart.setPositiveButton(
                            "OK"
                    ) { dialog, which ->
                        try {
                            startActivity(
                                    Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=" + namePackage)
                                    )
                            )
                        } catch (e: java.lang.Exception) {
                        }
                        finish()
                    }
                    dialogStart.setCancelable(false)
                    dialogStart.show()
                } else if (check.toInt() == 3) {
                    val intent = Intent(this@MainActivity, Activity_Home3012::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Log.d("zzz", "onDataChange: " + e.message)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}

fun isStoragePermissionGranted(): Boolean {
    return if (Build.VERSION.SDK_INT >= 23) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v("zzz", "Permission is granted")
            /////
            //    fmFavorite2311.saveFavorite();
            loadStart()
            true
        } else {
            Log.v("zzz", "Permission is revoked")
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
            )
            false
        }
    } else { //permission is automatically granted on sdk<23 upon installation
        Log.v("zzz", "Permission is granted")
        true
    }
}

override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Log.v("zzz", "Permission: " + permissions[0] + "was " + grantResults[0])
        //resume tasks needing this permission
        //    fmFavorite2311.saveFavorite();
        loadStart()
    } else {
        loadStart()
    }
}
}