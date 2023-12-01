package kr.ac.jbnu.se.tetris.entity;

public class Chromosome {


    public static final int SIZE_GENE = 4;
    int fitness = 0;
    int number = 0;
    long score = 0;

    /**
     * 0.블록 높이의 합<br/>
     * 1.블록 사이에 있는 빈 공간의 개수<br/>
     * 2.높이 차<br/>
     * 3.완성된 줄의 개수<br/>
     */
    double[] weight;

    public Chromosome(int number, double[] weight) {
        this.weight = new double[SIZE_GENE];
        this.number = number;
        System.arraycopy(weight, 0, this.weight, 0, SIZE_GENE);
    }

    public void normalize() {
        double sum = 0;
        for (int i = 0; i < SIZE_GENE; i++) {
            sum += weight[i] * weight[i];
        }
        double mean = Math.sqrt(sum);
        for (int i = 0; i < SIZE_GENE; i++) {
            weight[i] = weight[i] / mean;
        }
    }
    public int getFitness(){return fitness;}
    public void updateFitness(int i){fitness = i;}
    public double[] getWeight(){return weight;}
    public int getNumber() {return number;}
    public void updateNumber(int i) {number = i;}
}
