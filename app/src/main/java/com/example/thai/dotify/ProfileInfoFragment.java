package com.example.thai.dotify;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class ProfileInfoFragment extends Fragment implements View.OnClickListener{

    private Context activityContext;
    private TextView usernameEditText;
    private LoginFragment loginFragment;

    public interface OnChangeFragmentListener {
        void buttonClicked(StartUpContainer.AuthFragmentType fragmentType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        loginFragment = new LoginFragment();

        //TextViews
        TextView usernameEditText = view.findViewById(R.id.usernameDisplay);

        //Buttons
        Button logoutButton = view.findViewById(R.id.logoutButton);

        //On Click Listener for Buttons
        logoutButton.setOnClickListener(this);

        //Set the username to their username
        SharedPreferences userData = activityContext.getSharedPreferences("UserData", MODE_PRIVATE);
        String username1 = userData.getString("username", null);
        if (username1 != null) {
            usernameEditText.setText(username1);
        }
        else{
            usernameEditText.setText("Not Found");
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityContext = context;
    }

    /**
     * If the user clicks the log out button
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                SharedPreferences userData = activityContext.getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = userData.edit();
                editor.clear();
                editor.apply();
                //Send the User back to the login screen
                Intent signoutIntent = new Intent(getActivity(), StartUpContainer.class);
                startActivity(signoutIntent);
                getActivity().finish();
                break;
        }
    }


}