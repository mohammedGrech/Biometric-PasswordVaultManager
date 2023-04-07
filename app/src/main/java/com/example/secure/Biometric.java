package com.example.secure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Biometric {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;

    public boolean authenticate = false;

    // Below line denotes that the annotated element should only be called on the given API level or higher
    @RequiresApi(api = Build.VERSION_CODES.M)
    // This function checks the compatibility of the phone for Biometric authentication
    public boolean checkCompatability (Context context) {

        // BiometricManager provides system information related to biometrics (e.g. fingerprint, face, etc.).
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.BIOMETRIC_SUCCESS)
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context.getApplicationContext(), "Device does not have fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context.getApplicationContext(), "Not working, try again", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context.getApplicationContext(), "No fingerprint enrolled.", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Toast.makeText(context.getApplicationContext(), "Security Vulnerability detected. Security Update required.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    // This function can be called in other activity classes in order to prompt the biometric functionality
    // the Parameter Context is referred to an activity class. i.e. MainActivity
    public void biometricPrompt(Context context){


        //Create executor
        executor = ContextCompat.getMainExecutor(context);

        //Create Biometric prompt and return the result (whether successful or not)
        biometricPrompt=new BiometricPrompt((FragmentActivity)context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context.getApplicationContext(), "Authentication cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context.getApplicationContext(), "Login success.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,HomeActivity.class);
                context.startActivity(intent);
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context.getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Display the popup fingerprint/PIN to the user
        // Note: BIOMETRIC_STRONG is for finger print while BIOMETRIC_WEAK includes Face recognition
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Unlock Secure with fingerprint").setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL).build();
        biometricPrompt.authenticate(promptInfo);
    }


    public void biometricPromptAccessData(Context context){
        //Create executor
        executor = ContextCompat.getMainExecutor(context);

        //Create Biometric prompt and return the result (whether successful or not)
        biometricPrompt=new BiometricPrompt((FragmentActivity)context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context.getApplicationContext(), "Authentication cancelled", Toast.LENGTH_SHORT).show();
                authenticate = false;
                Intent intent = new Intent(context,HomeActivity.class);
                context.startActivity(intent);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context.getApplicationContext(), "Access granted", Toast.LENGTH_SHORT).show();
                authenticate = true;

                UpdateRecord updateRecord = new UpdateRecord();
                updateRecord.result(authenticate);

            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context.getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                authenticate = false;
                Intent intent = new Intent(context,HomeActivity.class);
                context.startActivity(intent);
            }
        });

        // Display the popup fingerprint/PIN to the user
        // Note: BIOMETRIC_STRONG is for finger print while BIOMETRIC_WEAK includes Face recognition
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Authentication required to access data").setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL).build();
        biometricPrompt.authenticate(promptInfo);
    }

}