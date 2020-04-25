package com.sabertooth.flood_puzzle;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;

class AdapterCell extends RecyclerView.ViewHolder {
    LinearLayout cx;
    AppCompatButton button;

    public AdapterCell(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.cellbutton);
        cx = itemView.findViewById(R.id.listCellView);
    }
}

public class AdapterGrid extends RecyclerView.Adapter<AdapterCell> {
    Context appContext;
    FloodMaker FM;
    int[] cellColor = {Color.BLUE, Color.DKGRAY, Color.YELLOW, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.WHITE};
    TextView result;
    int count = 0;
    boolean gameover = false;
    int gamemode;

    public AdapterGrid(Context appContext, FloodMaker FM, TextView _res, int gamemode) {
        this.appContext = appContext;
        this.FM = FM;
        this.gameover = false;
        this.result = _res;
        this.result.setText(R.string.player_move_count);
        this.gamemode = gamemode;
        count = 0;
    }

    @NonNull
    @Override
    public AdapterCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(appContext).inflate(R.layout.cell_layout, parent, false);
        return new AdapterCell(inflat);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCell holder, final int position) {
        int idx = FM.colorValue(position);
        final int curCellColor = cellColor[idx];

        //  int x_st = FM.StartPositionX(position);
        // int y_st = FM.StartPositionY(position);
        //int x_en = FM.EndPositionX(position);
        //int y_en = FM.EndPositionY(position);
        holder.cx.setBackgroundColor(curCellColor);
        holder.button.setBackgroundColor(curCellColor);
        //holder.button.setText("NICE"+position);
        //holder.button.redraw(x_st,y_st,x_en,y_en,curCell);
        if (!gameover) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int maincell = FM.colorValue(0);
                    int curCell = FM.colorValue(position);
                    if (gamemode != 0) count++;
                    else if(maincell!=curCell)count++;
                    if (maincell != curCell) {
                        FM.processMove(curCell);
                        notifyDataSetChanged();
                    }
                    if (gamemode == 1) {
                        if (count % 2 == 1)
                            result.setText("Player 2 Turn");
                        else {
                            result.setText("Player 1 Turn");
                        }
                    } else if (gamemode == 2) {
                        if (count % 2 == 1)
                            result.setText("AI Turn");
                        else {
                            result.setText("Player 1 Turn");
                        }
                    } else {
                        result.setText("Total Moves " + count);
                    }
                    gameover = FM.gameFinished();
                    if (gameover) {
                        if (gamemode == 0) {
                            result.setText("Player Finished with " + count + " moves");
                        } else if (gamemode == 1) {
                            if (count % 2 == 1) {
                                result.setText("Player 1 Wins");
                            } else {
                                result.setText("Player 2 Wins");
                            }
                        } else if (gamemode == 2) {
                            if (count % 2 == 1) {
                                result.setText("Player 1 Wins");
                            } else {
                                result.setText("AI 2 Wins");
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return FM.count();
    }
}