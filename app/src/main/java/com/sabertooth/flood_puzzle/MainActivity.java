package com.sabertooth.flood_puzzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    FloodMaker FM;
    TextView result;
    TextInputEditText user_row, user_colour;
    RadioGroup game_mode;
    MaterialButton generate;
    RecyclerView recyclerView;
    RadioButton sp,dp,ap;
    int col_now, row_now, g_mode_now, color_cnt_now;
    ImageButton helpbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    FM = new FloodMaker(row_now, col_now, color_cnt_now);
                    AdapterGrid adapterGrid = new AdapterGrid(getApplicationContext(), FM, result,g_mode_now);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,
                            row_now, RecyclerView.VERTICAL, false
                    );
                    recyclerView.setAdapter(adapterGrid);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.requestFocus();

                }
            }
        });
        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Help_activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //Toast.makeText(getApplicationContext(),"HELP NEEDED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    void init() {
        recyclerView = findViewById(R.id.recycle_view);
        user_row = findViewById(R.id.input_user_row);
        game_mode = findViewById(R.id.input_user_game_mode);
        generate = findViewById(R.id.input_user_gen);
        user_colour = findViewById(R.id.input_user_color);
        result = findViewById(R.id.text_view_move_count);
        ap = findViewById(R.id.input_user_ai_player);
        sp = findViewById(R.id.input_user_single_player);
        dp = findViewById(R.id.input_user_dual_player);
        helpbtn = findViewById(R.id.btn_help);
    }

    boolean validate() {

        int row = Integer.parseInt(((user_row.getText() == null || user_row.getText().toString().isEmpty())  ? "20" : user_row.getText().toString()));
        if (row > 20 || row < 5) {
            user_row.setError("Number must be in range [5,20]");
            return false;
        }
        int color_cnt = Integer.parseInt(((user_colour.getText() == null || user_colour.getText().toString().isEmpty())  ? "20" : user_colour.getText().toString()));
        if (color_cnt > 7 || color_cnt < 2) {
            user_colour.setError("Number must be in range [2,7]");
            return false;
        }
        col_now = row;
        row_now = row;
        color_cnt_now = color_cnt;
        if (sp.isChecked()) {
            g_mode_now = 0;
        } else if (dp.isChecked()) {
            g_mode_now = 1;
        } else if (ap.isChecked()) {
            g_mode_now = 2;
        }
        return true;
    }
}
