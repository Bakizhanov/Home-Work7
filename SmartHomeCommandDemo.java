import java.util.Stack;

interface ICommand {
    void execute();
    void undo();
}

class Light {
    private final String location;
    private boolean on = false;
    public Light(String location) { this.location = location; }
    public void on() { on = true; System.out.println(location + " light is ON"); }
    public void off() { on = false; System.out.println(location + " light is OFF"); }
    public boolean isOn() { return on; }
}

class Door {
    private final String name;
    private boolean open = false;
    public Door(String name) { this.name = name; }
    public void open() { open = true; System.out.println(name + " is OPEN"); }
    public void close() { open = false; System.out.println(name + " is CLOSED"); }
    public boolean isOpen() { return open; }
}

class Thermostat {
    private int temperature;
    public Thermostat(int initial) { this.temperature = initial; }
    public void setTemperature(int t) { System.out.println("Thermostat: " + temperature + " -> " + t); temperature = t; }
    public int getTemperature() { return temperature; }
}

class TV {
    private boolean on = false;
    public void on() { on = true; System.out.println("TV is ON"); }
    public void off() { on = false; System.out.println("TV is OFF"); }
    public boolean isOn() { return on; }
}

class Alarm {
    private boolean armed = false;
    public void arm() { armed = true; System.out.println("Alarm ARMED"); }
    public void disarm() { armed = false; System.out.println("Alarm DISARMED"); }
    public boolean isArmed() { return armed; }
}

class LightOnCommand implements ICommand {
    private final Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements ICommand {
    private final Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class DoorOpenCommand implements ICommand {
    private final Door door;
    public DoorOpenCommand(Door door) { this.door = door; }
    public void execute() { door.open(); }
    public void undo() { door.close(); }
}

class DoorCloseCommand implements ICommand {
    private final Door door;
    public DoorCloseCommand(Door door) { this.door = door; }
    public void execute() { door.close(); }
    public void undo() { door.open(); }
}

class IncreaseTempCommand implements ICommand {
    private final Thermostat thermostat;
    private final int delta;
    private int prev;
    public IncreaseTempCommand(Thermostat thermostat, int delta) { this.thermostat = thermostat; this.delta = delta; }
    public void execute() {
        prev = thermostat.getTemperature();
        thermostat.setTemperature(prev + delta);
    }
    public void undo() { thermostat.setTemperature(prev); }
}

class DecreaseTempCommand implements ICommand {
    private final Thermostat thermostat;
    private final int delta;
    private int prev;
    public DecreaseTempCommand(Thermostat thermostat, int delta) { this.thermostat = thermostat; this.delta = delta; }
    public void execute() {
        prev = thermostat.getTemperature();
        thermostat.setTemperature(prev - delta);
    }
    public void undo() { thermostat.setTemperature(prev); }
}

class TVToggleCommand implements ICommand {
    private final TV tv;
    private boolean prev;
    public TVToggleCommand(TV tv) { this.tv = tv; }
    public void execute() { prev = tv.isOn(); if (!prev) tv.on(); else tv.off(); }
    public void undo() { if (prev) tv.on(); else tv.off(); }
}

class AlarmToggleCommand implements ICommand {
    private final Alarm alarm;
    private boolean prev;
    public AlarmToggleCommand(Alarm alarm) { this.alarm = alarm; }
    public void execute() { prev = alarm.isArmed(); if (!prev) alarm.arm(); else alarm.disarm(); }
    public void undo() { if (prev) alarm.arm(); else alarm.disarm(); }
}

class Invoker {
    private final Stack<ICommand> history = new Stack<>();
    public void executeCommand(ICommand command) {
        command.execute();
        history.push(command);
    }
    public void undoLast() {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }
        ICommand last = history.pop();
        last.undo();
    }
    public void undoMultiple(int n) {
        if (n <= 0) { System.out.println("Specify positive number of commands to undo."); return; }
        for (int i = 0; i < n; i++) {
            if (history.isEmpty()) {
                System.out.println("No more commands to undo.");
                return;
            }
            undoLast();
        }
    }
    public int historySize() { return history.size(); }
}

public class SmartHomeCommandDemo {
    public static void main(String[] args) {
        Light living = new Light("LivingRoom");
        Door front = new Door("FrontDoor");
        Thermostat t = new Thermostat(20);
        TV tv = new TV();
        Alarm alarm = new Alarm();

        Invoker inv = new Invoker();

        inv.executeCommand(new LightOnCommand(living));
        inv.executeCommand(new IncreaseTempCommand(t, 3));
        inv.executeCommand(new TVToggleCommand(tv));
        inv.executeCommand(new AlarmToggleCommand(alarm));
        inv.executeCommand(new DoorOpenCommand(front));

        System.out.println("\n-- Undo last two commands --");
        inv.undoMultiple(2);

        System.out.println("\n-- Undo many (including none left) --");
        inv.undoMultiple(10);

        System.out.println("\n-- Attempt single undo (should be none) --");
        inv.undoLast();
    }
}