package client;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Datapackage;
import com.blogspot.debukkitsblog.net.Executable;
import sprite.Alien;
import sprite.Player;
import sprite.Shot;

import java.net.Socket;
import java.util.List;

public class MultiplayerNetwork extends Client {

    private Player[] Players;
    private List<Alien> Aliens;
    private List<Shot> Shots;

    public MultiplayerNetwork(String host, String username) {
        super(host, 25598, 1000, false, username, "player");

        registerMethod("POS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                int lastindex = 0;
                for(int i = 0; i < Players.length; i ++) {
                    int[] pos = (int[]) pack.get(i);
                    Players[i].setX(pos[0]);
                    Players[i].setY(pos[1]);
                }
                for(int i = 0; i < Aliens.size(); i ++){
                    int[] pos = (int[]) pack.get(i + 2);
                    Aliens.get(i).setX(pos[0]);
                    Aliens.get(i).setY(pos[1]);
                    lastindex = i + 2;
                }
                Shots = (List<Shot>) pack.get(lastindex + 1);
            }
        });

        registerMethod("PLAYERS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
                Players = (Player[]) pack.get(1);
                Aliens = (List<Alien>) pack.get(2);
                Shots = (List<Shot>) pack.get(3);
            }
        });
        start();
    }

    public Player[] getPlayers() {
        return Players;
    }

    public List<Alien> getAliens() {
        return Aliens;
    }

    public List<Shot> getShots() {
        return Shots;
    }
}
