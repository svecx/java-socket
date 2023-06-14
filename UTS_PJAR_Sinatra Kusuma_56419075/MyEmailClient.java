import java.util.Properties;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;

public class MyEmailClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Mengambil input dari pengguna
            System.out.print("Masukkan alamat server SMTP: "); // smtp.gmail.com
            String smtpHost = scanner.nextLine();

            System.out.print("Masukkan alamat server POP3: "); // pop.gmail.com
            String pop3Host = scanner.nextLine();

            System.out.print("Masukkan alamat email: "); // masukan email
            String username = scanner.nextLine();

            System.out.print("Masukkan password: "); // masukan password
            String password = scanner.nextLine();
            

            // Konfigurasi properti untuk protokol SMTP
            Properties smtpProperties = new Properties();
            smtpProperties.put("mail.smtp.host", smtpHost);
            smtpProperties.put("mail.smtp.port", "587");
            smtpProperties.put("mail.smtp.auth", "true");
            smtpProperties.put("mail.smtp.starttls.enable", "true");

            // Konfigurasi properti untuk protokol POP3
            Properties pop3Properties = new Properties();
            pop3Properties.put("mail.pop3.host", pop3Host);
            pop3Properties.put("mail.pop3.port", "995");
            pop3Properties.put("mail.store.protocol", "pop3");
            pop3Properties.put("mail.pop3.ssl.enable", "true");

            try {
                // Membuat session untuk protokol SMTP
                Session smtpSession = Session.getInstance(smtpProperties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                // Membuat session untuk protokol POP3
                Session pop3Session = Session.getDefaultInstance(pop3Properties);
                Store store = pop3Session.getStore();
                store.connect(username, password);

                boolean exit = false;

                while (!exit) {
                    // Menampilkan menu aplikasi
                    System.out.println("\nPilih menu:");
                    System.out.println("1. List semua pesan");
                    System.out.println("2. Baca pesan tertentu");
                    System.out.println("3. Tulis dan kirim email");
                    System.out.println("4. Keluar");

                    System.out.print("Masukkan pilihan Anda: ");
                    int choice = scanner.nextInt();

                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
            
                    // Mengambil pesan-pesan dari folder Inbox
                    Message[] messages = inbox.getMessages();

                    switch (choice) {
                        case 1:
            
                        // Menampilkan informasi pesan
                        for (int i = 6; i < 10 ; i++) {
                            Message message = messages[i];
                            System.out.println("Subject: " + message.getSubject());
                            System.out.println("From: " + message.getFrom()[0]);
                            System.out.println("Content: " + message.getContent());
                            System.out.println("---------------------------------------");
                        }
                            break;
                        case 2:
                            System.out.print("Masukkan nomor pesan yang ingin Anda baca: ");
                            int messageNumber = scanner.nextInt();

                            Message message = messages[messageNumber];
                            System.out.println("Subject: " + message.getSubject());
                            System.out.println("From: " + message.getFrom()[0]);
                            System.out.println("Content: " + message.getContent());
                            System.out.println("---------------------------------------");

                            break;
                        case 3:
                            scanner.nextLine(); // Membuang newline dari pilihan sebelumnya
                            System.out.print("Masukkan alamat email penerima: ");
                            String recipient = scanner.nextLine();

                            System.out.print("Masukkan subjek email: ");
                            String subject = scanner.nextLine();

                            System.out.print("Masukkan isi email: ");
                            String content = scanner.nextLine();

                            sendMessage(smtpSession, username, recipient, subject, content);
                            break;
                        case 4:
                            inbox.close(false);
                            store.close();
                            exit = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid.");
                            break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void sendMessage(Session session, String from, String to, String subject, String content) throws Exception {
        // Membuat objek MimeMessage baru
        MimeMessage message = new MimeMessage(session);

        // Menambahkan informasi penerima, pengirim, subjek, dan konten email
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(content);

        // Mengirim email
        Transport.send(message);
        System.out.println("Email terkirim!");
    }
}