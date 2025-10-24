abstract class Beverage {
    public final void prepareRecipe() {
        boilWater();
        brewOrSteep();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        } else {
            System.out.println("No condiments added.");
        }
    }

    protected void boilWater() {
        System.out.println("Boiling water");
    }

    protected abstract void brewOrSteep();
    protected abstract void addCondiments();

    protected boolean customerWantsCondiments() { return true; }

    protected void pourInCup() {
        System.out.println("Pouring into cup");
    }
}

class Tea extends Beverage {
    private final boolean wantCondiments;
    public Tea(boolean wantCondiments) { this.wantCondiments = wantCondiments; }

    protected void brewOrSteep() {
        System.out.println("Steeping the tea");
    }

    protected void addCondiments() {
        System.out.println("Adding lemon");
    }

    protected boolean customerWantsCondiments() {
        return wantCondiments;
    }
}

class Coffee extends Beverage {
    private final boolean wantCondiments;
    public Coffee(boolean wantCondiments) { this.wantCondiments = wantCondiments; }

    protected void brewOrSteep() {
        System.out.println("Dripping coffee through filter");
    }

    protected void addCondiments() {
        System.out.println("Adding sugar and milk");
    }

    protected boolean customerWantsCondiments() {
        return wantCondiments;
    }
}

class HotChocolate extends Beverage {
    private final boolean wantMarshmallows;
    public HotChocolate(boolean wantMarshmallows) { this.wantMarshmallows = wantMarshmallows; }

    protected void brewOrSteep() {
        System.out.println("Mixing chocolate powder");
    }

    protected void addCondiments() {
        if (wantMarshmallows) System.out.println("Adding marshmallows");
        else System.out.println("Adding whipped cream");
    }

    protected boolean customerWantsCondiments() {
        return true; // всегда добавляем что-то
    }
}

public class BeverageDemo {
    public static void main(String[] args) {
        System.out.println("=== Tea with lemon ===");
        Beverage tea = new Tea(true);
        tea.prepareRecipe();

        System.out.println("\n=== Coffee without condiments ===");
        Beverage coffee = new Coffee(false);
        coffee.prepareRecipe();

        System.out.println("\n=== Hot Chocolate (marshmallows) ===");
        Beverage choc = new HotChocolate(true);
        choc.prepareRecipe();
    }
}