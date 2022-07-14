import java.util.*;

public class HorseStep {
    private static int sizeBoard;
    private static int coordinateXStart;
    private static int coordinateYStart;

    public static void sizeBoard(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the size of the board N x N (minimum 5 x 5)");
        do {
            if (sc.hasNextInt()) {
                sizeBoard = Integer.parseInt(sc.next());
                if (sizeBoard < 5) {
                    System.out.println("You entered a number less than 5!");
                }
            }
            else {
                System.out.println("You did enter not a number!");
                sc.next(); //TODO надо переделать через исключения
            }
        }
        while (sizeBoard < 5);
        System.out.printf("Excellent! Board size %d x %d \n", sizeBoard, sizeBoard);
    }
    private static int sizePole;
    private final static int[][] allMovesHorse = {
            {2,1},
            {2,-1},
            {1,2},
            {1,-2},
            {-1,2},
            {-1,-2},
            {-2,1},
            {-2,-1}};
    private static int[][] board;
    private static int totalStep;

    public static void initPole(){
        for (int coordinateX = 0; coordinateX < sizePole; coordinateX++)
            for (int coordinateY = 0; coordinateY < sizePole; coordinateY++)
                if (coordinateX < 2 || coordinateX > sizePole - 3 || coordinateY < 2 || coordinateY > sizePole - 3)
                    board[coordinateX][coordinateY] = -1;

        setCoordinateXStart(2);
        setCoordinateYStart(2);
    }
    public static void solution() {
        sizeBoard();
        sizePole = sizeBoard + 4;// TODO очень кривое и костыльное решение. Необходимо акуратрно ввести валидацию проверки всех полей, куда может пойти конь
        board = new int[sizePole][sizePole];
        totalStep = sizeBoard * sizeBoard;

        initPole();



        board[getCoordinateXStart()][getCoordinateYStart()] = 1;

        if (isHorseStep(getCoordinateXStart(), getCoordinateYStart(), 2))
            printBoard();
        else
            System.out.println("Don`t find solution");

    }

    private static boolean isHorseStep(int coordinateX, int coordinateY, int count) {
        if (count > totalStep)
            return true;

        List<int[]> movesHorse = arrayMovesHorse(coordinateX, coordinateY);

        if (movesHorse.isEmpty() && count != totalStep)
            return false;
        //TODO переделать по рекомендациям из статить на JAvarush через Collections.sort(compare)
        Collections.sort(movesHorse, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[2] - b[2];
            }
        });

        for (int[] item : movesHorse) {
            coordinateX = item[0];
            coordinateY = item[1];
            board[coordinateX][coordinateY] = count;
            if (!deadLock(count, coordinateX, coordinateY) && isHorseStep(coordinateX, coordinateY, count + 1))
                return true;
            board[coordinateX][coordinateY] = 0;
        }

        return false;
    }

    private static List<int[]> arrayMovesHorse(int coordinateX, int coordinateY) {
        List<int[]> moves = new ArrayList<>();

        for (int[] m : allMovesHorse) {
            int x = m[1];
            int y = m[0];
            if (board[coordinateX + x][coordinateY + y] == 0) {
                int num = countMoves(coordinateX + x, coordinateY + y);
                moves.add(new int[]{coordinateX + x, coordinateY + y, num});
            }
        }
        return moves;
    }

    private static int countMoves(int r, int c) {
        int num = 0;
        for (int[] m : allMovesHorse)
            if (board[r + m[1]][c + m[0]] == 0)
                num++;
        return num;
    }

    private static boolean deadLock(int count, int r, int c) {
        if (count < totalStep - 1) {
            List<int[]> movesHorse = arrayMovesHorse(r, c);
            for (int[] item : movesHorse)
                if (countMoves(item[0], item[1]) == 0)
                    return true;
        }
        return false;
    }

    private static void printBoard() {
        for (int[] item : board) {
            for (int i : item) {
                if (i == -1) continue;
                System.out.printf("%2d ", i);
            }
            System.out.println();
        }
    }

    public static int getCoordinateXStart() {
        return coordinateXStart;
    }

    public static void setCoordinateXStart(int coordinateXStart) {
        HorseStep.coordinateXStart = coordinateXStart;
    }

    public static int getCoordinateYStart() {
        return coordinateYStart;
    }

    public static void setCoordinateYStart(int coordinateYStart) {
        HorseStep.coordinateYStart = coordinateYStart;
    }
}