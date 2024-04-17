package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_add, btn_view;
    EditText et_name, et_age;
    ListView lv_StudentList;
    ArrayAdapter studentArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add = findViewById(R.id.btn_add);
        btn_view = findViewById(R.id.btn_view);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);
        lv_StudentList = findViewById(R.id.lv_StudentList);
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowStudentsOnListView(dataBaseHelper);
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                ShowStudentsOnListView(dataBaseHelper);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentMod studentMod;
                try {
                    studentMod = new StudentMod(-1,
                            et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()));
                    Toast.makeText(MainActivity.this,
                            studentMod.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Enter Valid input",
                            Toast.LENGTH_SHORT).show();
                    studentMod = new StudentMod(-1, "ERROR", 0);
                }
                boolean b = dataBaseHelper.addOne(studentMod);
                Toast.makeText(MainActivity.this, "SUCCESS= " + b,
                        Toast.LENGTH_SHORT).show();
                ShowStudentsOnListView(dataBaseHelper);
            }
        });
        lv_StudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                StudentMod ClickedStudent = (StudentMod)
                        adapterView.getItemAtPosition(i);
                dataBaseHelper.DeleteOne(ClickedStudent);
                ShowStudentsOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted" +
                        ClickedStudent.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<StudentMod> searchResult = dataBaseHelper.searchStudent(s);
                if (searchResult.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Student not found!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    studentArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_list_item_1, searchResult);
                    lv_StudentList.setAdapter(studentArrayAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void ShowStudentsOnListView(DataBaseHelper dataBaseHelper) {
        studentArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
        lv_StudentList.setAdapter(studentArrayAdapter);
    }
}
