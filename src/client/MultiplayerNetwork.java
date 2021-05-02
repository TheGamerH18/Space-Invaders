package client;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Datapackage;
import com.blogspot.debukkitsblog.net.Executable;

import java.net.Socket;
import java.util.List;

public class MultiplayerNetwork extends Client {


    public MultiplayerNetwork(String host, String username) {
        super(host, 25598, 1000, false, username, "player");

        registerMethod("POS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
            }
        });

        registerMethod("PLAYERS", new Executable() {
            @Override
            public void run(Datapackage pack, Socket socket) {
            }
        });
        start();
    }

    }

    }
}
