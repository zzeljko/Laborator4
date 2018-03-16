package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private static final int CONTACTS_MANAGER_REQUEST_CODE = 0;
    private Button showHideButton;
    private Button saveButton;
    private Button cancelButton;
    private LinearLayout extraDetails;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        showHideButton = findViewById(R.id.showButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        extraDetails = findViewById(R.id.extraDetailsLayout);
        phoneEditText = findViewById(R.id.phoneNumberEditText);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

        showHideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (extraDetails.getVisibility() == View.VISIBLE) {
                    extraDetails.setVisibility(View.INVISIBLE);
                    showHideButton.setText("Show additional details");
                }
                else {
                    extraDetails.setVisibility(View.VISIBLE);
                    showHideButton.setText("Hide additional details");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveContact();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, new Intent());
            }
        });

    }

    private void saveContact() {
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();
        String jobTitle = ((EditText) findViewById(R.id.positionEditText)).getText().toString();
        String company = ((EditText) findViewById(R.id.companyEditText)).getText().toString();
        String website = ((EditText) findViewById(R.id.websiteEditText)).getText().toString();
        String im = ((EditText) findViewById(R.id.messengerIdEditText)).getText().toString();

        if (name != null) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        }
        if (phone != null) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        }
        if (email != null) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        }
        if (address != null) {
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
        }
        if (jobTitle != null) {
            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
        }
        if (company != null) {
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
        }
        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
        if (website != null) {
            ContentValues websiteRow = new ContentValues();
            websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
            websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
            contactData.add(websiteRow);
        }
        if (im != null) {
            ContentValues imRow = new ContentValues();
            imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
            imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
            contactData.add(imRow);
        }
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
        startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
