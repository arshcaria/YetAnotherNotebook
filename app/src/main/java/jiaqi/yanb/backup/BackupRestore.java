package jiaqi.yanb.backup;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jiaqi.yanb.Note;
import jiaqi.yanb.NoteLab;
import jiaqi.yanb.R;

/* Backup file example (JSON)
********************************************
{
  "root": {
    "notes": [
      {
        "uuid": "uuid1",
        "title": "Note 1",
        "content": "Note Content 1",
        "date": "1234554321"
      },
      {
        "uuid": "uuid2",
        "title": "Note 2",
        "content": "Note Content 2",
        "date": "2345665432"
      }
    ]
  }
}
*******************************************
*/

public class BackupRestore {

    private static final String TAG = BackupRestore.class.getSimpleName();

    private static final String BACKUP_FILE_NAME = "backup.json";
    private static final String APP_DIR = "YANB";

    private static BackupRestore sBR;

    private static NoteLab sNoteLab;

    private static Context sContext;

    private BackupRestore(Context context) {
        sContext = context;
        sNoteLab = NoteLab.getInstance(sContext);
    }

    public static BackupRestore getInstance(Context context) {
        if (sBR == null) {
            sBR = new BackupRestore(context);
        }
        return sBR;
    }

    public void backupNotes() {
        BackupRestore br = BackupRestore.getInstance(sContext.getApplicationContext());
        if(br.backupToFile(sNoteLab.getNotes())) {
            Toast.makeText(sContext, R.string.activity_main_backup_succussful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(sContext, R.string.activity_main_backup_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean backupToFile(List<Note> notes) {
        if (!isExternalStorageWritable()) {
            // external storage is not writable, fail and return false.
            Log.e(TAG, "external storage not writable");
            return false;
        }

        JSONObject jo = getJSONObject(notes);
        String jsonString = jo.toString();

        String exStorageDirPath = Environment.getExternalStorageDirectory().toString();

        File dir = new File(exStorageDirPath + "/" + APP_DIR);
        if (!dir.mkdirs() && !dir.exists()) {
            Log.e(TAG, "dir creation failed and the dir does not exist.");
            return false;
        }

        File file = new File(exStorageDirPath + "/" + APP_DIR + "/" + BACKUP_FILE_NAME);

        if (file.exists()) {

        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonString);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if there is no exception, you should not reach here.
        Log.e(TAG, "should not reach here");

        return false;
    }

    private static JSONObject getJSONObject(List<Note> notes) {
        JSONArray jaNotes = new JSONArray();

        for (Note note : notes) {
            String uuidString = note.getId().toString();
            String title = note.getTitle();
            String content = note.getContent();
            String dateString = note.getDate().toString();

            JSONObject joNote = new JSONObject();

            try {
                joNote.put("uuid", uuidString);
                joNote.put("title", title);
                joNote.put("content", content);
                joNote.put("date", dateString);

                jaNotes.put(joNote);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject joRoot = new JSONObject();
        try {
            joRoot.put("note", jaNotes);
            return joRoot;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if external storage is writable or not
     *
     * @return true if writable, false otherwise
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Check if external storage is readable or not
     *
     * @return true if readable, false otherwise
     */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
