import java.util.Properties;
import javax.mail.*;

public class MyPOP3Client {
    public static void main(String[] args) {
        // Konfigurasi email account
        String host = "pop.gmail.com"; // Ganti dengan alamat server POP3 Anda
        String username = "rizkydwiyanto2ia11@gmail.com"; // masukin email lo
        String password = "qczqrlbxipcahyqq"; // masukin password lo

        // Mengatur properti untuk protokol POP3
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.ssl.enable", "true");
        
        try {
            // Membuat session dengan properti yang telah dikonfigurasi
            Session session = Session.getDefaultInstance(properties);

            // Membuat store yang terhubung ke server POP3
            Store store = session.getStore();
            store.connect(username, password);

            // Membuka folder Inbox
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Mengambil pesan-pesan dari folder Inbox
            Message[] messages = inbox.getMessages();

            // Menampilkan informasi pesan
            for (int i = 6; i < 10 ; i++) {
                Message message = messages[i];
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Content: " + message.getContent());
                System.out.println("---------------------------------------");
            }

            // Menutup folder dan store
            inbox.close(false);
            store.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}