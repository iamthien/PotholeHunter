package thiennh23.potholehunter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import thiennh23.potholehunter.HomeFragment;
import thiennh23.potholehunter.FeedFragment;
import thiennh23.potholehunter.MapFragment;
import thiennh23.potholehunter  .UserFragment;


public class HomeActivity extends AppCompatActivity {

    private FragmentManager fm;
    private AnimatedBottomBar navbar;
    private Fragment homeFrag, mapsFrag, feedFrag, userFrag, fragment;

    //Handler -> utils
    public static Handler delayHandler;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // Handle notification permission granted or denied
            });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        InitVars();
        InitViews();
        InitEvents();

        // Ask for notification permission if needed
        //askNotificationPermission();

        fm.beginTransaction()
                .add(R.id.fragment_container, fragment = homeFrag, "0")
                .commit();

        navbar.selectTabAt(0, false);

        // New back button handler system
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int selectedIndex = navbar.getSelectedIndex();
                if (selectedIndex == 0) {
                    finish();
                } else {
                    navbar.selectTabAt(0, true);
                }
            }
        });
    }

    private void InitVars() {
        fm = getSupportFragmentManager();

        // Initialize Fragments
        homeFrag = new HomeFragment();
        feedFrag = new FeedFragment();
        mapsFrag = new MapFragment();
        userFrag = new UserFragment();
        delayHandler = new Handler();


        // Delay handler, if necessary (implement this in the Util class)
        //Util.delayHandler = new Handler();
    }

    private void InitViews() {
        navbar = findViewById(R.id.bottom_bar);
    }

    private void InitEvents() {
        navbar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                fm.beginTransaction().hide(fragment).commit();

                String tag = String.valueOf(newIndex);

                // Switch between fragments based on tab selection
                switch (newIndex) {
                    case 0:
                        fragment = homeFrag;
                        break;
                    case 1:
                        fragment = feedFrag;
                        break;
                    case 2:
                        fragment = mapsFrag;
                        break;
                    case 3:
                        fragment = userFrag;
                        break;
                }

                // Add or show the selected fragment
                if (fm.findFragmentByTag(tag) == null) {
                    fm.beginTransaction().add(R.id.fragment_container, fragment, tag).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                } else {
                    fm.beginTransaction().show(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                // Handle tab reselection if necessary
            }
        });
    }

//    private void askNotificationPermission() {
//        // This is only necessary for API level >= 33 (TIRAMISU)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                    // Directly ask for the permission
//                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//                }
//            }
//        }
//    }
}