package com.example.secure;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import com.example.secure.Permissions;

public class Backup {
    private HomeActivity activity;
    int index = 0;

    //Backup function
    public Backup(HomeActivity homeActivity) {
        this.activity = homeActivity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final Database db, final String outFileName) {
        // verifying the read/write access
        Permissions.verifyStoragePermissions(activity);
        // The folder where the database will be saved
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        try{
            // if folder does not exist, create one
            boolean success = true;
            if (!folder.exists())
                success = folder.mkdirs();
            //If exist, then ask user to insert the name of the imported database file
            if (success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Backup Name");
                final EditText input = new EditText(activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                // Dialog to display the message
                builder.setView(input);
                builder.setPositiveButton("Save", (dialog, which) -> {
                    String m_Text = input.getText().toString();
                    String out = outFileName + m_Text + ".db";
                    // Proceed with the backup
                    db.backup(out);
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                builder.show();
            } else
                Toast.makeText(activity, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Import the database
    public void performRestore(final Database db) {

        Permissions.verifyStoragePermissions(activity);
        // The folder where the database is saved
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        //If exist, then proceed with the import
        if (folder.exists()) {
            final File[] files = folder.listFiles();
            index = files.length - 1;
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());
            // Dialog to display the message
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Restore:");
            builder.setPositiveButton("Restore", (dialog, which) -> {
                try {
                    // call import function in the database
                    db.importDB(files[index].getPath());
                    activity.onRestart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();

        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }
}
