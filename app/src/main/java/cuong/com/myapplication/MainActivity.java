package cuong.com.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ListContactApadter mAdapter;
    private DatabaseAccess db;
    private ArrayList<Contact> contacts;
    private  int posClick = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseAccess(this);
        contacts = new ArrayList<>();
        getData();
        handle();
    }

    private void getData(){
        contacts.clear();
        contacts = db.getAllContacts();
        db.close();
    }

    private void handle(){
        mRecyclerView = (RecyclerView) findViewById(R.id.lv_contacts);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ListContactApadter(contacts, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);}


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.addContact:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.deleteAll:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("DROP ALL DATA");
                alertBuilder.setMessage("This action will deleta all data in SQLite. Are you sure?");
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAll();
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
    private void deleteAll(){
        db.deleteAllContact();
        contacts.clear();
        mAdapter.notifyDataSetChanged();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 1 && resultCode == RESULT_OK) {
            Contact contact = (Contact) data.getExtras().getSerializable("RETURN");
            contacts.add(contact);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode == 2 && resultCode == 1){
            getData();
            mAdapter = new ListContactApadter(contacts, MainActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

    }
        if (requestCode == 2 && resultCode == 2){
           return;
        }
    }


    public class ListContactApadter extends RecyclerView.Adapter<ListContactApadter.MyViewHolder> {
        private ArrayList<Contact> contacts;
        private Context mContext;
        private LayoutInflater mInflater;

        public ListContactApadter(ArrayList<Contact> contacts, Context context){
            this.mContext = context;
            this.contacts = contacts;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.item_list_contact, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Contact contact = contacts.get(position);
            holder.line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this,DetailActivity.class);
                    myIntent.putExtra("contact",contact);
                    MainActivity.this.startActivityForResult(myIntent,2);
                }
            });
            holder.mTvName.setText(contact.getmName());
            holder.getmTvNumber.setText(contact.getmPhone());
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView mTvName;
            TextView getmTvNumber;
            LinearLayout line;
            public MyViewHolder(View itemView){
                super(itemView);
                itemView.setClickable(true);
                mTvName = (TextView) itemView.findViewById(R.id.tv_contact_name);
                getmTvNumber = itemView.findViewById(R.id.tv_contact_number);
                line = itemView.findViewById(R.id.lv_item);
            }
        }
    }

}
