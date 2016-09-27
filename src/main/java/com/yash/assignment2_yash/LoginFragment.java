package com.yash.assignment2_yash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
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
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private TextInputEditText txtUsername;
    private TextInputEditText txtPassword;
    private Button btnLogin;
    private TextView txtVwSignUp;

    private MainActivity mainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        txtUsername = (TextInputEditText) view.findViewById(R.id.input_username);
        txtPassword = (TextInputEditText) view.findViewById(R.id.input_password);
        btnLogin    = (Button) view.findViewById(R.id.btn_login);
        txtVwSignUp = (TextView) view.findViewById(R.id.link_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonPressed();
            }
        });
        txtVwSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButtonPressed();
            }
        });

        LinearLayout layoutLoginContent = (LinearLayout) view.findViewById(R.id.loginContentView);
        layoutLoginContent.setOnClickListener(new View.OnClickListener() {
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

    public void loginButtonPressed() {
        Log.d("Login", "Button Pressed!");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mainActivity.hideSoftKeyboard();
        btnLogin.setEnabled(false);

        checkUserAuthentication();
    }

    private void checkUserAuthentication() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_ProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        YVSQLiteHelper sqLiteHelper = mainActivity.sqLiteHelper;
        SQLiteDatabase sqLiteDatabase = null;
        Boolean isLoginSuccess = false;

        try {
            sqLiteDatabase = sqLiteHelper.getReadableDatabase();
            Cursor cursor = null;
            try {
                String sql = "SELECT _id FROM USER WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
                Log.d("User authentication sql", sql);
                cursor = sqLiteDatabase.rawQuery(sql, null);
                if (cursor.getCount() != 0) {
                    isLoginSuccess = true;
                } else {
                    new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme_AlertDialog))
                            .setTitle("Error")
                            .setMessage("Username or Password is not correct.\nPlease check those and try again.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    onLoginFailed();
                }
            } catch (Exception e) {
                e.printStackTrace();
                onLoginFailed();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFailed();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

        final Boolean finalIsLoginSuccess = isLoginSuccess;
        new android.os.Handler().postDelayed (
                new Runnable() {
                    public void run() {
                        if (finalIsLoginSuccess) {
                            onLoginSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 500);
    }

    public void onLoginSuccess() {

        btnLogin.setEnabled(true);

        if (mListener != null) {

            SharedPreferences.Editor editor = mainActivity.preferences.edit();
            editor.putString("user_name", txtUsername.getText().toString());
            editor.commit();

            txtUsername.getText().clear();
            txtPassword.getText().clear();

            mListener.onLoginSuccess();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if (username.isEmpty()) {
            txtUsername.setError("enter a username");
            if (!txtUsername.isFocused()) {
                txtUsername.requestFocus();
            }
            valid = false;
        } else {
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

                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    txtPassword.setError("between 4 and 10 alphanumeric characters");
                    if (!txtPassword.isFocused()) {
                        txtPassword.requestFocus();
                    }
                    valid = false;
                } else {
                    txtPassword.setError(null);
                }
            }
        }

        return valid;
    }

    public void signUpButtonPressed() {
        Log.d("Sign Up", "Button Pressed!");
        SignUpFragment signUpFragment = new SignUpFragment();
        mainActivity.fragmentManager.beginTransaction()
                .add(R.id.drawer_layout, signUpFragment, getString(R.string.signup_title))
                .addToBackStack(getString(R.string.signup_title))
                .commit();
    }
}
