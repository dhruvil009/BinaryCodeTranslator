package com.notebook.idealabs.binarycodetranslator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText message;
    private EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.message);
        code = findViewById(R.id.code);
    }

    public void Encodetext(View view){
        code.setText(Encode(message.getText().toString()));
    }

    public void Decodetext(View view){
        message.setText(Decode(code.getText().toString()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                // User chose the "Settings" item, show the app settings UI...
                message.setText("");
                code.setText("");
                return true;
            case R.id.share_menu_id:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Hey check out this new app at: https://play.google.com/store/apps/details?id=com.notebook.idealabs.binarycodetranslator");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                return true;
            case R.id.rate_us_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.notebook.idealabs.binarycodetranslator")));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.notebook.idealabs.binarycodetranslator")));
                    return true;
                }
            case R.id.more_apps_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://developer?id=7665922803421240272")));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=7665922803421240272")));
                    return true;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void copymessage(View view){
        Copy(message.getText().toString());
    }

    public void copycode(View view){
        Copy(code.getText().toString());
    }

    public void Copy(String s){
        ((ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", s));
    }
    public String Encode(String s){
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    private String Decode(String s){
        try {
            String Text = "";
            int length = s.length() / 8;
            for (int i = 0; i < length; i++) {
                Text = Text + ((char) Integer.parseInt(s.substring(i * 8, (i + 1) * 8), 2));
            }
            return Text;
        } catch (NumberFormatException e) {
            return "Error";
        }
    }
}
