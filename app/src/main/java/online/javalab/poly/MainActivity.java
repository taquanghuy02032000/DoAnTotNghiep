package online.javalab.poly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import online.javalab.poly.activitys.LoginActivity;
import online.javalab.poly.activitys.SettingActivity;
import online.javalab.poly.adapters.ViewPagerAdapter;
import online.javalab.poly.fragments.HomeFragment;
import online.javalab.poly.fragments.ProfileFragment;
import online.javalab.poly.fragments.ProgramFragment;
import online.javalab.poly.fragments.RankFragment;
import online.javalab.poly.interfaces.SocketAnnotate;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.utils.DialogUtil;
import online.javalab.poly.utils.ImageUtils;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private NavigationView navigationView;
    private CircleImageView mHeaderMenuAvtCricleImageView;
    private TextView mHeaderMenuEmailText;
    private TextView mHeaderMenuNameText;
    private GoogleApiClient mGoogleApiClient;

    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initView();
        setUpViewPager();
        loadDataProfile();
        DialogUtil.init(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mSocket();


        mHeaderMenuEmailText = findViewById(R.id.header_menu_email_txt);
        mHeaderMenuNameText = findViewById(R.id.header_menu_name_txt);
        mHeaderMenuAvtCricleImageView = findViewById(R.id.header_menu_avt_circleimageview);

        navigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.main_drawer_nav);
        mBottomNavigationView = findViewById(R.id.main_bottom_navigation);
        mViewPager = findViewById(R.id.main_view_pager);
//        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.material_500));
        mBottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        mViewPager.setOffscreenPageLimit(5);

        navigationView.findViewById(R.id.nav_header).findViewById(R.id.header_menu_avt_circleimageview).setOnClickListener(this);
        navigationView.findViewById(R.id.nav_main).findViewById(R.id.drawer_navigation_setting_text).setOnClickListener(this);
        navigationView.findViewById(R.id.nav_main).findViewById(R.id.drawer_navigation_rateus_text).setOnClickListener(this);
        navigationView.findViewById(R.id.nav_main).findViewById(R.id.drawer_navigation_contact_text).setOnClickListener(this);
        navigationView.findViewById(R.id.nav_main).findViewById(R.id.drawer_navigation_user_text).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_program:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.nav_rank:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.nav_profile:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
        return true;
    }

    private void setUpViewPager() {
        mViewPagerAdapter.addFragment(new HomeFragment(), getString(R.string.fg_home));
        mViewPagerAdapter.addFragment(new ProgramFragment(), getString(R.string.fg_program));
        mViewPagerAdapter.addFragment(RankFragment.newInstance(), getString(R.string.fg_rank));
        mViewPagerAdapter.addFragment(new ProfileFragment(), getString(R.string.fg_profile));
        mViewPager.setAdapter(mViewPagerAdapter);
        getSupportActionBar().setTitle(R.string.fg_home);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        getSupportActionBar().setTitle(R.string.fg_home);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_program).setChecked(true);
                        getSupportActionBar().setTitle(R.string.fg_program);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_rank).setChecked(true);
                        getSupportActionBar().setTitle(R.string.fg_rank);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        getSupportActionBar().setTitle(R.string.fg_profile);
                        break;
                }


            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_navigation_setting_text:
                startActivity(new Intent(this, SettingActivity.class));

                break;
            case R.id.drawer_navigation_rateus_text:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_rateus, viewGroup, false);
                TextView rateText = dialogView.findViewById(R.id.dialog_rateus_rate_text);
                TextView cancelText = dialogView.findViewById(R.id.dialog_rateus_cancel_text);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                cancelText.setOnClickListener(view1 -> alertDialog.dismiss());
                rateText.setOnClickListener(v -> {
                    String uri = ("https://play.google.com/store/apps/details?id=com.miHoYo.GenshinImpact&fbclid=IwAR1bsO66WcKA3B92dQAK1zH9LlbTFStNO4qiL1qxuMAefvU1rghcp8t7IPI");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                    alertDialog.dismiss();
                });
                break;
            case R.id.drawer_navigation_contact_text:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "0963094221"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.drawer_navigation_user_text:
                logOut();
                break;
            case R.id.header_menu_avt_circleimageview:
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }


    private void loadDataProfile() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        DataStorageManager.setUserName(signInAccount.getDisplayName());
        if (signInAccount != null) {
            mHeaderMenuNameText.setText(signInAccount.getDisplayName());
            mHeaderMenuEmailText.setText(signInAccount.getEmail());
            ImageUtils.loadImage(mHeaderMenuAvtCricleImageView,
                    Objects.requireNonNull(signInAccount.getPhotoUrl()).toString());

        }


    }

    private void logOut() {
        if (!DeviceUtil.hasConnection(this)) {
            CommonExt.toast(this, Define.Message.NO_CONNECT);
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.logout);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            FirebaseAuth.getInstance().signOut();
            DataStorageManager.removeFromKey(Define.StorageKey.USER_ID);
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    status -> {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
            );
        });
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onStop() {
        super.onStop();
        mSocket.emit(SocketAnnotate.userOut, DataStorageManager.getStringValue(Define.StorageKey.USER_ID));

    }

    private Socket mSocket() {

        try {

            mSocket = IO.socket(SocketAnnotate.URI_SOCKET);
            //  mSocket.on(SocketAnnotate.out, userOut);
            mSocket.on(SocketAnnotate.out, userOut);
        } catch (URISyntaxException e) {

        }
        mSocket.connect();
        return mSocket;
    }


    private Emitter.Listener userOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            String message = data.optString("data");

        }
    };
}
