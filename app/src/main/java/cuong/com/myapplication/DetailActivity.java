package cuong.com.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class DetailActivity extends AppCompatActivity {
    private Bundle b;
    Contact contact;
    DatabaseAccess db;
    TextView tvDetailName, tvDetailPhone, tvDetailAddress, tvDetailGender, tvDetailDate, tvDetailTime;
    ImageView ivCall, ivText;
    boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent myIntent = getIntent();
        contact = (Contact) myIntent.getSerializableExtra("contact");
        db = new DatabaseAccess(this);
        init();
        buttonAction();
        setData();
    }

    private void init() {
        tvDetailName = (TextView) findViewById(R.id.tv_contact_name_detail);
        tvDetailPhone = (TextView) findViewById(R.id.tvDetailPhone);
        tvDetailAddress = (TextView) findViewById(R.id.tvDetailAddress);
        tvDetailGender = (TextView) findViewById(R.id.tvDetailGender);
        tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
        tvDetailTime = (TextView) findViewById(R.id.tvDetailTime);

        ivCall = (ImageView) findViewById(R.id.ivDetailCall);
        ivText = (ImageView) findViewById(R.id.ivDetailText);
    }

    private void buttonAction() {
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + tvDetailPhone.getText()));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(call);
            }
        });
        ivText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent text = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+tvDetailPhone.getText()));
                text.putExtra("sms_body", "");
                startActivity(text);
            }
        });
    }

    private void setData(){
        tvDetailName.setText(contact.getmName());
        tvDetailAddress.setText(contact.getmAddress());
        tvDetailGender.setText(contact.getmGender());
        tvDetailDate.setText(contact.getmDate());
        tvDetailTime.setText(contact.getmTime());
        tvDetailPhone.setText(contact.getmPhone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if (edited){
                    Intent i = new Intent();
                    setResult(1,i);
                }
                this.finish();
                break;
            case R.id.btnEdit:
                Intent intent = new Intent(getBaseContext(), AddActivity.class);
                intent.putExtra("ID",contact.getmId());
                startActivityForResult(intent, 3);
                break;
            case R.id.btnDelete:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Delete this contact");
                alertBuilder.setMessage("Are you sure?");
                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteContact(contact);
                        db.close();
                        Intent intent = new Intent();
                        intent.putExtra("ID",String.valueOf(contact.getmId()));
                        setResult(1,intent);

                        finish();
                    }
                });
                alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            contact = (Contact) data.getExtras().getSerializable("RETURN");
            setData();
            edited = true;
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        if (edited){

            setResult(1,i);
        }
        else {
            setResult(2,i);
        }
        this.finish();
    }


}
