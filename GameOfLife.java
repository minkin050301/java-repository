import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GameOfLife {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().replace(">", "").split(" ");
        int size = Integer.parseInt(input[0]);
        //int seed = Integer.parseInt(input[1]);
        //int numberOfGens = Integer.parseInt(input[2]);

        Universe universe = new Universe();
        universe.createUniverse(size);
        //universe.createUniverse(size, seed);
        //universe.showField();
        //System.out.println("====================");
        for (int i = 1; i <= 50; i++) {
            try {
                if (System.getProperty("os.name").contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();                    
                } else {
                    Runtime.getRuntime().exec("clear");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }

            universe.calculateNextGen();
            System.out.println("Generation #" + i);
            System.out.println("Alive: " + universe.getAliveCells());
            universe.showField();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

//        Random random = new Random(seed);
//
//        char[][] field = new char[n][n];
//        for (int i = 0; i < field.length; i++) {
//            for (int j = 0; j < field[i].length; j++) {
//                field[i][j] = random.nextBoolean() ? 'O' : ' ';
//            }
//        }
//        for (int i = 0; i < field.length; i++) {
//            for (int j = 0; j < field[i].length; j++) {
//                System.out.print(field[i][j]);
//            }
//            System.out.println();
//        }
    }
}

class Universe {
    private Cell[][] curGen;

    public void createUniverse(int size, long seed) {
        Random random = new Random(seed);
        curGen = new Cell[size][size];
        for (int i = 0; i < curGen.length; i++) {
            for (int j = 0; j < curGen[i].length; j++) {
                curGen[i][j] = new Cell(random.nextBoolean());
            }
        }
    }

    public void createUniverse(int size) {
        Random random = new Random();
        curGen = new Cell[size][size];
        for (int i = 0; i < curGen.length; i++) {
            for (int j = 0; j < curGen[i].length; j++) {
                curGen[i][j] = new Cell(random.nextBoolean());
            }
        }
    }

    public void showField() {
        for (int i = 0; i < curGen.length; i++) {
            for (int j = 0; j < curGen[i].length; j++) {
                System.out.print(curGen[i][j].isAlive() ? 'O' : ' ');
            }
            System.out.println();
        }
    }

    public void calculateNextGen() {
        curGen = Evolution.processNextGen(curGen);
    }

    public int getAliveCells() {
        int count = 0;
        for (int i = 0; i < curGen.length; i++) {
            for (int j = 0; j < curGen[i].length; j++) {
                if (curGen[i][j].isAlive()) {
                    count++;
                }
            }
        }
        return count;
    }
}

class Evolution {
    public static Cell[][] processNextGen(Cell[][] curGen) {
        Cell[][] nextGen = new Cell[curGen.length][curGen.length];
        for (int i = 0; i < curGen.length; i++) {
            for (int j = 0; j < curGen.length; j++) {

                // calculate the amount of alive neighbors
                int aliveNeighbors = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k == i && l == j) {
                            continue;
                        }
                        int tempK = k;
                        int tempL = l;
                        if (tempK < 0) {
                            tempK = curGen.length - 1;
                        }
                        if (tempL < 0) {
                            tempL = curGen.length - 1;
                        }
                        if (tempK >= curGen.length) {
                            tempK = 0;
                        }
                        if (l >= curGen.length) {
                            tempL = 0;
                        }
                        if (curGen[tempK][tempL].isAlive()) {
                            aliveNeighbors++;
                        }
                    }
                }

                //System.out.println(aliveNeighbors);
                // calculating the state of the current cell in the next generation
                if (curGen[i][j].isAlive()) {
                    if (aliveNeighbors == 2 || aliveNeighbors == 3) {
                        nextGen[i][j] = new Cell(true);
                    } else {
                        nextGen[i][j] = new Cell(false);
                    }
                } else {
                    if (aliveNeighbors == 3) {
                        nextGen[i][j] = new Cell(true);
                    } else {
                        nextGen[i][j] = new Cell(false);
                    }
                }

            }
        }

        return nextGen;
    }
}

class Cell {
    private boolean alive;

    public Cell(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}