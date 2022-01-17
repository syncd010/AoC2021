package src.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class day25 {
    public static class Pair<S, T> {
        S first;
        T second;
        public Pair(S first, T second) {
            this.first = first;
            this.second = second;
        }
    }

	public static void main(String[] args) {
		BufferedReader reader;
        ArrayList<char[]> contents = new ArrayList<char[]>();
        long startTime = System.currentTimeMillis();
        String filename = (args.length > 0) ? args[0] : "resources/input25";

		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
                contents.add(line.toCharArray());
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        char[][] input = contents.toArray(new char[0][0]);
        Pair<Integer, char[][]> res = evolve(input);
        long stopTime = System.currentTimeMillis();
        //print_matrix(res.second);
        System.out.println("Part one: " + res.first);
        System.out.println("Elapsed time: " + (stopTime - startTime) + " ms.");
	}

    public static void print_matrix(char[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col]);
            }
            System.out.print('\n');
        }
    }

    public static Pair<Integer, char[][]> evolve_direction(char[][] matrix, char dir) {
        int height = matrix.length, 
            width = (height > 0)? matrix[0].length : 0,
            moves = 0;

        char[][] new_matrix = new char[height][width];
        for (int row = 0; row < height; row++) {
            new_matrix[row] = new char[width];
            for (int col = 0; col < width; col++) {
                new_matrix[row][col] = matrix[row][col];
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (matrix[row][col] == dir) {
                    int next_row = (dir == 'v') ? (row + 1) % height : row,
                        next_col = (dir == '>') ? (col + 1) % width : col;
                    if (matrix[next_row][next_col] == '.') {
                        new_matrix[row][col] = '.';
                        new_matrix[next_row][next_col] = dir;
                        moves++;
                    } else {
                        new_matrix[row][col] = dir;
                    }
                }
            }
        }
        return new Pair<Integer, char[][]>(moves, new_matrix);
    }

    public static Pair<Integer, char[][]> evolve(char[][] matrix) {
        int steps = 0;
        Pair<Integer, char[][]> res_east, res_south;
        do {
            res_east = evolve_direction(matrix, '>');
            res_south = evolve_direction(res_east.second, 'v');
            matrix = res_south.second;
            steps++;
        } while (res_east.first + res_south.first > 0); // (steps < 59);
        return new Pair<Integer, char[][]>(steps, matrix);
    }
}
