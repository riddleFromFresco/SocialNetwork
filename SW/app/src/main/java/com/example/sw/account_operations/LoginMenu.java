package com.example.sw.account_operations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sw.R;
import com.example.sw.application.SocialNetworkApplication;
import com.example.sw.chat_list.ChatsListMenu;
import com.example.sw.model.firebase_connectors.AccountsFirebase;
import com.example.sw.system_messages.MessageViewer;
import com.example.sw.system_messages.SystemMessages;


public class LoginMenu extends AppCompatActivity {
    EditText emailField;
    EditText passwordField;
    Button signInBtn;
    Button signUpBtn;

    Validators validators;
    MessageViewer messageViewer;

    AccountsFirebase accountsFirebase;


    void setFieldsValues() {
        setViewFields();
        setObjectFields();
    }

    void setViewFields() {
        emailField = findViewById(R.id.emailTextField);
        passwordField = findViewById(R.id.passwordTextField);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
    }

    void setObjectFields() {
        messageViewer = new MessageViewer();
        validators = new Validators();
        accountsFirebase = new AccountsFirebase();
    }

    void signInBtnClicked() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if (!isEnteredDataValid(email, password))
            return;

        accountsFirebase.getPasswordOfUser(email, password, this);
    }

    void openSignUpPage() {
        startActivity(new Intent(LoginMenu.this, SignUpMenu.class));
    }

    boolean isEnteredDataValid(String email, String password) {
        if (!validators.isValidEmail(email)) {
            messageViewer.showMessage(SystemMessages.INCORRECT_EMAIL, getApplicationContext());
            return false;
        }
        if (!validators.isValidPassword(password)) {
            messageViewer.showMessage(SystemMessages.INCORRECT_PASSWORD, getApplicationContext());
            return false;
        }

        return true;
    }

    public void unableToLogin() {
        messageViewer.showMessage(SystemMessages.USER_DOES_NOT_EXIST, getApplicationContext());
    }

    public void openChatsMenu(String username, String enteredPassword, String realPassword) {
        SocialNetworkApplication application = ((SocialNetworkApplication) this.getApplication());
        application.setCurrentUsername(username);

        startActivity(new Intent(LoginMenu.this, ChatsListMenu.class));
    }

    public void wrongPassword() {
        messageViewer.showMessage(SystemMessages.WRONG_PASSWORD, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        setFieldsValues();

        signInBtn.setOnClickListener(view -> signInBtnClicked());
        signUpBtn.setOnClickListener(view -> openSignUpPage());
    }
}