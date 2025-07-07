package com.khoadonguyen.java_music_streaming.Service.Auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String email;
    private String avt;

    public String getEmail() {
        if (email != null) {
            return email;
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                email = user.getEmail();
                return email;
            }
            return null;
        }
    }

    public String getAvt() {
        if (avt != null) {
            return avt;
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getPhotoUrl() != null) {
                avt = user.getPhotoUrl().toString();
                return avt;
            }
            return null;
        }
    }
}
