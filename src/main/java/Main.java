package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {

    private static final Path DEFAULT_INPUT = Paths.get("src", "main", "test", "TestCase.txt");
    private static final int[] ROW_CHANGE = {-1, 1, 0, 0};
    private static final int[] COLUMN_CHANGE = {0, 0, -1, 1};
    private static final String[] MOVE_NAME = {"UP", "DOWN", "LEFT", "RIGHT"};

    private static int rows;
    private static int columns;
    private static boolean[] walls;
    private static char[] targets;
    private static int finalPlayerPosition;

    public static void main(String[] args) {
        try {
            List<String> lines = readGrid(args);
            Puzzle puzzle = parseGrid(lines);
            List<String> moves = solve(puzzle);

            if (moves == null) {
                System.out.println("IMPOSSIBLE");
            } else if (moves.isEmpty()) {
                System.out.println("[]");
            } else {
                System.out.println(String.join(" ", moves));
            }
        } catch (IllegalArgumentException | IOException exception) {
            System.out.println("INVALID INPUT: " + exception.getMessage());
        }
    }

    private static List<String> readGrid(String[] args) throws IOException {
        List<String> input;

        if (args.length > 0) {
            input = Files.readAllLines(Paths.get(args[0]));
        } else if (Files.isRegularFile(DEFAULT_INPUT)) {
            input = Files.readAllLines(DEFAULT_INPUT);
        } else {
            input = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    input.add(line);
                }
            }
        }

        List<String> nonBlankLines = new ArrayList<>();
        for (String line : input) {
            if (!line.isBlank()) {
                nonBlankLines.add(line.trim());
            }
        }
        return nonBlankLines;
    }

    private static Puzzle parseGrid(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("the grid is empty");
        }

        String[][] cells = new String[lines.size()][];
        for (int row = 0; row < lines.size(); row++) {
            cells[row] = lines.get(row).split("\\s+");
        }

        rows = cells.length;
        columns = cells[0].length;
        if (columns == 0) {
            throw new IllegalArgumentException("the grid has no columns");
        }
        for (String[] row : cells) {
            if (row.length != columns) {
                throw new IllegalArgumentException("all grid rows must have the same number of cells");
            }
        }

        int cellCount = rows * columns;
        walls = new boolean[cellCount];
        targets = new char[cellCount];
        char[] tiles = new char[cellCount];
        int playerPosition = -1;
        finalPlayerPosition = -1;
        int tileCount = 0;
        int targetCount = 0;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int position = row * columns + column;
                String cell = cells[row][column];

                if (cell.equals("W")) {
                    walls[position] = true;
                    continue;
                }
                if (cell.equals(".")) {
                    continue;
                }

                boolean hasTile = false;
                boolean hasTarget = false;
                for (int i = 0; i < cell.length(); i++) {
                    char symbol = cell.charAt(i);
                    if (symbol >= 'a' && symbol <= 'e') {
                        if (hasTile) {
                            throw invalidCell(row, column, cell);
                        }
                        tiles[position] = symbol;
                        hasTile = true;
                        tileCount++;
                    } else if (symbol >= 'A' && symbol <= 'D') {
                        if (hasTarget) {
                            throw invalidCell(row, column, cell);
                        }
                        targets[position] = symbol;
                        hasTarget = true;
                        targetCount++;
                    } else if (symbol == 'X') {
                        if (playerPosition != -1) {
                            throw new IllegalArgumentException("the grid must contain exactly one X");
                        }
                        playerPosition = position;
                    } else if (symbol == 'Y') {
                        if (finalPlayerPosition != -1) {
                            throw new IllegalArgumentException("the grid must contain exactly one Y");
                        }
                        finalPlayerPosition = position;
                    } else {
                        throw invalidCell(row, column, cell);
                    }
                }

                if (hasTile && playerPosition == position) {
                    throw new IllegalArgumentException("X cannot share a cell with a tile");
                }
            }
        }

        if (playerPosition == -1 || finalPlayerPosition == -1) {
            throw new IllegalArgumentException("the grid must contain exactly one X and one Y");
        }
        if (tileCount != targetCount) {
            throw new IllegalArgumentException("the numbers of tiles and targets must be equal");
        }

        return new Puzzle(playerPosition, tiles);
    }

    private static IllegalArgumentException invalidCell(int row, int column, String cell) {
        return new IllegalArgumentException(
                "invalid cell '" + cell + "' at row " + (row + 1) + ", column " + (column + 1));
    }

    private static List<String> solve(Puzzle puzzle) {
        boolean[][] canReachCompatibleTarget = buildTileReachability();
        PriorityQueue<SearchState> queue = new PriorityQueue<>(Comparator.comparingInt(state -> state.distance));
        Map<String, SearchState> bestState = new HashMap<>();

        String initialKey = stateKey(puzzle.playerPosition, puzzle.tiles);
        SearchState initial = new SearchState(
                initialKey, puzzle.playerPosition, puzzle.tiles, 0, null, new byte[0]);
        queue.add(initial);
        bestState.put(initialKey, initial);

        int bestGoalDistance = Integer.MAX_VALUE;
        SearchState bestGoalState = null;
        byte[] bestFinalWalk = null;

        while (!queue.isEmpty()) {
            SearchState current = queue.remove();
            if (bestState.get(current.key) != current) {
                continue;
            }
            if (current.distance >= bestGoalDistance) {
                break;
            }

            WalkMap walkMap = findWalks(current.playerPosition, current.tiles);
            if (tilesAreSolved(current.tiles) && walkMap.distance[finalPlayerPosition] != -1) {
                int goalDistance = current.distance + walkMap.distance[finalPlayerPosition];
                if (goalDistance < bestGoalDistance) {
                    bestGoalDistance = goalDistance;
                    bestGoalState = current;
                    bestFinalWalk = walkingMoves(walkMap, finalPlayerPosition, null);
                }
                continue;
            }

            for (int tilePosition = 0; tilePosition < current.tiles.length; tilePosition++) {
                char tile = current.tiles[tilePosition];
                if (tile == 0) {
                    continue;
                }

                int tileRow = tilePosition / columns;
                int tileColumn = tilePosition % columns;
                for (byte pushDirection = 0; pushDirection < MOVE_NAME.length; pushDirection++) {
                    int destinationRow = tileRow + ROW_CHANGE[pushDirection];
                    int destinationColumn = tileColumn + COLUMN_CHANGE[pushDirection];
                    int standingRow = tileRow - ROW_CHANGE[pushDirection];
                    int standingColumn = tileColumn - COLUMN_CHANGE[pushDirection];

                    if (!isOpenCell(destinationRow, destinationColumn)
                            || !isOpenCell(standingRow, standingColumn)) {
                        continue;
                    }

                    int destination = destinationRow * columns + destinationColumn;
                    int standingPosition = standingRow * columns + standingColumn;
                    if (current.tiles[destination] != 0 || walkMap.distance[standingPosition] == -1) {
                        continue;
                    }
                    if (!canReachCompatibleTarget[tile - 'a'][destination]) {
                        continue;
                    }

                    byte[] transitionMoves = walkingMoves(walkMap, standingPosition, pushDirection);
                    int nextDistance = current.distance + transitionMoves.length;
                    if (nextDistance >= bestGoalDistance) {
                        continue;
                    }

                    char[] nextTiles = current.tiles.clone();
                    nextTiles[destination] = tile;
                    nextTiles[tilePosition] = 0;
                    int nextPlayerPosition = tilePosition;
                    String nextKey = stateKey(nextPlayerPosition, nextTiles);

                    SearchState previousBest = bestState.get(nextKey);
                    if (previousBest != null && previousBest.distance <= nextDistance) {
                        continue;
                    }

                    SearchState next = new SearchState(
                            nextKey,
                            nextPlayerPosition,
                            nextTiles,
                            nextDistance,
                            current,
                            transitionMoves);
                    bestState.put(nextKey, next);
                    queue.add(next);
                }
            }
        }

        if (bestGoalState == null) {
            return null;
        }
        return reconstructMoves(bestGoalState, bestFinalWalk, bestGoalDistance);
    }

    private static boolean isOpenCell(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            return false;
        }
        return !walls[row * columns + column];
    }

    private static boolean tilesAreSolved(char[] tiles) {
        for (int position = 0; position < targets.length; position++) {
            char target = targets[position];
            char tile = tiles[position];

            if (target == 0) {
                if (tile != 0) {
                    return false;
                }
            } else if (tile != 'e' && tile != Character.toLowerCase(target)) {
                return false;
            }
        }
        return true;
    }

    private static String stateKey(int playerPosition, char[] tiles) {
        return playerPosition + ":" + new String(tiles);
    }

    private static boolean[][] buildTileReachability() {
        boolean[][] reachable = new boolean[5][targets.length];

        for (int tileType = 0; tileType < reachable.length; tileType++) {
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            for (int position = 0; position < targets.length; position++) {
                char target = targets[position];
                if (target != 0 && (tileType == 4 || target - 'A' == tileType)) {
                    reachable[tileType][position] = true;
                    queue.addLast(position);
                }
            }

            while (!queue.isEmpty()) {
                int current = queue.removeFirst();
                int currentRow = current / columns;
                int currentColumn = current % columns;

                for (int direction = 0; direction < MOVE_NAME.length; direction++) {
                    int previousRow = currentRow - ROW_CHANGE[direction];
                    int previousColumn = currentColumn - COLUMN_CHANGE[direction];
                    int standingRow = previousRow - ROW_CHANGE[direction];
                    int standingColumn = previousColumn - COLUMN_CHANGE[direction];
                    if (!isOpenCell(previousRow, previousColumn)
                            || !isOpenCell(standingRow, standingColumn)) {
                        continue;
                    }

                    int previous = previousRow * columns + previousColumn;
                    if (!reachable[tileType][previous]) {
                        reachable[tileType][previous] = true;
                        queue.addLast(previous);
                    }
                }
            }
        }
        return reachable;
    }

    private static WalkMap findWalks(int start, char[] tiles) {
        int[] distance = new int[tiles.length];
        int[] previous = new int[tiles.length];
        byte[] move = new byte[tiles.length];
        Arrays.fill(distance, -1);
        Arrays.fill(previous, -1);

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        distance[start] = 0;
        queue.addLast(start);

        while (!queue.isEmpty()) {
            int current = queue.removeFirst();
            int currentRow = current / columns;
            int currentColumn = current % columns;

            for (byte direction = 0; direction < MOVE_NAME.length; direction++) {
                int nextRow = currentRow + ROW_CHANGE[direction];
                int nextColumn = currentColumn + COLUMN_CHANGE[direction];
                if (!isOpenCell(nextRow, nextColumn)) {
                    continue;
                }

                int next = nextRow * columns + nextColumn;
                if (tiles[next] != 0 || distance[next] != -1) {
                    continue;
                }

                distance[next] = distance[current] + 1;
                previous[next] = current;
                move[next] = direction;
                queue.addLast(next);
            }
        }
        return new WalkMap(distance, previous, move);
    }

    private static byte[] walkingMoves(WalkMap walkMap, int destination, Byte finalPush) {
        int walkingDistance = walkMap.distance[destination];
        int pushCount = finalPush == null ? 0 : 1;
        byte[] moves = new byte[walkingDistance + pushCount];
        int current = destination;

        for (int index = walkingDistance - 1; index >= 0; index--) {
            moves[index] = walkMap.move[current];
            current = walkMap.previous[current];
        }
        if (finalPush != null) {
            moves[moves.length - 1] = finalPush;
        }
        return moves;
    }

    private static List<String> reconstructMoves(
            SearchState goalState, byte[] finalWalk, int totalDistance) {
        ArrayDeque<byte[]> segments = new ArrayDeque<>();
        SearchState current = goalState;
        while (current.parent != null) {
            segments.addFirst(current.movesFromParent);
            current = current.parent;
        }

        List<String> result = new ArrayList<>(totalDistance);
        for (byte[] segment : segments) {
            for (byte move : segment) {
                result.add(MOVE_NAME[move]);
            }
        }
        for (byte move : finalWalk) {
            result.add(MOVE_NAME[move]);
        }
        return result;
    }

    private static final class Puzzle {
        private final int playerPosition;
        private final char[] tiles;

        private Puzzle(int playerPosition, char[] tiles) {
            this.playerPosition = playerPosition;
            this.tiles = tiles;
        }
    }

    private static final class SearchState {
        private final String key;
        private final int playerPosition;
        private final char[] tiles;
        private final int distance;
        private final SearchState parent;
        private final byte[] movesFromParent;

        private SearchState(
                String key,
                int playerPosition,
                char[] tiles,
                int distance,
                SearchState parent,
                byte[] movesFromParent) {
            this.key = key;
            this.playerPosition = playerPosition;
            this.tiles = tiles;
            this.distance = distance;
            this.parent = parent;
            this.movesFromParent = movesFromParent;
        }
    }

    private static final class WalkMap {
        private final int[] distance;
        private final int[] previous;
        private final byte[] move;

        private WalkMap(int[] distance, int[] previous, byte[] move) {
            this.distance = distance;
            this.previous = previous;
            this.move = move;
        }
    }
}
