package com.andremion.heroes.sync.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.andremion.heroes.R;

public class SyncHelper {

    private static final String LOG_TAG = SyncHelper.class.getSimpleName();
    // Interval at which to sync, in seconds.
    private static final int SYNC_INTERVAL = (int) (DateUtils.WEEK_IN_MILLIS / 1000);
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private SyncHelper() {
    }

    public static void initializeSync(Context context) {
        getSyncAccount(context);
        Log.v(LOG_TAG, "Syncing initialized");
    }

    /**
     * Get a dummy account for the sync adapter
     *
     * @param context The application context
     */
    private static Account getSyncAccount(Context context) {

        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            onAccountCreated(context, newAccount);
        }

        return newAccount;
    }

    private static void onAccountCreated(Context context, Account newAccount) {
        final String authority = context.getString(R.string.data_provider_authority);
        /*
         * Inform the system that this account supports sync
         */
        ContentResolver.setIsSyncable(newAccount, authority, 1);
        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, authority, true);
        /*
         * Since we've created an account
         */
        configurePeriodicSync(context, newAccount);
        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, Account account) {
        final String authority = context.getString(R.string.data_provider_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().setExtras(Bundle.EMPTY).
                    syncPeriodic(SYNC_INTERVAL, SYNC_FLEXTIME).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, SYNC_INTERVAL);
        }
        Log.v(LOG_TAG, "Periodic sync configured with " + SYNC_INTERVAL + " interval and " + SYNC_FLEXTIME + " flextime");
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.data_provider_authority), bundle);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     * @param extras  The Bundle of extra values
     */
    public static void syncImmediately(Context context, Bundle extras) {
        Bundle bundle = new Bundle(extras);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.data_provider_authority), bundle);
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
