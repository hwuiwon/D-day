package io.hwuiwon.d_day;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.joda.time.DateTime;
import org.joda.time.Days;

import io.hwuiwon.d_day.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        CircularProgressBar progressBar = findViewById(R.id.cProgressBar);
        SharedPreferences settings = getSharedPreferences("data.ser", MODE_PRIVATE);

        if (settings.getBoolean("hasSave", false)) {
            binding.eventTitle.setText(settings.getString("title", ""));
            binding.dateFromTo.setText(settings.getString("date", ""));
            binding.dDate.setText(settings.getString("dDay", ""));
            progressBar.setProgressWithAnimation(
                    settings.getFloat("percentage", 0), (long) 2000);
        }

        binding.addButton.setOnClickListener(unused ->
                startActivityForResult(new Intent(this,
                        DateActivity.class), 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CircularProgressBar progressBar = findViewById(R.id.cProgressBar);
                SharedPreferences.Editor editor =
                        getSharedPreferences("data.ser", MODE_PRIVATE).edit();

                assert data != null;
                String title = data.getStringExtra("title");
                String startDate = data.getStringExtra("startDate");
                String endDate = data.getStringExtra("endDate");

                int startY = Integer.valueOf(startDate) / 10000;
                int startM = Integer.valueOf(startDate) % 10000 / 100;
                int startD = Integer.valueOf(startDate) % 100;
                int endY = Integer.valueOf(endDate) / 10000;
                int endM = Integer.valueOf(endDate) % 10000 / 100;
                int endD = Integer.valueOf(endDate) % 100;

                DateTime sDate = new DateTime(startY, startM, startD, 0, 0, 0);
                DateTime eDate = new DateTime(endY, endM, endD, 0, 0, 0);
                String date = getString(R.string.dateForm,
                        startY, startM, startD, endY, endM, endD);

                binding.eventTitle.setText(title);
                binding.dateFromTo.setText(date);

                float daysTotal = calculateDate(sDate, eDate);
                float daysLeftNow = dateFromNow(eDate);

                if (daysLeftNow < daysTotal) {
                    String dDay = getString(R.string.dDayForm, (int) daysLeftNow + 1);
                    binding.dDate.setText(dDay);
                    editor.putString("dDay", dDay);
                    progressBar.setProgressWithAnimation(
                            ((daysTotal - daysLeftNow - 1) / daysTotal) * 100, (long) 2000);
                    editor.putFloat("percentage", ((daysTotal - daysLeftNow - 1) / daysTotal) * 100);
                } else {
                    String dDay = getString(R.string.dDayForm, (int) daysTotal);
                    binding.dDate.setText(dDay);
                    editor.putString("dDay", dDay);
                    progressBar.setProgress(0f);
                }

                editor.putString("title", title);
                editor.putString("date", date);
                editor.putBoolean("hasSave", true);
                editor.apply();
            }
        }
    }

    public int calculateDate(DateTime startDate, DateTime endDate) {
        return Days.daysBetween(startDate, endDate).getDays();
    }

    public int dateFromNow(DateTime endDate) {
        DateTime now = new DateTime();
        return Days.daysBetween(now, endDate).getDays();
    }
}