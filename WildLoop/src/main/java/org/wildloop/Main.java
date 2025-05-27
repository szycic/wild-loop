package org.wildloop;

public class Main {
    public static void main(String[] args) {
        // Inicjalizacja świata
        World world = new World(20, 20);
        
        // Dodawanie drapieżników
        for (int i = 0; i < 5; i++) {
            Position pos = getRandomEmptyPosition(world);
            if (pos != null) {
                world.addAnimal(new Predator(pos, 100, 20));
            }
        }
        
        // Dodawanie ofiar
        for (int i = 0; i < 20; i++) {
            Position pos = getRandomEmptyPosition(world);
            if (pos != null) {
                world.addAnimal(new Prey(pos, 100, 15));
            }
        }
        
        // Główna pętla symulacji
        while (!world.getAnimals().isEmpty()) {
            clearConsole();
            displayWorld(world);
            displayStats(world);
            
            world.tick();
            
            try {
                Thread.sleep(500); // Pół sekundy pauzy między turami
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Symulacja zakończona!");
    }
    
    private static Position getRandomEmptyPosition(World world) {
        for (int attempts = 0; attempts < 100; attempts++) {
            int x = (int) (Math.random() * world.getWidth());
            int y = (int) (Math.random() * world.getHeight());
            Position pos = new Position(x, y);
            if (world.isCellEmpty(pos)) {
                return pos;
            }
        }
        return null;
    }
    
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void displayWorld(World world) {
        // Górna krawędź
        System.out.print("┌");
        for (int x = 0; x < world.getWidth(); x++) {
            System.out.print("─");
        }
        System.out.println("┐");
        
        // Zawartość świata
        for (int y = 0; y < world.getHeight(); y++) {
            System.out.print("│");
            for (int x = 0; x < world.getWidth(); x++) {
                Animal animal = world.getGrid()[x][y];
                if (animal == null) {
                    System.out.print("·");
                } else if (animal instanceof Predator) {
                    System.out.print("P");
                } else if (animal instanceof Prey) {
                    System.out.print("O");
                }
            }
            System.out.println("│");
        }
        
        // Dolna krawędź
        System.out.print("└");
        for (int x = 0; x < world.getWidth(); x++) {
            System.out.print("─");
        }
        System.out.println("┘");
    }
    
    private static void displayStats(World world) {
        int predatorCount = 0;
        int preyCount = 0;
        
        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Predator) {
                predatorCount++;
            } else if (animal instanceof Prey) {
                preyCount++;
            }
        }
        
        System.out.println("Tura: " + world.getTurns());
        System.out.println("Drapieżniki (P): " + predatorCount);
        System.out.println("Ofiary (O): " + preyCount);
        System.out.println("Łącznie zwierząt: " + world.getAnimals().size());
    }
}