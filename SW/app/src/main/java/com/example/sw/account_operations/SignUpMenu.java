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

public class SignUpMenu extends AppCompatActivity {
    EditText emailField;
    EditText passwordField;
    EditText repeatedPasswordField;
    Button signUpBtn;

    Validators validators;
    MessageViewer messageViewer;
    AccountsInterface testAccountsData;

    boolean doesAccountExists(String username) {
        return testAccountsData.doesUserExist(username);
    }

    boolean createUser(String username, String password) {
        return testAccountsData.createNewAccount(username, password);
    }

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
        validators = new Validators();
        messageViewer = new MessageViewer();
        testAccountsData = new TestAccountsData();
    }

    boolean isAccountDataValid(String email, String password, String repeatedPassword) {
        if (!validators.isValidEmail(email)) {
            messageViewer.showMessage("Некорректный email", getApplicationContext());
            return false;
        }

        if (!validators.isValidPassword(password) || !validators.isValidPassword(repeatedPassword))
        {
            messageViewer.showMessage("Некорректный пароль", getApplicationContext());
            return false;
        }

        if (!password.equals(repeatedPassword)) {
            messageViewer.showMessage("Введенные пароли не совпадают", getApplicationContext());
            return false;
        }

        if (doesAccountExists(email)) {
            messageViewer.showMessage("Пользователь с таким именем уже существует", getApplicationContext());
            return false;
        }

        return true;
    }

    void openChatsMenu(String email, String password, String repeatedPassword) {
        if (!isAccountDataValid(email, password, repeatedPassword))
            return;

        boolean hasUserBeenCreated = createUser(email, password);
        if (!hasUserBeenCreated) {
            messageViewer.showMessage("Не удалось создать аккаунт.", getApplicationContext());
            return;
        }

        SocialNetworkApplication application = (SocialNetworkApplication) this.getApplication();
        application.setCurrentUsername(email);

        startActivity(new Intent(SignUpMenu.this, ChatsListMenu.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        setFieldsValues();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String repeatedPassword = repeatedPasswordField.getText().toString();

                openChatsMenu(email, password, repeatedPassword);
            }
        });
    }
}

