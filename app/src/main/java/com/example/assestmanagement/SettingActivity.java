package com.example.assestmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    CardView ipconfig, ifconfigForm, setpower, setpowerform;
    Button submitConfig,submitpower;
    SeekBar seekBar;
  public static int progressChangedValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ipconfig = findViewById(R.id.ipconfig);
        ifconfigForm = findViewById(R.id.ipconfigForm);
        setpower = findViewById(R.id.setPower);
        setpowerform = findViewById(R.id.setpowerform);
        submitConfig = findViewById(R.id.button_submit_url);
        submitpower=findViewById(R.id.submitpower);
        seekBar=findViewById(R.id.seekBar_luminosite);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SettingActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                seekBar.setProgress(progressChangedValue);
            }
        });        setpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setpowerform.setVisibility(View.VISIBLE);
            }
        });

        submitpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setpowerform.setVisibility(View.GONE);
            }
        });
        submitConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifconfigForm.setVisibility(View.GONE);
            }
        });
        ipconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifconfigForm.setVisibility(View.VISIBLE);
            }
        });

    }
}