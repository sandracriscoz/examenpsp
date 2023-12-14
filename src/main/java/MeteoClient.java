import constants.Constantes;
import redis.clients.jedis.Jedis;

import java.util.Scanner;
import java.util.Set;
public class MeteoClient {
    public static void main(String[] args) {
        String command = "";
        Scanner sc = new Scanner(System.in);
        try (Jedis jedis = new Jedis(Constantes.REDIS_SERVER_URI, Constantes.REDIS_SERVER_PORT)) {
            while (!command.equals("exit")){
                System.out.print("Enter the command: ");
                String rawCommand = sc.nextLine() + " ";
                String[] commandSplit = rawCommand.split(" ", 2);
                command = commandSplit[0];
                switch (command) {
                    case "LAST" -> {
                        String lastTemperature = jedis.hget(String.format(Constantes.HASH_KEY_LAST_TEMPERATURE_REDIS, Constantes.INICIALES_ALUMNO, commandSplit[1]), "temperature");
                        System.out.printf("Ultima temperatura registrada %s: %s\n", commandSplit[1], lastTemperature);
                    }
                    case "MAXTEMP" -> {

                    }
                    case "ALERTS" -> {
                        Set<String> keys = jedis.keys("ARG:ALERTS");
                        if (keys != null) {
                            for (String key : keys) {
                                String alert = jedis.get(key);
                                System.out.println(alert);
                                jedis.del(key);
                            }
                        }
                    }
                }
            }
        }
    }
}
