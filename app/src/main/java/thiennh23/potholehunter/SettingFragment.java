package thiennh23.potholehunter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Handle Back Button Click
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        // Setting unique text and icons for each option
        setupEditProfileOption(view);
        setupChangePasswordOption(view);
        //setupNotificationsOption(view);
        setupFeedbackOption(view);
        setupLanguagesOption(view);
        setupLogoutOption(view);

        return view;
    }

    private void setupEditProfileOption(View view) {
        LinearLayout editProfileOption = view.findViewById(R.id.edit_profile_option);
        ((TextView) editProfileOption.findViewById(R.id.option_text)).setText("Edit Profile");
        ((ImageView) editProfileOption.findViewById(R.id.option_icon)).setImageResource(R.drawable.user);

        editProfileOption.setOnClickListener(v -> {
            // Handle Edit Profile click (e.g., navigate to EditProfileActivity)
            // Example: openEditProfile();
        });
    }

    private void setupChangePasswordOption(View view) {
        LinearLayout changePasswordOption = view.findViewById(R.id.change_password_option);
        ((TextView) changePasswordOption.findViewById(R.id.option_text)).setText("Change Password");
        ((ImageView) changePasswordOption.findViewById(R.id.option_icon)).setImageResource(R.drawable.ic_changepwd);

        changePasswordOption.setOnClickListener(v -> {
            // Handle Change Password click (e.g., navigate to ChangePasswordActivity)
            // Example: openChangePassword();
        });
    }

    private void setupFeedbackOption(View view) {
        LinearLayout feedbackOption = view.findViewById(R.id.feedbacks_option);
        ((TextView) feedbackOption.findViewById(R.id.option_text)).setText("Feedback");
        ((ImageView) feedbackOption.findViewById(R.id.option_icon)).setImageResource(R.drawable.feedback);

        feedbackOption.setOnClickListener(v -> {
            // Handle Feedback click (e.g., navigate to FeedbackActivity)
            // Example: openFeedback();
        });
    }

    private void setupLanguagesOption(View view) {
        LinearLayout languagesOption = view.findViewById(R.id.languages_option);
        ((TextView) languagesOption.findViewById(R.id.option_text)).setText("Languages");
        ((ImageView) languagesOption.findViewById(R.id.option_icon)).setImageResource(R.drawable.language);

        languagesOption.setOnClickListener(v -> {
            // Handle Languages click (e.g., navigate to LanguageSettingsActivity)
            // Example: openLanguages();
        });
    }

    private void setupLogoutOption(View view) {
        LinearLayout logoutOption = view.findViewById(R.id.logout_option);
        ((TextView) logoutOption.findViewById(R.id.option_text)).setText("Logout");
        ((ImageView) logoutOption.findViewById(R.id.option_icon)).setImageResource(R.drawable.logout);

        logoutOption.setOnClickListener(v -> {
            // Handle Logout click (e.g., log out the user)
            // Example: logoutUser();
        });
    }

}
