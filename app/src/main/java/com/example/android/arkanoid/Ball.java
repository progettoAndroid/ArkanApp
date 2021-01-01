package com.example.android.arkanoid;

public class Ball {

    protected float xRychlost;
    protected float yRychlost;
    private float x;
    private float y;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        vytvorRychlost();
    }

    // vytvorí random rýchlosť lopticky
    protected void vytvorRychlost() {
        int maxX = 13;
        int minX = 7;
        int maxY = -17;
        int minY = -23;
        int rangeX = maxX - minX + 1;
        int rangeY = maxY - minY + 1;

        xRychlost = (int) (Math.random() * rangeX) + minX;
        yRychlost = (int) (Math.random() * rangeY) + minY;
    }

    // cambia direzione in base alla velocità
    protected void zmenSmer() {
        if (xRychlost > 0 && yRychlost < 0) {
            otocXRychlost();
        } else if (xRychlost < 0 && yRychlost < 0) {
            otocYRychlost();
        } else if (xRychlost < 0 && yRychlost > 0) {
            otocXRychlost();
        } else if (xRychlost > 0 && yRychlost > 0) {
            otocYRychlost();
        }
    }

    // aumenta velocità in base al livello
    protected void zvysRychlost(int level) {
        xRychlost = xRychlost + (1 * level);
        yRychlost = yRychlost - (1 * level);
    }

    //cambia direzione a seconda del muro che ha toccato e della velocità
    protected void zmenSmer(String stena) {
        if (xRychlost > 0 && yRychlost < 0 && stena.equals("prava")) {
            otocXRychlost();
        } else if (xRychlost > 0 && yRychlost < 0 && stena.equals("hore")) {
            otocYRychlost();
        } else if (xRychlost < 0 && yRychlost < 0 && stena.equals("hore")) {
            otocYRychlost();
        } else if (xRychlost < 0 && yRychlost < 0 && stena.equals("lava")) {
            otocXRychlost();
        } else if (xRychlost < 0 && yRychlost > 0 && stena.equals("lava")) {
            otocXRychlost();
        } else if (xRychlost > 0 && yRychlost > 0 && stena.equals("dole")) {
            otocYRychlost();
        } else if (xRychlost > 0 && yRychlost > 0 && stena.equals("prava")) {
            otocXRychlost();
        }
    }

    //scopri se la palla è vicina
    private boolean jeBlizko(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        if ((Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow(ay - by, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 100) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 150) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        }
        return false;
    }

    //scopri se la palla è vicina a un mattone
    private boolean jeBlizkoBrick(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        double d = Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow((ay + 40) - by, 2));
        return d < 80;
    }

    //se la palla entra in collisione con il paddle, cambierà direzione
    protected void NarazPaddle(float xPaddle, float yPaddle) {
        if (jeBlizko(xPaddle, yPaddle, getX(), getY())) zmenSmer();
    }


    // se la palla entra in collisione con un mattone, cambierà direzione
    protected boolean NarazBrick(float xBrick, float yBrick) {
        if (jeBlizkoBrick(xBrick, yBrick, getX(), getY())) {
            zmenSmer();
            return true;
        } else return false;
    }

    // si sposta alla velocità specificata
    protected void pohni() {
        x = x + xRychlost;
        y = y + yRychlost;
    }

    public void otocXRychlost() {
        xRychlost = -xRychlost;
    }

    public void otocYRychlost() {
        yRychlost = -yRychlost;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setxRychlost(float xRychlost) {
        this.xRychlost = xRychlost;
    }

    public void setyRychlost(float yRychlost) {
        this.yRychlost = yRychlost;
    }

    public float getxRychlost() {
        return xRychlost;
    }

    public float getyRychlost() {
        return yRychlost;
    }
}
