package com.example.sw.model.test_data;

import com.example.sw.model.data_models.AccountData;
import com.example.sw.model.database_interfaces.AccountsInterface;

import java.util.ArrayList;


public class TestAccountsData implements AccountsInterface {
    ArrayList<AccountData> accounts;


    public TestAccountsData() {
        accounts = new ArrayList<AccountData>();

        for (int i = 0; i < 100; i++)
            accounts.add(new AccountData(
                "User" + String.valueOf(i) +"@example.com", "Passsword1%#"
                )
            );
    }

    @Override
    public String getPasswordOfUser(String username) {
        for (AccountData account: accounts)
            if (account.getUsername().equals(username))
                return account.getPassword();

        return null;
    }

    @Override
    public boolean doesUserExist(String username) {
        for (AccountData account: accounts)
            if (account.getUsername().equals(username))
                return true;

        return false;
    }

    @Override
    public boolean createNewAccount(String username, String password) {
        accounts.add(new AccountData(username, password));
        return true;
    }
}
