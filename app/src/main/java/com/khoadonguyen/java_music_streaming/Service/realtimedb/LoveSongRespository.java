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
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;

public class LoveSongRespository {
    private static String tag = "LoveSongRespository";
    private static final String FAVORITE_NODE = "favorite";

    private DatabaseReference firebaseDatabase;
    private String user_id;

    public LoveSongRespository() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Log.d(tag, "user_id is auth" + currentUser.getUid());
            user_id = currentUser.getUid();
        } else {
            user_id = null;
        }
    }

    public void addFavorite(Song song, SimpleCallback callback) {
        if (user_id == null) {
            Log.e(tag, "User chưa đăng nhập");
            if (callback != null) callback.onComplete(false);
            return;
        }
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(String.valueOf(source_id)).child(song.getId()).setValue(song.getUrl()).addOnSuccessListener(aVoid -> {
            Log.d(tag, "Thêm bài hát yêu thích thành công");
            if (callback != null) callback.onComplete(true);
        }).addOnFailureListener(e -> {
            Log.e(tag, "Lỗi khi thêm bài hát yêu thích: " + e.getMessage());
            if (callback != null) callback.onComplete(false);
        });
    }

    public interface SimpleCallback {
        void onComplete(boolean success);
    }

    public void removeFavorite(Song song, SimpleCallback callback) {
        if (user_id == null) {
            Log.e(tag, "User chưa đăng nhập");
            if (callback != null) callback.onComplete(false);
            return;
        }
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(String.valueOf(source_id)).child(song.getId()).removeValue().addOnSuccessListener(aVoid -> {
            Log.d(tag, "Xóa bài hát yêu thích thành công");
            if (callback != null) callback.onComplete(true);
        }).addOnFailureListener(e -> {
            Log.e(tag, "Lỗi khi xóa bài hát yêu thích: " + e.getMessage());
            if (callback != null) callback.onComplete(false);
        });
    }


    public void isFavorite(Song song, FavoriteCheckCallback callback) {
        if (user_id == null) {
            callback.onResult(false);
            return;
        }
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(String.valueOf(source_id)).child(song.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = snapshot.exists();
                Log.d(tag, String.valueOf(exists));
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
        int source_id = SourceExtractor.getInstance().getCurrent_source_id();

        firebaseDatabase.child(user_id).child(FAVORITE_NODE).child(String.valueOf(source_id))

                .limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
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