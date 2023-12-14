import org.eclipse.paho.client.mqttv3.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MeteoStations {
    private static final int NUM_STATIONS = 10;

    public static void main(String[] args) {
        for (int i = 1; i <= NUM_STATIONS; i++) {
            String clientId = "Station" + i;
            Thread stationThread = new Thread(new MeteoStationThread(clientId));
            stationThread.start();
        }
    }
}

class MeteoStationThread implements Runnable {
    private static final String BROKER_URL = "tcp://mqtt.eclipse.org:1883";
    private String clientId;

    public MeteoStationThread(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            MqttClient client = new MqttClient(BROKER_URL, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + BROKER_URL);
            client.connect(connOpts);
            System.out.println("Connected");

            while (true) {
                String measurements = generateMeasurements();
                String topic = "/ABC/METEO/" + clientId + "/MEASUREMENTS";
                MqttMessage message = new MqttMessage(measurements.getBytes());
                client.publish(topic, message);
                System.out.println("Published: " + measurements);

                Thread.sleep(5000); // Espera 5 segundos
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String generateMeasurements() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Date now = new Date();
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);

        double temperature = generateRandomTemperature(-10.0, 40.0);

        return String.format("date=31/12/2022#hour=23:59#temperature=30", date, time, temperature);
    }

    private double generateRandomTemperature(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }
}
