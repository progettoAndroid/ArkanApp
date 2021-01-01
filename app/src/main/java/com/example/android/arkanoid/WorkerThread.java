package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;

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
           if(event.getAction()==MotionEvent.ACTION_DOWN) {
               if (context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                   //se il tocco avviene a destra dello schermo, e il paddle non finisce fuori dal bordo destro, lo muovo a destra
                   if ((event.getRawX() > (size.x / 2)) && ((paddle.getX() + size.x / 12) < size.x - 200)) {
                       paddle.setX(paddle.getX() + (size.x / 12));
                   }

                   //se il tocco avviene a sinistra e il paddle non finisce fuori dal bordo sinistro, lo muovo a sinistra
                   else if ((event.getRawX() < (size.x / 2)) && ((paddle.getX() - (size.x / 12)) > 0)) {
                       paddle.setX(paddle.getX() - (size.x / 12));
                   }
               }
               //do un po' di tempo per far notare lo spostamento
               try {
                   sleep(200);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               //altrimenti, se il comando è di tipo move, muovo il paddle assieme al dito
           } else if (event.getAction()==MotionEvent.ACTION_MOVE){
               if ((event.getRawX()<size.x - 220 && event.getRawX()>6)) {
                   paddle.setX(event.getRawX());
               }
           }
       }
    }

    public void terminate(){
        terminated = true;
    }

}
