package com.khoadonguyen.java_music_streaming.storage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;

public class PathDownloader {

    public static void launchFolderPicker(Activity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        launcher.launch(intent);
    }

    public static void handleResult(Activity activity, Intent data) {
        Uri treeUri = data.getData();
        if (treeUri != null) {
            final int flags = data.getFlags() &
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            SharePreferencesHelper.setString(KeyNames.DOWNLOADPATH, treeUri.toString(), activity);
        }
    }
}
