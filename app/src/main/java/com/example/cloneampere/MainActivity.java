package com.example.cloneampere;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtCurrent;
    private TextView txtAvgCurrent;
    private TextView txtBattery;
    private TextView txtVoltage;
    private TextView txtTemperature;
    private TextView txtStatus;
    private TextView txtHealth;
    private TextView txtTechnology;
    private TextView txtCapacity;
    private TextView txtChargeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrent = findViewById(R.id.txtCurrent);
        txtAvgCurrent = findViewById(R.id.txtAvgCurrent);
        txtBattery = findViewById(R.id.txtBattery);
        txtVoltage = findViewById(R.id.txtVoltage);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtStatus = findViewById(R.id.txtStatus);
        txtHealth = findViewById(R.id.txtHealth);
        txtTechnology = findViewById(R.id.txtTechnology);
        txtCapacity = findViewById(R.id.txtCapacity);
        txtChargeCounter = findViewById(R.id.txtChargeCounter);

        loadBatteryInfo();
    }

    private void loadBatteryInfo() {

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

        txtCurrent.setText(String.format("%.0f mA", currentNowMa));
        txtAvgCurrent.setText("Average Current: " + currentAvgMa + " mA");
        txtBattery.setText("Battery: " + batteryPct + " %");
        txtVoltage.setText("Voltage: " + voltageV + " V");
        txtTemperature.setText("Temperature: " + tempC + " °C");
        txtStatus.setText("Status: " + statusText);
        txtHealth.setText("Health: " + healthText);
        txtTechnology.setText("Technology: " + technology);
        txtCapacity.setText("Capacity API: " + capacity + " %");
        txtChargeCounter.setText("Charge Counter: " + chargeCounter + " µAh");
    }
}