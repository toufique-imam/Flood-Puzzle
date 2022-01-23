package com.sabertooth.flood_puzzle;

import android.util.Log;
import android.util.Pair;

class FloodMaker {
    private final int row;
    private final int col;
    private final int[][] grid;
    private final int[][] ai_grid;
    private final int[][] visited;
    private final int[][] same;
    private final int[] cnt_map;
    private int ai_cnt = 0;
    private final int[] dx = {-1, 1, 0, 0};
    private final int[] dy = {0, 0, -1, 1};
    private final int distinct_col;

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

    private void ai_dfs(int x, int y, int color, int newColor) {
        visited[x][y] = 1;
        ai_cnt++;
        ai_grid[x][y] = newColor;

        for (int i = 0; i < 4; i++) {
            int px = dx[i] + x;
            int py = dy[i] + y;
            if (px > -1 && py > -1 && px < row && py < col && visited[px][py] == 0 && (ai_grid[px][py] == color || ai_grid[px][py] == newColor)) {
                ai_dfs(px, py, color, newColor);
            }
        }
    }

    /*
    Strategy: Make the game hard for user , don't create optimal move , basically other will have to create an
    optimal move and lead the game to win , when it's close to win , grab it!
    Select a color that doesn't
    1.completely remove another color
    2.Doesn't make a whole row/col same color
    3.Otherwise use the smallest move
     */
    private int AI_move(boolean goodAi) {
        ai_cnt = 0;
        int[] mini = new int[3];
        if (!goodAi) {
            mini[0] = (int) -1e9;
            mini[1] = (int) -1e9;
            mini[2] = (int) -1e9;
        }
        int[] idx = new int[]{-1, -1, -1};
        for (int i = 0; i < distinct_col; i++) {
            if (i == grid[0][0]) continue;
            if (cnt_map[i] == 0) continue;
            ai_cnt = 0;
            int colorSetBefore = 0;
            int rowSetBefore = 0;
            int colSetBefore = 0;

            for (int k = 0; k < row; k++) {
                for (int j = 0; j < col; j++) {
                    visited[k][j] = 0;
                    int color_ij = grid[k][j];
                    ai_grid[k][j] = color_ij;
                    colorSetBefore = colorSetBefore | (1 << color_ij); //setting 1 if this color exists
                    if (ai_grid[k][0] != color_ij) {
                        rowSetBefore = rowSetBefore | (1 << k); // setting 1 if color in kth row arent same
                    }
                }
            }
            for (int k = 0; k < col; k++) {
                for (int j = 0; j < row; j++) {
                    int color_ij = ai_grid[j][k];
                    if (ai_grid[0][k] != color_ij) {
                        colSetBefore = colSetBefore | (1 << k); //setting 1 if kth col arent same
                    }
                }
            }


            ai_dfs(0, 0, grid[0][0], i);
            int colorSetAfter = 0;
            int rowSetAfter = 0;
            int colSetAfter = 0;
            for (int k = 0; k < row; k++) {
                for (int j = 0; j < col; j++) {
                    int color_ij = ai_grid[k][j];
                    colorSetAfter = colorSetAfter | (1 << color_ij);
                    if (ai_grid[k][0] != color_ij) {
                        rowSetAfter = rowSetAfter | (1 << k); // setting 1 if color in kth row arent same
                    }
                }
            }
            for (int k = 0; k < col; k++) {
                for (int j = 0; j < row; j++) {
                    int color_ij = ai_grid[j][k];
                    if (ai_grid[0][k] != color_ij) {
                        colSetAfter = colSetAfter | (1 << k); //setting 1 if kth col arent same
                    }
                }
            }

            Log.e("colorSet", colorSetBefore + " " + colorSetAfter);
            Log.e("RowSet", rowSetBefore + " " + rowSetAfter);
            Log.e("ColSet", colSetBefore + " " + colorSetAfter);
            Log.e("min_GREED", i + " " + ai_cnt);
            if (!goodAi) {
                if (colorSetAfter == colorSetBefore) {
                    //no color was completely deleted
                    if (mini[0] > ai_cnt) {
                        mini[0] = ai_cnt;
                        idx[0] = i;
                    }
                }
                if (rowSetBefore == rowSetAfter) {
                    //no row was completely became same color
                    if (mini[1] > ai_cnt) {
                        mini[1] = ai_cnt;
                        idx[1] = i;
                    }
                } else if (colSetBefore == colSetAfter) {
                    //no row was completely became same color
                    if (mini[1] > ai_cnt) {
                        mini[1] = ai_cnt;
                        idx[1] = i;
                    }
                }
                if (mini[2] > ai_cnt) {
                    mini[2] = ai_cnt;
                    idx[2] = i;
                }
            } else {
                if (colorSetAfter != colorSetBefore) {
                    //some color was completely deleted
                    if (mini[0] < ai_cnt) {
                        mini[0] = ai_cnt;
                        idx[0] = i;
                    }
                }
                if (rowSetBefore != rowSetAfter) {
                    //some row was completely became same color
                    if (mini[1] < ai_cnt) {
                        mini[1] = ai_cnt;
                        idx[1] = i;
                    }
                } else if (colSetBefore != colSetAfter) {
                    //some row was completely became same color
                    if (mini[1] < ai_cnt) {
                        mini[1] = ai_cnt;
                        idx[1] = i;
                    }
                }
                if (mini[2] < ai_cnt) {
                    mini[2] = ai_cnt;
                    idx[2] = i;
                }
            }
        }
        if (idx[0] != -1) return idx[0];
        else if (idx[1] != -1) return idx[1];
        else return idx[2];
    }

    int bad_AI_Move() {
        int idx = AI_move(false);
        Log.e("AI MOVE", idx + "");
        return idx;
        // runDfs(0,0,grid[0][0],idx);
    }

    int good_AI_Move() {
        int idx = AI_move(true);
        Log.e("AI MOVE", idx + "");
        return idx;
        // runDfs(0,0,grid[0][0],idx);
    }


    int attached(int x) {
        Pair<Integer, Integer> cor = decode(x);
        return same[cor.first][cor.second];
    }
}