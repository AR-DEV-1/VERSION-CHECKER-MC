import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MinecraftVersionChecker {
    public static void main(String[] args) {
        try {
            String currentVersion = getCurrentMinecraftVersion();
            System.out.println("Current Minecraft Version: " + currentVersion);
        } catch (IOException e) {
            System.out.println("Error occurred while fetching Minecraft version: " + e.getMessage());
        }
    }

    private static String getCurrentMinecraftVersion() throws IOException {
        // URL to fetch Minecraft version information
        String url = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

        // Create a URL object
        URL obj = new URL(url);

        // Open a connection
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set request method
        con.setRequestMethod("GET");

        // Get the response code
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Reading response from input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response to get the latest Minecraft version
            String jsonResponse = response.toString();
            // Extracting latest version from JSON response
            String currentVersion = parseJsonForLatestVersion(jsonResponse);
            return currentVersion;
        } else {
            throw new IOException("HTTP response code: " + responseCode);
        }
    }

    private static String parseJsonForLatestVersion(String jsonResponse) {
        // Parse JSON response to get the latest Minecraft version
        String latestVersion = null;
        try {
            // Parsing JSON to get the latest release version
            int index = jsonResponse.indexOf("\"release\":\"");
            if (index != -1) {
                index += 11; // Length of "\"release\":\""
                int endIndex = jsonResponse.indexOf("\"", index);
                if (endIndex != -1) {
                    latestVersion = jsonResponse.substring(index, endIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latestVersion;
    }
}
