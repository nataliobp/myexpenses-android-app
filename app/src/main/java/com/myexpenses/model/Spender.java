package com.myexpenses.model;

public class Spender {

    public final String name;
    public final String spenderId;
    public final String email;

    public Spender(String aSpenderId, String aName, String anEmail) {
        name = aName;
        spenderId = aSpenderId;
        email = anEmail;
    }
}
