package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static java.lang.Thread.sleep;

public class WorkerThread extends Thread {
    private volatile Context context;
    private volatile MotionEvent event;
    private volatile Paddle paddle;
    private volatile Point size;
    private volatile boolean terminated=false;
    public WorkerThread (Context context, MotionEvent event, Paddle paddle, Point size){
        this.context = context;
        this.event = event;
        this.paddle = paddle;
        this.size = size;
    }

    @Override
    public void run() {
        super.run();
        //finché non si verifica l'azione di rimozione del dito dallo schermo
       while(!terminated){
           //se l'azione è un tocco, sposto a destra o sinistra il paddle

               if (context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                   //se il tocco avviene a destra dello schermo, e il paddle non finisce fuori dal bordo destro, lo muovo a destra
                   if ((event.getRawX() > (size.x / 2)) && ((paddle.getX() + size.x / 100) < size.x - 200)) {
                       paddle.setX(paddle.getX() + (size.x / 100));
                   }

                   //se il tocco avviene a sinistra e il paddle non finisce fuori dal bordo sinistro, lo muovo a sinistra
                   else if ((event.getRawX() < (size.x / 2)) && ((paddle.getX() - (size.x / 100)) > 0)) {
                       paddle.setX(paddle.getX() - (size.x / 100));
                   }
               } else if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
                   if ((event.getRawX() > (size.x / 2)) && ((paddle.getX() + size.x / 100) < size.x - 200)) {
                       paddle.setX(paddle.getX() + (size.x / 100));
                   }

                   //se il tocco avviene a sinistra e il paddle non finisce fuori dal bordo sinistro, lo muovo a sinistra
                   else if ((event.getRawX() < (size.x / 2)) && ((paddle.getX() - (size.x / 100)) > 0)) {
                       paddle.setX(paddle.getX() - (size.x / 100));
                   }
               }
               //do un po' di tempo per far notare lo spostamento
           try {
               sleep(10);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           }

    }

    public void terminate(){
        terminated = true;
    }

}
