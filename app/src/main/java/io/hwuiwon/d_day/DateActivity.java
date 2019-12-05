package io.hwuiwon.d_day;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.Calendar;

import io.hwuiwon.d_day.databinding.ActivityDateBinding;

public class DateActivity extends AppCompatActivity {

    protected ActivityDateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date);
    }

    public void showDatePicker(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener =
                (datePicker, year, month, day) -> {
                    String tmp;
                    if (day / 10 < 1) {
                        tmp = year + "" + (month + 1) + "0" + day;
                    } else {
                        tmp = year + "" + (month + 1) + "" + day;
                    }

                    switch (view.getId()) {
                        case R.id.dateStartBT:
                            binding.startDate.setText(tmp);
                            break;
                        case R.id.dateEndBT:
                            binding.endDate.setText(tmp);
                            break;
                    }
                };

        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(DateActivity.this,
                onDateSetListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("Please select date");
        dialog.show();
    }

    public void saveEvent(View view){
        Intent intent = new Intent();
        intent.putExtra("title", binding.eventName.getText().toString());
        intent.putExtra("startDate", binding.startDate.getText().toString());
        intent.putExtra("endDate", binding.endDate.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}