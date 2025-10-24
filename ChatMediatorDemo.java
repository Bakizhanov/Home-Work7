import java.util.*;

interface IMediator {
    void registerUser(User user);
    void removeUser(User user);
    void sendMessage(User from, String message); // broadcast
    void sendPrivateMessage(User from, String toUsername, String message) throws Exception;
}

class ChatRoom implements IMediator {
    private final Map<String, User> users = new LinkedHashMap<>();

    public void registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            System.out.println("User with name " + user.getUsername() + " already in chat.");
            return;
        }
        users.put(user.getUsername(), user);
        user.setMediator(this);
        broadcastSystemMessage(user.getUsername() + " joined the chat.");
    }

    public void removeUser(User user) {
        if (users.remove(user.getUsername()) != null) {
            user.setMediator(null);
            broadcastSystemMessage(user.getUsername() + " left the chat.");
        } else {
            System.out.println("User " + user.getUsername() + " not found in chat.");
        }
    }

    public void sendMessage(User from, String message) {
        if (!isRegistered(from)) {
            System.out.println("Error: " + from.getUsername() + " is not part of the chat.");
            return;
        }
        for (User u : users.values()) {
            if (!u.getUsername().equals(from.getUsername())) {
                u.receive(from.getUsername(), message);
            }
        }
    }

    public void sendPrivateMessage(User from, String toUsername, String message) throws Exception {
        if (!isRegistered(from)) throw new Exception("Sender not in chat");
        User to = users.get(toUsername);
        if (to == null) throw new Exception("Recipient '" + toUsername + "' not found in chat");
        to.receivePrivate(from.getUsername(), message);
    }

    private boolean isRegistered(User u) {
        return users.containsKey(u.getUsername());
    }

    private void broadcastSystemMessage(String msg) {
        for (User u : users.values()) {
            u.receiveSystem(msg);
        }
    }
}

class User {
    private final String username;
    private IMediator mediator;

    public User(String username) { this.username = username; }

    public String getUsername() { return username; }
    public void setMediator(IMediator m) { this.mediator = m; }

    public void send(String message) {
        if (mediator == null) {
            System.out.println("Cannot send — " + username + " is not in any chat.");
            return;
        }
        mediator.sendMessage(this, message);
    }

    public void sendPrivate(String toUsername, String message) {
        if (mediator == null) {
            System.out.println("Cannot send private — " + username + " is not in any chat.");
            return;
        }
        try {
            mediator.sendPrivateMessage(this, toUsername, message);
        } catch (Exception e) {
            System.out.println("Private message error: " + e.getMessage());
        }
    }

    public void receive(String fromUsername, String message) {
        System.out.println("[" + username + "] " + fromUsername + ": " + message);
    }

    public void receivePrivate(String fromUsername, String message) {
        System.out.println("[" + username + "] (private) " + fromUsername + ": " + message);
    }

    public void receiveSystem(String message) {
        System.out.println("[" + username + "] (system): " + message);
    }
}

public class ChatMediatorDemo {
    public static void main(String[] args) {
        ChatRoom chat = new ChatRoom();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User carol = new User("Carol");

        chat.registerUser(alice);
        chat.registerUser(bob);
        chat.registerUser(carol);

        alice.send("Hello everyone!");
        bob.send("Hi Alice!");

        carol.sendPrivate("Alice", "Hey Alice, can you help me?");

        bob.sendPrivate("Zed", "Are you there?");

        chat.removeUser(bob);
        alice.send("Bob left?");

        bob.send("I shouldn't be able to send this");
    }
}