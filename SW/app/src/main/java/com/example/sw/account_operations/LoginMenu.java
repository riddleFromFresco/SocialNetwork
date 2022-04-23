package com.example.sw.account_operations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sw.R;
import com.example.sw.application.SocialNetworkApplication;
import com.example.sw.chat_list.ChatsListMenu;
import com.example.sw.model.database_interfaces.AccountsInterface;
import com.example.sw.model.test_data.TestAccountsData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginMenu extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    EditText emailField;
    EditText passwordField;
    Button signInBtn;
    Button signUpBtn;

    Validators validators;
    MessageViewer messageViewer;

    AccountsInterface testAccountsData;


    boolean isEnteredDataValid(String email, String password) {
        if (!validators.isValidEmail(email)) {
            messageViewer.showMessage("Некорректный email", getApplicationContext());
            return false;
        }
        if (!validators.isValidPassword(password))
        {
            messageViewer.showMessage("Некорректный пароль", getApplicationContext());
            return false;
        }

        return true;
    }

    boolean doesAccountExist(String email, String password) {
        testAccountsData = new TestAccountsData();

        String truePassword = testAccountsData.getPasswordOfUser(email);
        if (!testAccountsData.doesUserExist(email)) {
            messageViewer.showMessage("Пользователя не существует.", getApplicationContext());
            return false;
        }

        if (!password.equals(truePassword)) {
            messageViewer.showMessage("Пароль неверный.", getApplicationContext());
            return false;
        }

        return true;
    }

    void openChatsMenu() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        SocialNetworkApplication application = ((SocialNetworkApplication) this.getApplication());
        application.setCurrentUsername(email);

        if (!isEnteredDataValid(email, password))
            return;
        if (!doesAccountExist(email, password))
            return;

        startActivity(new Intent(LoginMenu.this, ChatsListMenu.class));
    }

    void openSignUpPage() {
        startActivity(new Intent(LoginMenu.this, SignUpMenu.class));
    }

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        setFieldsValues();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChatsMenu();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpPage();
            }
        });

    }
}