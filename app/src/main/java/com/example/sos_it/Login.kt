package com.example.sos_it

import com.google.firebase.auth.*


lateinit var auth: FirebaseAuth
lateinit var storedVerificationId:String
lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
lateinit var number: String
lateinit var name:String

