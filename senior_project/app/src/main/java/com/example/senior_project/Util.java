package com.example.senior_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    //SEND QUERY TO CHATGPT
    public static String queryGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "sk-proj-zYJnyxsffYcurMpXd2NiT3BlbkFJ65bNp9BFabBMCgI1isXI";
        Log.d("success", apiKey);
        String model = "gpt-4";
        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{" +
                    "\"model\": \"" + model + "\", " +
                    "\"messages\": " + "[" +
                    "{" +
                        "\"role\": \"user\", " +
                        "\"content\": \"" + message + "\"" +
                    "}]" +
                    "}";

            Log.d("success", body);
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // returns the extracted contents of the response.
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //PARSE AND EXTRACT RESPONSE FROM GPT
    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        int endMarker = response.indexOf("}\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }

    //CREATE NEW MODULAR INPUT DIALOG
    public static void newInputDialog(Context c, Button b, Listener l, String title, String positive, String negative) {
        b.setOnClickListener(view -> {
            AlertDialog.Builder newDialog = new AlertDialog.Builder(c);
            newDialog.setTitle(title);

            final EditText input = new EditText(c);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            newDialog.setView(input);
            newDialog.setPositiveButton(positive, (dialogInterface, i) -> {
                //MAKE POSITIVE BRANCH INTO FUNCTION THAT EXECUTES DEPENDING ON CONTEXT
                final String myText = input.getText().toString();
                //Modularize Toast Message
                Toast.makeText(c, "Generating New Recipe, Please Wait...", Toast.LENGTH_LONG).show();
                ///SEND RECIPE NAME TO QUERY
                l.onNameInput(myText, c);
                //l -> (myText, c);
            });
            newDialog.setNegativeButton(negative, (dialogInterface, i) -> dialogInterface.cancel());
            newDialog.show();
        });
    }

    //SAVE FILE
    public static void saveFile(Context c, String fileDir, String fileName, String fileContents) {
        File dir = new File(c.getFilesDir(), fileDir);
        if (!dir.exists()) dir.mkdir();
        try {
            File file = new File(dir, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(String.format(fileContents.concat("}")));
            writer.flush();
            writer.close();
            //Toast.makeText().show();

        } catch (IOException e) {
            Log.d("success", "file NOT saved");
        }
    }
}
