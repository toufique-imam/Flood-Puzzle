package com.sabertooth.flood_puzzle;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import java.util.Random;

public class FloodMaker {
    int row,col;
    int[][] grid;
    int[][] visited;
    //int[][] X_st,X_en,Y_st,Y_en;
    int dx[] = {-1,1,0,0};
    int dy[] = {0,0,-1,1};
    int distinct_col;
    FloodMaker(int x,int y,int color_cnt){
        row = x;
        col = y;
        distinct_col = color_cnt;
        grid = new int[x][y];
        visited = new int[x][y];
     //   X_st = new int[x][y];
      //  X_en = new int[x][y];
       // Y_st = new int[x][y];
        //Y_en = new int[x][y];
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                grid[i][j] = (int) (Math.random()*distinct_col);
               // Log.e("GRID VALUE ",i+" "+j+" "+grid[i][j]);
                assert (grid[i][j]<8);
                visited[i][j] = 0;
          //      X_st[i][j] = j*sq_size;
           //     X_en[i][j] = (j+1)*sq_size;
            //    Y_st[i][j] = i*sq_size;
             //   Y_en[i][j] = (i+1)*sq_size;
            }
        }
    }
    void runDfs(int x,int y,int color,int newColor){
        visited[x][y] = 1;
        grid[x][y]= newColor;
        for(int i=0;i<4;i++){
            int px = dx[i]+x;
            int py = dy[i]+y;
            if(px>-1 && py>-1 && px<row && py<col && visited[px][py]==0 && (grid[px][py]==color || grid[px][py]==newColor)){
                runDfs(px,py,color,newColor);
            }
        }
    }
    void processMove(int color){
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                visited[i][j] = 0;
            }
        }
        runDfs(0,0,grid[0][0],color);
    }
    boolean gameFinished(){
        boolean ok = true;
        for(int i=0;i<row && ok;i++){
            for(int j=0;j<col && ok;j++){
                if(grid[i][j]!=grid[0][0]){
                    ok = false;
                    break;
                }
            }
        }
        return ok;
    }
    int count(){
        return row*col;
    }
    int encode(int x,int y){
        return x*col+y;
    }
    Pair<Integer,Integer> decode(int i){
        return  Pair.create(i/col,i%col);
    }
    int colorValue(int i){
        Pair<Integer,Integer> x =decode(i);
        Log.e("LOG",x.first+" "+x.second);
        if ((grid[x.first][x.second] >= 8)) throw new AssertionError();
        return grid[x.first][x.second];
    }
//    int StartPositionX(int i){
//        Pair<Integer,Integer> x =decode(i);
//        return X_st[x.first][x.second];
//    }
//    int StartPositionY(int i){
//        Pair<Integer,Integer> x =decode(i);
//        return Y_st[x.first][x.second];
//    }
//    int EndPositionY(int i){
//        Pair<Integer,Integer> x =decode(i);
//        return Y_en[x.first][x.second];
//    }
//    int EndPositionX(int i){
//        Pair<Integer,Integer> x =decode(i);
//        return X_en[x.first][x.second];
//    }
}
