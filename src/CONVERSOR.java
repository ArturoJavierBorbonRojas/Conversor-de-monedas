import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import java.util.Map;

public class CONVERSOR {

    // Clase para mapear la respuesta de la API
    static class ExchangeRateResponse {
        String base_code;
        Map<String, Double> conversion_rates;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Se muestran las siguientes divisas:"+
                "\n AED\tUAE Dirham\tUnited Arab Emirates\n" +
                "AFN\tAfghan Afghani\tAfghanistan\n" +
                "ALL\tAlbanian Lek\tAlbania\n" +
                "AMD\tArmenian Dram\tArmenia ");

        System.out.print("Ingrese la divisa o moneda nacional que tiene: ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la divisa o moneda nacional a la que desea convertir: ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la cantidad de dinero para convertir: ");
        double amount = scanner.nextDouble();

        String apiKey = "35938f6f805358c646f3954a";

        // Crear la URL
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        // Crear el cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Enviar la solicitud y obtener la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Comprobar si la respuesta fue exitosa (código de estado 200)
        if (response.statusCode() == 200) {
            // Deserializar la respuesta JSON usando GSON
            Gson gson = new Gson();
            ExchangeRateResponse exchangeRateResponse = gson.fromJson(response.body(), ExchangeRateResponse.class);

            // Verificar si la divisa objetivo está en los rates
            if (exchangeRateResponse.conversion_rates.containsKey(toCurrency)) {
                // Obtener la tasa de conversión
                double conversionRate = exchangeRateResponse.conversion_rates.get(toCurrency);
                double convertedAmount = amount * conversionRate;

                System.out.println(amount + " " + baseCurrency + " = " + convertedAmount + " " + toCurrency);
            } else {
                System.out.println("La divisa objetivo no es válida.");
            }
        } else {
            System.out.println("Error al obtener los datos de la API. Código de respuesta: " + response.statusCode());
        }

        scanner.close();
    }
}