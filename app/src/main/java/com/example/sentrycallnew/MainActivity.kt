package com.example.sentrycallnew
import android.content.ComponentName
import android.content.Intent
import android.provider.Settings
import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CONTACT_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🌟 ROOT LAYOUT
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.WHITE)

        // 🔷 TOP BAR
        val topBar = TextView(this)
        topBar.text = "Smart DND Enforcer"
        topBar.setBackgroundColor(Color.parseColor("#3F51B5"))
        topBar.setTextColor(Color.WHITE)
        topBar.textSize = 20f
        topBar.setPadding(30, 60, 30, 30)

        // 📄 STATUS TEXT
        val content = TextView(this)
        content.text = "Status: Active"
        content.textSize = 18f
        content.setTextColor(Color.BLACK)
        content.setPadding(30, 50, 30, 20)

        // 🔘 COMMON BUTTON PARAMS
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(30, 20, 30, 0)

        // ☁️ SYNC BUTTON
        val syncButton = android.widget.Button(this)
        syncButton.text = "Sync to Cloud ☁️"
        syncButton.setTextColor(Color.WHITE)
        syncButton.background = ContextCompat.getDrawable(this, R.drawable.rounded_green)
        syncButton.layoutParams = params

        syncButton.setOnClickListener {
            syncContactsToCloud()
        }

        // 📘 INSTRUCTIONS BUTTON
        val instructionButton = android.widget.Button(this)
        instructionButton.text = "How to Use 📘"
        instructionButton.setTextColor(Color.WHITE)
        instructionButton.background = ContextCompat.getDrawable(this, R.drawable.rounded_blue)
        instructionButton.layoutParams = params

        instructionButton.setOnClickListener {
            showInstructionsDialog()
        }

        // 🔐 PRIVACY BUTTON
        val privacyButton = android.widget.Button(this)
        privacyButton.text = "Privacy & Security 🔐"
        privacyButton.setTextColor(Color.WHITE)
        privacyButton.background = ContextCompat.getDrawable(this, R.drawable.rounded_purple)
        privacyButton.layoutParams = params

        privacyButton.setOnClickListener {
            showPrivacyDialog()
        }

        // ➕ ADD VIEWS (CORRECT ORDER)
        root.addView(topBar)
        root.addView(content)
        root.addView(syncButton)
        root.addView(instructionButton)
        root.addView(privacyButton)

        setContentView(root)
        val cn = ComponentName(this, NotificationService::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")

        if (flat == null || !flat.contains(packageName)) {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
        checkPermission()
    }
    private fun showInstructionsDialog() {

        val message = """
1. Enable Notification Access:
   Settings → Apps → Special Access → Notification Access → Enable this app

2. Enable Do Not Disturb (DND):
   Settings → Sound → Do Not Disturb → Turn ON

3. Add Important Contacts:
   Mark contacts as ⭐ Starred in your phone

4. Sync Contacts:
   Click "Sync to Cloud ☁️"

5. Test:
   Ask a starred contact to call via WhatsApp during DND
    """.trimIndent()

        android.app.AlertDialog.Builder(this)
            .setTitle("How to Use")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    private fun showPrivacyDialog() {

        val message = """
Privacy Disclosure

1. Data Collection:
This app accesses starred contacts and notifications only to detect important calls.

2. Security:
Contact names are NOT stored in plain text.
They are converted into SHA-256 hashes before storage.

3. Data Usage:
We do NOT read messages.
Only caller name from WhatsApp notifications is used.

4. Third-Party:
This app is an academic prototype.
Not affiliated with WhatsApp or Google.

5. User Control:
You can disable notification access anytime.
    """.trimIndent()

        android.app.AlertDialog.Builder(this)
            .setTitle("Privacy & Security")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACT_PERMISSION_CODE
            )
        } else {
            fetchStarredContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CONTACT_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchStarredContacts()
        }
    }

    private fun fetchStarredContacts() {

        val dbHelper = DBHelper(this)
        val uniqueSet = mutableSetOf<String>()

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            "${ContactsContract.Contacts.STARRED} = 1",
            null,
            null
        )

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {

                val name = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )

                if (name != null) {
                    val cleanName = name.trim().lowercase()

                    if (!uniqueSet.contains(cleanName)) {
                        uniqueSet.add(cleanName)
                        dbHelper.insertContact(cleanName, "unknown")
                    }
                }
            }
            cursor.close()
        }

        val storedContacts = dbHelper.getAllContacts()
        Log.d("DB_DEBUG", "Stored Contacts in SQLite: $storedContacts")
    }
    private fun syncContactsToCloud() {

        try {
            val dbHelper = DBHelper(this)
            val contacts = dbHelper.getAllContacts()

            Log.d("DB_DEBUG", "Contacts count: ${contacts.size}")

            for (name in contacts) {

                Log.d("API_DEBUG", "Sending contact: $name")

                val hashedName = HashUtil.getSecureHash(name)

                val contact = Contact(
                    userId = "user1",
                    contactName = hashedName,
                    phone = "unknown"
                )

                try {
                    RetrofitClient.api.sendContact(contact)
                        .enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {

                            override fun onResponse(
                                call: retrofit2.Call<okhttp3.ResponseBody>,
                                response: retrofit2.Response<okhttp3.ResponseBody>
                            ) {
                                Log.d("CLOUD", "Response: ${response.code()}")

                                if (response.isSuccessful) {
                                    Log.d("CLOUD", "Uploaded: $name")
                                } else {
                                    Log.e("CLOUD", "Failed: $name")
                                }
                            }

                            override fun onFailure(
                                call: retrofit2.Call<okhttp3.ResponseBody>,
                                t: Throwable
                            ) {
                                Log.e("CLOUD_ERROR", "Error: ${t.message}")
                            }
                        })

                } catch (e: Exception) {
                    Log.e("CLOUD_CRASH", "Crash: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e("CLOUD_FATAL", "Fatal crash: ${e.message}")
        }
    }
}