package ewang;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;


public class botRegister {
    private static String transcriptContents;
    public static String getTranscriptContents(){
        return transcriptContents;
    }
    public static void main(String [] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new simpleBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    
    }
    public static String getTranscript() throws Exception{
        Transcript transcript = new Transcript();
        transcript.setAudio_url(simpleBot.getFileUrl());
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);
        System.out.println(jsonRequest);

        HttpRequest postRequest =  HttpRequest.newBuilder()
            .uri(URI.create("https://api.assemblyai.com/v2/transcript"))
            .header("Authorization", "0e1f6e546a36457793e98278149f444d")
            .POST(BodyPublishers.ofString(jsonRequest))
            .build();
        
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());

        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        
        System.out.println(transcript.getId());

        HttpRequest getRequest =  HttpRequest.newBuilder()
            .uri(URI.create("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
            .header("Authorization", "0e1f6e546a36457793e98278149f444d")
            .GET()
            .build();
        
        while(true){
        HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
        transcript = gson.fromJson(getResponse.body(), Transcript.class);
        System.out.println(transcript.getStatus());
        if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())){
            break;
        }
        Thread.sleep(1000);
        }
        System.out.println("Transcription Complete.");
        System.out.println(transcript.getText());
        transcriptContents = transcript.getText();
        return transcript.getText();
    }
}
