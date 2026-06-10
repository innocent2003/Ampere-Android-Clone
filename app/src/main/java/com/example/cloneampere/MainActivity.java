package com.example.cloneampere;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtCurrent;
    private TextView txtAvgCurrent;

    private TextView txtMin;
    private TextView txtMax;

    private TextView txtStatus;
    private TextView txtLevel;
    private TextView txtHealth;
    private TextView txtVoltage;
    private TextView txtTemp;
    private TextView txtTech;

    private TextView txtCapacity;
    private TextView txtChargeCounter;

    private float minCurrent = Float.MAX_VALUE;
    private float maxCurrent = Float.MIN_VALUE;

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {

            loadBatteryInfo();

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrent = findViewById(R.id.txtCurrent);
        txtAvgCurrent = findViewById(R.id.txtAvgCurrent);

        txtMin = findViewById(R.id.txtMin);
        txtMax = findViewById(R.id.txtMax);

        txtStatus = findViewById(R.id.txtStatus);
        txtLevel = findViewById(R.id.txtLevel);
        txtHealth = findViewById(R.id.txtHealth);
        txtVoltage = findViewById(R.id.txtVoltage);
        txtTemp = findViewById(R.id.txtTemp);
        txtTech = findViewById(R.id.txtTech);

        txtCapacity = findViewById(R.id.txtCapacity);
        txtChargeCounter = findViewById(R.id.txtChargeCounter);

        handler.post(updateTask);
    }

    private void loadBatteryInfo() {

        BatteryManager batteryManager =
                (BatteryManager) getSystemService(BATTERY_SERVICE);

        Intent batteryStatus = registerReceiver(
                null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        );

        if (batteryManager == null || batteryStatus == null) {
            txtCurrent.setText("Battery Error");
            return;
        }

        int currentNow = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        int currentAvg = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);

        int level = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);

        int scale = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100f / scale;

        int voltage = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_VOLTAGE, -1);

        int temperature = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);

        int status = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_STATUS, -1);

        int health = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_HEALTH, -1);

        String technology = batteryStatus.getStringExtra(
                BatteryManager.EXTRA_TECHNOLOGY);

        int chargeCounter = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);

        int capacity = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY);

        String statusText;

        switch (status) {

            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusText = "Charging";
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusText = "Discharging";
                break;

            case BatteryManager.BATTERY_STATUS_FULL:
                statusText = "Full";
                break;

            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusText = "Not Charging";
                break;

            default:
                statusText = "Unknown";
        }

        String healthText;

        switch (health) {

            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthText = "Good";
                break;

            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthText = "Overheat";
                break;

            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthText = "Dead";
                break;

            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthText = "Over Voltage";
                break;

            case BatteryManager.BATTERY_HEALTH_COLD:
                healthText = "Cold";
                break;

            default:
                healthText = "Unknown";
        }

        float currentNowMa = currentNow / 1000f;
        float currentAvgMa = currentAvg / 1000f;
        float voltageV = voltage / 1000f;
        float tempC = temperature / 10f;

        if (currentNowMa < minCurrent) {
            minCurrent = currentNowMa;
        }

        if (currentNowMa > maxCurrent) {
            maxCurrent = currentNowMa;
        }

        String currentText;

        if (currentNowMa > 0) {
            currentText = "+" +
                    String.format("%.0f", currentNowMa);
        } else {
            currentText =
                    String.format("%.0f", currentNowMa);
        }

        txtCurrent.setText(currentText);

        txtAvgCurrent.setText(
                "Average Current: "
                        + String.format("%.0f", currentAvgMa)
                        + " mA"
        );

        txtMin.setText(
                "min: "
                        + String.format("%.0f", minCurrent)
                        + " mA"
        );

        txtMax.setText(
                "max: "
                        + String.format("%.0f", maxCurrent)
                        + " mA"
        );

        txtStatus.setText(
                "Status: " + statusText
        );

        txtLevel.setText(
                "Level: "
                        + String.format("%.0f", batteryPct)
                        + "%"
        );

        txtHealth.setText(
                "Health: " + healthText
        );

        txtVoltage.setText(
                "Voltage: "
                        + voltageV
                        + " V"
        );

        txtTemp.setText(
                "Temperature: "
                        + tempC
                        + " °C"
        );

        txtTech.setText(
                "Technology: " + technology
        );

        txtCapacity.setText(
                "Capacity API: "
                        + capacity
                        + "%"
        );

        txtChargeCounter.setText(
                "Charge Counter: "
                        + chargeCounter
                        + " µAh"
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(updateTask);
    }
}