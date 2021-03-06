package com.example.thai.dotify.Layouts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thai.dotify.Fragments.ForgetPasswordFragment;
import com.example.thai.dotify.R;

import java.util.ArrayList;
import java.util.List;


/**
 * the ResetPasswordSecurityQuestionLayout object represents data for security questions and answers
 */
public class ResetPasswordSecurityQuestionLayout extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView securityQuestion1, securityQuestion2;

    /**
     * default constructor
     */
    public ResetPasswordSecurityQuestionLayout() {
        // Required empty public constructor
    }

    /**
     * creates the View object to display the ResetPasswordSecurityQuestionLayout object
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forget_password_security_question_layout, container, false);
        securityQuestion1 = (TextView) view.findViewById(R.id.forget_password_security_question_1_text_view);
        securityQuestion2 = (TextView) view.findViewById(R.id.forget_password_security_question_2_text_view);
        return view;
    }

    /**
     * inflate the object by getting the security questions the user answered
     * @param context
     * @param attrs
     * @param savedInstanceState
     */
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        //Set the security questions to the first and second question respetively
        securityQuestion1.setText(ForgetPasswordFragment.getListOfSecQuestions().get(0));
        securityQuestion2.setText(ForgetPasswordFragment.getListOfSecQuestions().get(1));
}


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
