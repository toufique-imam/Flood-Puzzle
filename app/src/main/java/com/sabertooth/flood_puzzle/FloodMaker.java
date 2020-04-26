package com.sabertooth.flood_puzzle;

import android.util.Log;
import android.util.Pair;

class FloodMaker {
    private int row, col;
    private int[][] grid;
    private int[][] ai_grid;
    private int[][] visited;
    private int[][] same;
    private int[] cnt_map;
    private int ai_cnt = 0;
    private int[] dx = {-1, 1, 0, 0};
    private int[] dy = {0, 0, -1, 1};
    private int distinct_col;

    FloodMaker(int x, int y, int color_cnt) {
        row = x;
        col = y;
        distinct_col = color_cnt;
        cnt_map = new int[color_cnt + 2];
        grid = new int[x][y];
        ai_grid = new int[x][y];
        visited = new int[x][y];
        same = new int[x][y];
        for (int i = 0; i <= color_cnt; i++) cnt_map[i] = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = (int) (Math.random() * distinct_col);
                visited[i][j] = 0;
                same[i][j] = 0;
                cnt_map[grid[i][j]]++;
            }
        }
        same[0][0] = 1;
    }

    private void runDfs(int x, int y, int color, int newColor) {
        cnt_map[grid[x][y]]--;
        visited[x][y] = 1;
        same[x][y] = 1;
        grid[x][y] = newColor;
        cnt_map[grid[x][y]]++;
        for (int i = 0; i < 4; i++) {
            int px = dx[i] + x;
            int py = dy[i] + y;
            if (px > -1 && py > -1 && px < row && py < col && visited[px][py] == 0 && (grid[px][py] == color || grid[px][py] == newColor)) {
                runDfs(px, py, color, newColor);
            }
        }
    }

    void processMove(int color) {
        if (color < 0) return;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                visited[i][j] = 0;
            }
        }
        runDfs(0, 0, grid[0][0], color);
    }

    boolean gameFinished() {
        for (int k = 0; k < row; k++) {
            for (int j = 0; j < col; j++) {
                if (grid[k][j] != grid[0][0]) return false;
            }
        }
        return true;
    }

    void print_grid() {
        StringBuilder x = new StringBuilder();
        x.append("\n");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                x.append(grid[i][j]);
                x.append(" ");
            }
            x.append("\n");
        }
        Log.e("DEBUG", x.toString());
    }

    int count() {
        return row * col;
    }

    private int encode(int x, int y) {
        return x * col + y;
    }

    private Pair<Integer, Integer> decode(int i) {
        return Pair.create(i / col, i % col);
    }

    int colorValue(int i) {
        Pair<Integer, Integer> x = decode(i);
        //Log.e("LOG", x.first + " " + x.second);
        if ((grid[x.first][x.second] >= 8)) throw new AssertionError();
        return grid[x.first][x.second];
    }

    private void greedy_ai_dfs(int x, int y, int color, int newColor) {
        visited[x][y] = 1;
        ai_cnt++;
        ai_grid[x][y] = newColor;

        for (int i = 0; i < 4; i++) {
            int px = dx[i] + x;
            int py = dy[i] + y;
            if (px > -1 && py > -1 && px < row && py < col && visited[px][py] == 0 && (ai_grid[px][py] == color || ai_grid[px][py] == newColor)) {
                greedy_ai_dfs(px, py, color, newColor);
            }
        }
    }

    private int greedy_AI_move() {
        ai_cnt = 0;
        int maxi = 0;
        int colidx = -1;
        for (int i = 0; i < distinct_col; i++) {
            if (i == grid[0][0]) continue;
            if (cnt_map[i] == 0) continue;
            ai_cnt = 0;

            for (int k = 0; k < row; k++) {
                for (int j = 0; j < col; j++) {
                    visited[k][j] = 0;
                    ai_grid[k][j] = grid[k][j];
                }
            }

            greedy_ai_dfs(0, 0, grid[0][0], i);
            Log.e("GREED", i + " " + ai_cnt);
            if (maxi < ai_cnt) {
                maxi = ai_cnt;
                colidx = i;
            }
        }
        return colidx;
    }

    int AI_Move() {
        int idx = greedy_AI_move();
        Log.e("AI MOVE", idx + "");
        if (idx == -1) return idx;
        return idx;
        // runDfs(0,0,grid[0][0],idx);
    }


    int attached(int x) {
        Pair<Integer, Integer> cor = decode(x);
        return same[cor.first][cor.second];
    }
}