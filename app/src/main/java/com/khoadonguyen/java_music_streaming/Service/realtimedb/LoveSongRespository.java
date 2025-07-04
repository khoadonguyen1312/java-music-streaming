package com.khoadonguyen.java_music_streaming.Service.realtimedb;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khoadonguyen.java_music_streaming.Model.Song;

public class LoveSongRespository {
    private static final String FAVORITE_NODE = "favorite";
    private static String tag = "RealtimeDbHelper";
    private DatabaseReference firebaseDatabase;
    private String user_id;

    public LoveSongRespository() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            user_id = currentUser.getUid();
        } else {
            user_id = null;
        }
    }

    public void addFavorite(Song song) {
        if (user_id == null) {
            Log.e(tag, "User chưa đăng nhập");
            return;
        }

        try {
            firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(song.getId()).setValue(song)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(tag, "Thêm bài hát yêu thích thành công");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(tag, "Lỗi khi thêm bài hát yêu thích: " + e.getMessage());
                    });

        } catch (Exception e) {
            Log.e(tag, "Exception khi thêm favorite: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeFavorite(Song song) {
        if (user_id == null) {
            Log.e(tag, "User chưa đăng nhập");
            return;
        }

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(song.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(tag, "Xóa bài hát yêu thích thành công");
                })
                .addOnFailureListener(e -> {
                    Log.e(tag, "Lỗi khi xóa bài hát yêu thích: " + e.getMessage());
                });
    }


    public void isFavorite(Song song, FavoriteCheckCallback callback) {
        if (user_id == null) {
            callback.onResult(false);
            return;
        }

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(song.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean exists = snapshot.exists();
                        callback.onResult(exists);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(tag, "Lỗi khi kiểm tra favorite: " + error.getMessage());
                        callback.onResult(false);
                    }
                });
    }


    public interface FavoriteCheckCallback {
        void onResult(boolean isFavorite);
    }


    public void getAllFavorites(FavoriteListCallback callback) {
        if (user_id == null) {
            callback.onResult(null);
            return;
        }

        firebaseDatabase.child(user_id).child(FAVORITE_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback.onResult(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(tag, "Lỗi khi lấy danh sách favorite: " + error.getMessage());
                        callback.onResult(null);
                    }
                });
    }

    public interface FavoriteListCallback {
        void onResult(DataSnapshot snapshot);
    }
}