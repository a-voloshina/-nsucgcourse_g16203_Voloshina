package ru.nsu.fit.g16203.voloshina;

import ru.nsu.fit.g16203.voloshina.view.MainWindow;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        for(int M = 0; M <= 4; ++M)
//        {
//        int dim = 1 << M;
//            System.out.println(" X = " + dim + ", Y = " + dim);
//            for(int y=0; y < dim; ++y)
//            {
//                System.out.print("   ");
//                for(int x=0; x < dim; ++x)
//                {
//                    int v = 0, mask = M-1, xc = x ^ y, yc = y;
//                    for(int bit = 0; bit < 2*M; --mask)
//                    {
//                        v |= ((yc >> mask)&1) << bit++;
//                        v |= ((xc >> mask)&1) << bit++;
//                    }
//                    System.out.print(v + " ");
//                }
//                System.out.print("|");
//                if(y == 0) System.out.print(" 1/" + dim * dim);
//                System.out.println();
//            }
//        }

        MainWindow mainWindow = new MainWindow();
    }

}
