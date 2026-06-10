package com.example.cloneampere;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrent = findViewById(R.id.txtCurrent);

        BatteryManager batteryManager =
                (BatteryManager) getSystemService(BATTERY_SERVICE);

        Intent batteryStatus = registerReceiver(
                null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        );

        if (batteryManager == null || batteryStatus == null) {
            txtCurrent.setText("Không lấy được thông tin pin");
            return;
        }

        // Current
        int currentNow = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);

        int currentAvg = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);

        // Battery %
        int level = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);

        int scale = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100f / scale;

        // Voltage
        int voltage = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_VOLTAGE, -1);

        // Temperature
        int temperature = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);

        // Status
        int status = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_STATUS, -1);

        // Health
        int health = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_HEALTH, -1);

        // Technology
        String technology = batteryStatus.getStringExtra(
                BatteryManager.EXTRA_TECHNOLOGY);

        // Charge Counter (µAh)
        int chargeCounter = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);

        // Capacity (%)
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

        txtCurrent.setText(
                "Current Now: " + currentNowMa + " mA\n" +
                        "Current Avg: " + currentAvgMa + " mA\n\n" +

                        "Battery: " + batteryPct + " %\n" +
                        "Capacity API: " + capacity + " %\n\n" +

                        "Voltage: " + voltageV + " V\n" +
                        "Temperature: " + tempC + " °C\n\n" +

                        "Status: " + statusText + "\n" +
                        "Health: " + healthText + "\n" +
                        "Technology: " + technology + "\n\n" +

                        "Charge Counter: " + chargeCounter + " µAh"
        );
    }
}