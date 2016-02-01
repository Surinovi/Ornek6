package com.ab2016.neval.ornek6;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    Cursor c=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If the database already exist no need to copy it from the assets folder
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases/ab2016";
            File file = new File(destPath);
            File path = new File("/data/data/" + getPackageName() + "/databases/");
            if (!file.exists()) {
                path.mkdirs();
                CopyDB( getBaseContext().getAssets().open("ab2016"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DBAdapter dbadapter = new DBAdapter(this);


        // Iletisim ekle
        dbadapter.open();
        long id = dbadapter.insertContact("Nese Ozcelik", "nozcelik@bilkent.edu.tr");
        id = dbadapter.insertContact("Neval Goksel", "nevalgoksel@gmail.com");

        dbadapter.close();


        // Butun iletisim bilgilerini al
        dbadapter.open();
        c = dbadapter.getAllContacts();
        if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        dbadapter.close();


        // Bir iletisim bilgisi al
        dbadapter.open();
        Cursor c = dbadapter.getContact(2);
        if (c.moveToFirst())
            DisplayContact(c);
        else
            Toast.makeText(this, "Iletisim bulunamadı", Toast.LENGTH_LONG).show();
        dbadapter.close();



        // Iletisim güncelle
        dbadapter.open();
        if (dbadapter.updateContact(1, "Nese Ozcelik", "neseozcelik@gmail.com"))
            Toast.makeText(this, "Guncellendi.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Guncellenemedi.", Toast.LENGTH_LONG).show();
        dbadapter.close();



        // Delete a contact
        dbadapter.open();
        if (dbadapter.deleteContact(1))
            Toast.makeText(this, "Silindi.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Silinemedi.", Toast.LENGTH_LONG).show();
        dbadapter.close();

    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void DisplayContact(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Isim: " + c.getString(1) + "\n" +
                        "Email:  " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }
}
