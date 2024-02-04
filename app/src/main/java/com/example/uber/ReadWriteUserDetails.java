package com.example.uber;

import java.security.PublicKey;

public class ReadWriteUserDetails {
    public String firstName;
    public String lastName;
    public  String email;

    public ReadWriteUserDetails(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
