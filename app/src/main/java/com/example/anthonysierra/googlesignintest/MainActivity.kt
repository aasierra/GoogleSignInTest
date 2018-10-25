package com.example.anthonysierra.googlesignintest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("587450322170-d3b1ptoj06ku6pocqs9iubummn83mrtr.apps.googleusercontent.com").requestEmail().build()
        val signInClient = GoogleSignIn.getClient(this, gso)
        signInButton.setOnClickListener {
            val intent = signInClient.signInIntent
            startActivityForResult(intent, 1)
        }
        GoogleSignIn.getLastSignedInAccount(this)?.let{
            updateAccount(it)
        }
        getMorePermissions.setOnClickListener {
            GoogleSignIn.requestPermissions(this, 2, GoogleSignIn.getLastSignedInAccount(this), Scope("drive.appfolder"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(MainActivity::class.java.simpleName, requestCode.toString())
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                updateAccount(account)
            } catch (e:ApiException) {
                Log.e(MainActivity::class.java.simpleName, e.message)
                updateText.text = "Something went wrong " + e.message
            }
        } else if (requestCode == 2 && resultCode ==  Activity.RESULT_OK) {
            Toast.makeText(this, "Got Permissions for google drive!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateAccount(account:GoogleSignInAccount) {
        val sb = StringBuilder()
        sb.appendln("Display Name : " + account.displayName)
        sb.appendln("Email " + account.email)
        sb.appendln("Family Name : " + account.familyName)
        sb.appendln("Given Name : " + account.givenName)
        sb.appendln("ID : " + account.id)
        sb.appendln("ID Token : " + account.idToken)
        sb.appendln("Is Expired : " + account.isExpired)
        sb.appendln("ObfuscatedIdentifier : " + account.obfuscatedIdentifier)
        sb.appendln("Photo Url : " + account.photoUrl)
        sb.appendln("Server Auth Code : " + account.serverAuthCode)
        sb.appendln("Expiration Time Seconds : " +account.expirationTimeSecs)
        sb.appendln("Requested Scopes : " + account.requestedScopes)
        sb.appendln("Graned Scopes : " + account.grantedScopes)
        updateText.text = sb.toString()
    }
}
