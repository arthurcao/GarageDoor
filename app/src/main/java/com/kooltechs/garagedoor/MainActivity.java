package com.kooltechs.garagedoor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kooltechs.garagedoor.dns_api.DNSService;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //https://github.com/arthurcao/GarageDoor.git
        //https://code.google.com/p/mdnsjava/source/browse/tags/mdnsjava_2.1.5/src/main/java/Tester.java
        //https://code.google.com/p/mdnsjava/
        //https://justanapplication.wordpress.com/2013/11/03/service-discovery-in-android-and-ios-part-three-if-you-want-something-done-properly-mdns-and-dns-service-discovery-basics/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
//        new Thread(){
//            @Override
//            public void run() {
//                DNSService.test();
//
//            }
//        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
