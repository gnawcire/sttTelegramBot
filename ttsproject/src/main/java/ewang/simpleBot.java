package ewang;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updates.*;
import org.telegram.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.methods.GetFile;


/**
 * Hello world!
 *
 */
public class simpleBot extends TelegramLongPollingBot
{
    private static String fileUrl;
    public static String getFileUrl(){
        return fileUrl;
    }
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // System.out.println(update.getMessage().getText()); //gets the text typed and prints to console
        // System.out.println(update.getMessage().getFrom().getFirstName()); //gets the name of the user who typed it and prints to console

        // String command = update.getMessage().getText();
        // if(command.equals("/test")){
        //     String message = "Run, Forest, Testing";
        //     SendMessage response = new SendMessage();
        //     response.setChatId(update.getMessage().getChatId().toString());
        //     response.setText(message);

        //     try {
        //         execute(response);
        //     } catch (TelegramApiException E) {
        //         E.printStackTrace();
        //     }

        if (update.getMessage().getVoice().getFileId() != null) {
            String fileId = update.getMessage().getVoice().getFileId();
            GetFile getFile = GetFile.builder().fileId(fileId).build();
            try {
                execute(getFile);
            } catch (TelegramApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                System.out.println(execute(getFile).getFilePath());
                String filePath = execute(getFile).getFilePath();
                fileUrl = "https://api.telegram.org/file/bot5486713395:AAGxtC7svpsYZgOfaQ0obX5p7vFyUifvGTo/" + filePath;
            } catch (TelegramApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                botRegister.getTranscript();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText(botRegister.getTranscriptContents());
            try {
                         execute(response);
                } catch (TelegramApiException E) {
                      E.printStackTrace();
                  }
        }
        }

    

    @Override
    public String getBotUsername() {
        // TODO
        return "EricSpeechToTextBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5486713395:AAGxtC7svpsYZgOfaQ0obX5p7vFyUifvGTo";
    }
}
