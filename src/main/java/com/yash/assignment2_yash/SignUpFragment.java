package com.yash.assignment2_yash;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private TextInputEditText txtName;
    private TextInputEditText txtUsername;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private TextInputEditText txtRepeatPassword;
    private Button btnSignUp;
    private TextView txtVwLogin;

    private MainActivity mainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mainActivity = (MainActivity) getActivity();

        txtName = (TextInputEditText) view.findViewById(R.id.input_name);
        txtUsername = (TextInputEditText) view.findViewById(R.id.input_username);
        txtEmail = (TextInputEditText) view.findViewById(R.id.input_email);
        txtPassword = (TextInputEditText) view.findViewById(R.id.input_password);
        txtRepeatPassword = (TextInputEditText) view.findViewById(R.id.input_repeat_password);
        btnSignUp = (Button) view.findViewById(R.id.btn_signup);
        txtVwLogin = (TextView) view.findViewById(R.id.link_login);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButtonPressed();
            }
        });

        txtVwLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonPressed();
            }
        });

        LinearLayout layoutSignUpContent = (LinearLayout) view.findViewById(R.id.signUpContentView);
        layoutSignUpContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.hideKeyboard(v);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Methods

    public void signUpButtonPressed() {
        Log.d("Sign Up", "Button Pressed!");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mainActivity.hideSoftKeyboard();
        btnSignUp.setEnabled(false);

        insertUser();
    }

    private void insertUser() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_ProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = txtName.getText().toString();
        String username = txtUsername.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String repeatPassword = txtRepeatPassword.getText().toString();

        SharedPreferences.Editor editor = mainActivity.preferences.edit();
        editor.putString("user_name", username);
        editor.commit();

        YVSQLiteHelper sqLiteHelper = mainActivity.sqLiteHelper;
        SQLiteDatabase sqLiteDatabase = null;
        Boolean isSignUpSuccess = false;

        try {

            sqLiteDatabase = sqLiteHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME", name);
            contentValues.put("USERNAME", username);
            contentValues.put("EMAIL", email);
            contentValues.put("PASSWORD", password);
            Log.d("Create user", contentValues.toString());
            sqLiteDatabase.insert("USER", null, contentValues);

            isSignUpSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();
            onSignupFailed();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        final Boolean finalIsSignUpSuccess = isSignUpSuccess;
        new android.os.Handler().postDelayed (
                new Runnable() {
                    public void run() {
                        if (finalIsSignUpSuccess) {
                            onSignupSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 500);
    }

    public void onSignupSuccess() {
        btnSignUp.setEnabled(true);
        if (mListener != null) {
            mListener.onSignUpSuccess();
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "SignUp failed", Toast.LENGTH_LONG).show();
        btnSignUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = txtName.getText().toString();
        String username = txtUsername.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String repeatPassword = txtRepeatPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            txtName.setError("at least 3 characters");
            if (!txtName.isFocused()) {
                txtName.requestFocus();
            }
            valid = false;
        } else {
            txtName.setError(null);

            final String username_pattern = "^[a-z0-9_-]{3,15}$";
            Pattern pattern = Pattern.compile(username_pattern);
            Matcher matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                txtUsername.setError("enter a valid username");
                if (!txtUsername.isFocused()) {
                    txtUsername.requestFocus();
                }
                valid = false;
            } else {
                txtUsername.setError(null);

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("enter a valid email address");
                    if (!txtEmail.isFocused()) {
                        txtEmail.requestFocus();
                    }
                    valid = false;
                } else {
                    txtEmail.setError(null);

                    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                        txtPassword.setError("between 4 and 10 alphanumeric characters");
                        if (!txtPassword.isFocused()) {
                            txtPassword.requestFocus();
                        }
                        valid = false;
                    } else {
                        txtPassword.setError(null);

                        if (!password.equals(repeatPassword)) {
                            txtRepeatPassword.setError("password and repeat password must be same");
                            if (!txtRepeatPassword.isFocused()) {
                                txtRepeatPassword.requestFocus();
                            }
                            valid = false;
                        } else {
                            txtRepeatPassword.setError(null);
                        }
                    }
                }
            }
        }

        return valid;
    }

    public void loginButtonPressed() {
        Log.d("Login", "Button Pressed!");
        if (getView().hasFocus()) {
            mainActivity.hideSoftKeyboard();
        }
        LoginFragment loginFragment = mainActivity.loginFragment;
        mainActivity.fragmentManager.beginTransaction()
                .replace(R.id.drawer_layout, loginFragment, getString(R.string.login_title))
//                .remove(this)
                .commit();
    }
}
