package kr.ac.jbnu.se.tetris.Control;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Entity.Point;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import java.util.*;

import static kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas.TETRIS_CANVAS_H;
import static kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas.TETRIS_CANVAS_W;

public class Calculator {
    private TetrisCanvas canvas;

    public Calculator (TetrisCanvas canvas) {
        this.canvas = canvas;
    }

    public Double blockFitness(double[] weight) {
        double result = 0.0;

        int hc = hole_count();
        int cl = complete_line();

        int[] height = new int[TETRIS_CANVAS_H];
        int ah = aggregate_height(height);
        int b = bumpiness(height);

        result += ah * weight[0];
        result += hc * weight[1];
        result += b * weight[2];
        result += cl * weight[3];

        //System.out.println("h : " + hc + " c : " + cl + " bw : " + b + " ah : " + ah);
        return result;
    }

    // 완성된 줄을 찾는 메소드
    private int complete_line() {
        int ret = 0;
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            int j;
            for (j = 0; j < TETRIS_CANVAS_W; j++) {
                if (canvas.board[j * TETRIS_CANVAS_W + i] == Tetrominoes.NoShape)
                    break;
            }

            if (j == TETRIS_CANVAS_W)
                ret++;
        }

        return ret;
    }

    private int bumpiness(int height[]) {
        int ret = 0;
        for (int i = 1; i < TETRIS_CANVAS_W; i++) {
            ret += Math.abs(height[i - 1] - height[i]);
        }
        return ret;
    }

    private int aggregate_height(int height[]) {
        for (int i = 0; i < TETRIS_CANVAS_W; i++) {
            int high = TETRIS_CANVAS_H - 1;
            while (high >= 0) {
                if (canvas.board[high * TETRIS_CANVAS_W + i] != Tetrominoes.NoShape) {
                    break;
                }
                high--;
            }
            height[i] = high + 1;
        }

        int ret = 0;
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            ret += height[i];
        }
        return ret;
    }

    // 테트리스 블럭들 사이에 존재하는 구멍을 구하는 메소드
    private int hole_count() {
        boolean[][] visited = new boolean[TETRIS_CANVAS_H][TETRIS_CANVAS_W];

        // 테트리스 보드판에 0이 아닌 지점을 찾는다.
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            for (int j = 0; j < TETRIS_CANVAS_W; j++) {
                if (canvas.board[i * TETRIS_CANVAS_W + j] != Tetrominoes.NoShape)
                    visited[i][j] = true;
            }
        }

        // (0,4)를 기준으로 해서 bfs를 진행한다.
        bfs(visited);

        int ret = 0;

        // bfs를 진행하고 boolean 값이 false인 값은 구멍이다.
        for (int i = 0; i < TETRIS_CANVAS_H; i++) {
            for (int j = 0; j < TETRIS_CANVAS_W; j++) {
                if (!visited[i][j])
                    ret++;
            }
        }

        return ret;
    }

    private void bfs(boolean[][] visited) {
        int ud[] = { -1, 0, 1, 0 };
        int rl[] = { 0, 1, 0, -1 };

        Queue<Point> q = new LinkedList<>();
        q.add(new Point(0, 4));
        visited[0][4] = true;

        while (!q.isEmpty()) {
            Point cur = q.poll();

            for (int i = 0; i < 4; i++) {
                int nx = cur.x + ud[i];
                int ny = cur.y + rl[i];

                if (nx < 0 || nx >= TETRIS_CANVAS_H || ny < 0 || ny >= TETRIS_CANVAS_W || visited[nx][ny])
                    continue;

                visited[nx][ny] = true;
                q.add(new Point(nx, ny));
            }
        }
    }
}