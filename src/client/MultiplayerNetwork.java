package client;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Datapackage;
import com.blogspot.debukkitsblog.net.Executable;

import java.net.Socket;
import java.util.ArrayList;

public class MultiplayerNetwork extends Client {
    int playeramount = 2;

    private int[][] playerpos = new int[playeramount][2];
    private int gameinfo;
    private ArrayList<int[]> shots = new ArrayList<>();
    private ArrayList<int[]> aliens = new ArrayList<>();
    private ArrayList<int[]> bombs = new ArrayList<>();

    public MultiplayerNetwork(String host, String username) {
        super(host, 25598, 1000, false, username, "player");
        setMuted(true);

        registerMethod("GAME_INFO", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                System.out.println("New Game Info");
                gameinfo = (int) pack.get(1);
                if(gameinfo == 2) {
                    stop();
                }
            }
        });

        registerMethod("POS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                playerpos[0][0] = (int) pack.get(1);
                playerpos[0][1] = (int) pack.get(2);
                playerpos[1][0] = (int) pack.get(3);
                playerpos[1][1] = (int) pack.get(4);
            }
        });

        registerMethod("BOMBS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                bombs = (ArrayList<int[]>) pack.get(1);
            }
        });

        registerMethod("ALIENS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                aliens = (ArrayList<int[]>) pack.get(1);
                shots = (ArrayList<int[]>) pack.get(2);
            }
        });

        registerMethod("SHOTS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                shots = (ArrayList<int[]>) pack.get(1);
            }
        });
        start();
    }

    public int[][] getPlayerpos() {
        return playerpos;
    }

    public int getGameinfo() {
        return gameinfo;
    }

    public ArrayList<int[]> getShots() {
        return shots;
    }

    public ArrayList<int[]> getAliens() {
        return aliens;
    }

    public ArrayList<int[]> getBombs() {
        return bombs;
    }
}
