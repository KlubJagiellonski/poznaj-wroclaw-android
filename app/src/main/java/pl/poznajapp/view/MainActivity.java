package pl.poznajapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import pl.poznajapp.API.APIService;
import pl.poznajapp.BuildConfig;
import pl.poznajapp.PoznajApp;
import pl.poznajapp.R;
import pl.poznajapp.adapter.StoryListAdapter;
import pl.poznajapp.helpers.FacebookPageUrl;
import pl.poznajapp.helpers.Utils;
import pl.poznajapp.listeners.RecyclerViewItemClickListener;
import pl.poznajapp.model.Story;
import pl.poznajapp.service.LocationService;
import pl.poznajapp.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Rafał Gawlik on 13.08.17.
 */

public class MainActivity extends BaseAppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LocationService locationService = null;
    private boolean bound = false;
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            locationService.requestLocationUpdates();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            bound = false;
        }
    };
    private LocationReceiver locationReceiver;
    private GoogleApiClient googleApiClient;
    private APIService service;
    private List<Story> stories;
    private StoryListAdapter adapter;
    private RecyclerView storyListRV;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationReceiver = new LocationReceiver();
        setContentView(R.layout.activity_main);

        stories = new ArrayList<Story>();
        setupView();
        initListeners();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            checkLocationEnabled();
        }
    }
<<<<<<< HEAD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                intent = new Intent(this, ActivityWebView.class);
                intent.putExtra("url", Utils.INSTANCE.getURL_POZNAJAPP_ABOUT());
                startActivity(intent);
                return true;
            case R.id.action_club:
                intent = new Intent(this, ActivityWebView.class);
                intent.putExtra("url", Utils.INSTANCE.getURL_POZNAJAPP_KJ());
                startActivity(intent);
                return true;
            case R.id.action_team:
                intent = new Intent(this, ActivityWebView.class);
                intent.putExtra("url", Utils.INSTANCE.getURL_POZNAJAPP_TEAM());
                startActivity(intent);
                return true;
            case R.id.action_partners:
                intent = new Intent(this, ActivityWebView.class);
                intent.putExtra("url", Utils.INSTANCE.getURL_POZNAJAPP_PARTNERS());
                startActivity(intent);
                return true;
            case R.id.action_bug:
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Utils.INSTANCE.getPOZNAJAPP_MAIL()}); // do kogo
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.bug_mail_title)); // tytuł maila
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "APP_VERSION_NAME: "+ BuildConfig.VERSION_NAME + " | ANDROID_VERSION: "+ Build.VERSION.RELEASE + " | DEVICE_MODEL: " +  android.os.Build.MODEL + " | "+ getString(R.string.bug_mail_text) ); // tresc maila
                startActivity(intent);
                return true;

            case R.id.action_rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;
            case R.id.action_fb:
                String facebookPageURL = new FacebookPageUrl().getFacebookPageURL(this, Utils.INSTANCE.getURL_POZNAJAPP_FB(), Utils.INSTANCE.getURL_POZNAJAPP_FB_PAGENAME());
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageURL));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
=======
>>>>>>> parent of 7e0bfe6... add menu

    private void setupView() {
        storyListRV = (RecyclerView) findViewById(R.id.activity_main_story_list_rv);
        adapter = new StoryListAdapter(getApplicationContext(), stories);
        storyListRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        storyListRV.setAdapter(adapter);
        storyListRV.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListeners() {
        storyListRV.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                storyListRV, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(StoryDetailsActivity.getConfigureIntent(getApplicationContext(), stories.get(position).getId()));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void loadStories(Location location) {
        service = PoznajApp.retrofit.create(APIService.class);
        showProgressDialog("Trasy", "Pobieraniem tras");
        Call<List<Story>> storyListCall = service.listStories(location.getLatitude(), location.getLongitude());
        storyListCall.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                Timber.d(response.message());
                stories.clear();
                stories.addAll(response.body());
                adapter.notifyDataSetChanged();
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Timber.e(t);
                hideProgressDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(locationReceiver,
                new IntentFilter(LocationService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Timber.i("onStop");
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
        super.onStop();
    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Timber.d("Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Timber.d("Requesting permission");

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void checkLocationEnabled() {
        //location settings
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).build();

        builder.setAlwaysShow(true);

        googleApiClient.connect();
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                Timber.d(status.toString());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Timber.d("LocationSettingsStatusCodes.SUCCESS");

                        bindService(new Intent(getApplicationContext(), LocationService.class), serviceConnection,
                                Context.BIND_AUTO_CREATE);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Timber.d("LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (@SuppressLint("NewApi") IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Timber.d("LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
                        //TODO show toast - location turn off
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Timber.d("onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Timber.d("User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnabled();
            } else {
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        bindService(new Intent(getApplicationContext(), LocationService.class), serviceConnection,
                                Context.BIND_AUTO_CREATE);
                        break;
                    case Activity.RESULT_CANCELED:
                        //TODO show toast - location turn off
                        break;
                }
                break;
        }
    }

    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationService.EXTRA_LOCATION);
            if (location != null) {
                Timber.d(Utils.INSTANCE.getLocationText(location));
                loadStories(location);
            }
        }
    }


}