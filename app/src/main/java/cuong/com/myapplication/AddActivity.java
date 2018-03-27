package cuong.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtPhone, edtAddress;
    private RadioGroup radioGender;
    private RadioButton rbGender;
    private Button btnAdd, btnCancel;
    private DatabaseAccess db = new DatabaseAccess(this);
    private Bundle b;
    Contact contact = new Contact();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Add Contact");
        init();
        b = getIntent().getExtras();
        if (b!=null){
            contact.setmId(b.getInt("ID"));
            contact = db.getContact(contact.getmId());
            setData();
        }
    }

    private void init(){
        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }
    private  void setData(){
        getSupportActionBar().setTitle("Update Contact");
        btnAdd.setText("UPDATE");
        edtName.setText(contact.getmName());
        edtAddress.setText(contact.getmAddress());
        edtPhone.setText(contact.getmPhone());
        if (contact.getmGender().equals("Male")) {
            radioGender.check(R.id.radioMale);
        } else radioGender.check(R.id.radioFemale);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void add(){
        if (edtPhone.getText().toString().isEmpty()) {
            Toast.makeText(this,"Nhập số điện thoại",Toast.LENGTH_LONG).show();
            return;
        }
        if (edtName.getText().toString().isEmpty()) {

            contact.setmName("unknow");
         } else
        {
            contact.setmName(edtName.getText().toString());
        }

        contact.setmPhone(edtPhone.getText().toString());
        contact.setmAddress(edtAddress.getText().toString());
        long day = System.currentTimeMillis();
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(day));
        contact.setmDate(date);
        String h = new SimpleDateFormat("hh:mm:ss").format(new Date(day));
        contact.setmTime(h);
        int idrad = radioGender.getCheckedRadioButtonId();
        rbGender = (RadioButton) findViewById(idrad);
        contact.setmGender(rbGender.getText().toString());

        if (b!=null){
            db.updateContact(contact);


        } else {
            contact.setmId(db.addContact(contact));

        }

        db.close();
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("RETURN", contact);
        i.putExtras(b);
        setResult(RESULT_OK,i);


        finish();
    }

}
