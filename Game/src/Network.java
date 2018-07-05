
import java.io.*;
import java.net.*;
import java.util.LinkedList;


public class Network {
    public static final int PORT = 10023;
    public static final String HOST = "localhost";
    String str;
    Socket sock;
    String tmp;
    BufferedReader inp;
    PrintWriter outp;
    String stra;

    public Network() throws IOException {

       //System.out.println(get_levels_list());
    }

    String[] get_highscores() throws IOException {
        sock = new Socket(HOST, PORT);
        String[] scores = new String[5];
        // System.out.println("Nawiazalem polaczenie: "+sock);
        // BufferedReader klaw;
        //  klaw=new BufferedReader(new InputStreamReader(System.in));
        inp = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        outp = new PrintWriter(sock.getOutputStream());
        //komunikacja - czytanie danych z klawiatury i przekazywanie ich do strumienia
        String str = "highscores";
        outp.println(str);
        outp.flush();
        for (int i = 0; i < 5; i++) {
            scores[i] = inp.readLine();
            //System.out.println(scores[i]);
        }
        for(int i = 0; i<5; i++){
            outp.flush();
        }
        inp.close();
        outp.close();
        return scores;
    }

    int get_levels_list() throws IOException {
        sock = new Socket(HOST, PORT);
        inp = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        outp = new PrintWriter(sock.getOutputStream());
        //komunikacja - czytanie danych z klawiatury i przekazywanie ich do strumienia
        String str = "level_list_request";
        outp.println(str);
        outp.flush();
        int lev_amount= Integer.parseInt(inp.readLine());
        inp.close();
        outp.close();
        return lev_amount;
    }

    String[] get_level(String namelev) throws IOException {
        sock = new Socket(HOST, PORT);
        String[] level = new String[5];
        // System.out.println("Nawiazalem polaczenie: "+sock);
        // BufferedReader klaw;
        //  klaw=new BufferedReader(new InputStreamReader(System.in));
        inp = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        outp = new PrintWriter(sock.getOutputStream());
        //komunikacja - czytanie danych z klawiatury i przekazywanie ich do strumienia
        String str = "level_request"+' '+namelev;
        outp.println(str);
        outp.flush();
        for (int i = 0; i < 5; i++) {
            level[i] = inp.readLine();

        }
        return level;
    }

    void send_highscores(String nick, int score) throws IOException{
        sock = new Socket(HOST, PORT);
        inp = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        outp = new PrintWriter(sock.getOutputStream());

        String str = "best_request"+' '+nick+' '+score;
        outp.println(str);
        outp.flush();
    }
}
