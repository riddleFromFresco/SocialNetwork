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


public class SignUpMenu extends AppCompatActivity {
    EditText emailField;
    EditText passwordField;
    EditText repeatedPasswordField;
    Button signUpBtn;

    MessageViewer messageViewer;
    AccountsFirebase firebaseAccountsData;


    void setFieldsValues() {
        setViewFields();
        setObjectFields();
    }

    void setViewFields() {
        emailField = findViewById(R.id.emailTextField2);
        passwordField = findViewById(R.id.passwordTextField2);
        repeatedPasswordField = findViewById(R.id.repeatPasswordTextField);
        signUpBtn = findViewById(R.id.signUpBtn2);
    }

    void setObjectFields() {
        messageViewer = new MessageViewer();
        firebaseAccountsData = new AccountsFirebase();
    }

    boolean isAccountDataValid(String email, String password, String repeatedPassword) {
        if (!Validators.isValidEmail(email)) {
            messageViewer.showMessage(SystemMessages.INCORRECT_EMAIL, getApplicationContext());
            return false;
        }

        if (!Validators.isValidPassword(password) || !Validators.isValidPassword(repeatedPassword))
        {
            messageViewer.showMessage(SystemMessages.INCORRECT_PASSWORD, getApplicationContext());
            return false;
        }

        if (!password.equals(repeatedPassword)) {
            messageViewer.showMessage(SystemMessages.MISMATCHED_PASSWORDS, getApplicationContext());
            return false;
        }

        return true;
    }

    public void accountAlreadyExistsMessage() {
        messageViewer.showMessage(SystemMessages.NICKNAME_IS_ALREADY_IN_USE, getApplicationContext());
    }

    public void createUserWithValidation(String email, String password) {
        createUser(email, password);
        firebaseAccountsData.checkIfAccountAlreadyExist(email, password, this, AccountsFirebase.CHECK_IF_ACCOUNT_CREATED);
    }

    void createUser(String username, String password) {
        firebaseAccountsData.createNewAccount(username, password);
    }

    public void openChatsMenu(String email) {
        SocialNetworkApplication application = (SocialNetworkApplication) this.getApplication();
        application.setCurrentUsername(email);

        startActivity(new Intent(SignUpMenu.this, ChatsListMenu.class));
    }

    public void unableToCreateAccountMessage() {
        messageViewer.showMessage(SystemMessages.UNABLE_TO_CREATE_ACCOUNT, getApplicationContext());
    }

    void signUpBtnPressed(String email, String password, String repeatedPassword) {
        if (!isAccountDataValid(email, password, repeatedPassword))
            return;

        firebaseAccountsData.checkIfAccountAlreadyExist(email, password, this, AccountsFirebase.CHECK_IF_USERNAME_ALREADY_IN_USE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        setFieldsValues();

        signUpBtn.setOnClickListener(view -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String repeatedPassword = repeatedPasswordField.getText().toString();

            signUpBtnPressed(email, password, repeatedPassword);
        });
    }
}

