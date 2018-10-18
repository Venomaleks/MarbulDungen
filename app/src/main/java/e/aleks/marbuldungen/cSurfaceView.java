package e.aleks.marbuldungen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class cSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // Bitmap Bilder med start atribut{

    // joysitck = Ratt för spelaren{
    Bitmap joysitck;

    // Rattens positon på skärmen
    float joysitckx, joysticky;
    //}

    // marb = Kullan som joysitck styr{
    Bitmap marb;

    // Kullans positon på skärmen
    float marbW, marbH;
    //}

    //Bitmap}

    // Paint pänan som ritar som frågas{

    // text som kan användas
    Paint paint1;

    // Laburintväggar användas{
    Paint dungenWall;

    // Laburintensväg max positoner (gämtemed X och Y axeln av hella skärmen)
    float MinWidth, MaxWidth, MinHight, MaxHight;
    //}

    // Animerad cirkel, balls ritade rörelse område{
    Paint animeradCirkel;

    // radien på cirkeln
    int radius;
    // Cirkelns positioner
    float cirkelx, cirkely;
    //}
    //Paint}

    // Rörelseområdet för Joystick (NÄR MAN RÖR SKÄRMEN INTE ANNARS!!!!!<--- Viktigt att veta){

    // mittposition av Rörelseområdet (där joystick kommer visas när man rör på skärmen)
    float dx, dy;
    // vinkel av Rörelseområdets cirkeln ( vilken vinkel kommer personen ha i jämförelse av rörelse områdets mittposition)
    float angle;
    // längdavståndet av mitten av cirkeln och användarens finger i rörelse området
    float hypotenusan;
    //Rörelseområdet}


    // användarens Touch position på skärmen
    float x, y;

    // startar en trråd
    MySurficeThread thread;

    // sätter en run boolean till thread som automatiskt är sant
    boolean run = true;

    // debug text man som man kna få ut
    String tag = "debugging";


    // A, speedLV;
    // float tempX;

    public cSurfaceView(Context context) {
        super(context);

        init();
    }

    public cSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public cSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {

        // skapar en tråd samt en holder
        thread = new MySurficeThread(getHolder(), this);
        getHolder().addCallback(this);

        // dungenWalls atribut{
        dungenWall = new Paint();

        //tjocklek
        dungenWall.setStrokeWidth(20);

        //Färg
        dungenWall.setColor(Color.rgb(0,0,255));

        //}

        // paint1s attribut {
        paint1 = new Paint();

        //textstrolek
        paint1.setTextSize(40);
        //Färg
        paint1.setColor(Color.rgb(255,0,0));
        //}

        // animeradCirkels atribut{
        animeradCirkel = new Paint();

        //Färg
        animeradCirkel.setColor(Color.rgb(0,0,0));
        // cirkelns radie
        radius = 125;
        // cirkelns position på skärmen

        //}

        dx = dy = 0;

        // Ritar Bilderna som ska användas (samma bild men olika namn){

        // Kulan
        marb = BitmapFactory.decodeResource(getResources(),R.drawable.marb);

        // joystickens atribut
        joysitck = BitmapFactory.decodeResource(getResources(), R.drawable.joystick);

        //}

        // Hur man kan ändra storlek på Bilder

        /*


        float scale = Math.min(((float)maxHeight / max.getWidth()), ((float)maxWidth / max.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

      max = Bitmap.createBitmap(max, 0, 0, max.getWidth(), max.getHeight(), matrix, true);
*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread.execute((Void[])null);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //    @Override
    protected void draw(Canvas canvas, float x, float y) {
        super.draw(canvas);

        //Backgrundsbilden, färg (använder ingen bild)

        canvas.drawRGB(0, 255, 0);


        // Draw Frame of Dungun

        //canvas.getWidth()/100*54 canvas.getHeight()/100)*85

        // cirkelns position på skärmen {
        cirkelx = (canvas.getWidth()/100)*54;
        cirkely = (canvas.getHeight()/100)*90;

        canvas.drawCircle(cirkelx ,cirkely, radius, animeradCirkel);
        //cirkeln ritad }

        // canvas.drawRect(MinHight, MaxWidth, MaxHight, MinWidth,dungenWall);

        // Labyrintens MaxArea yta på skärmen
        MinWidth = (getWidth()/100);
        MaxWidth = (getWidth()/100)*107;
        MinHight = (getHeight()/100);
        MaxHight = (getHeight()/100)*75;

        // Labyrint ram
        canvas.drawLine(MinWidth,MinHight, MinWidth, MaxHight, dungenWall);
        canvas.drawLine(MaxWidth,MinHight, MaxWidth, MaxHight, dungenWall);
        canvas.drawLine(MinWidth,MinHight, MaxWidth, MinHight, dungenWall);
        canvas.drawLine(MinWidth,MaxHight, MaxWidth, MaxHight, dungenWall);

        // The Dungen :,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(



        // Kulans position på skärmen{
        marbW = (getWidth()/100)*5;
        marbH = (getHeight()/100)*5;

        canvas.drawBitmap(marb, marbW, marbH, null);
        // Kulan ritad}




        // tar fram användarens touch position i x och y axeln på skärmen som text
        canvas.drawText(Float.toString(x),20,40,paint1);
        canvas.drawText(Float.toString(y),20,140,paint1);

        // (canvas.getHeight()/100)*85- joysitck.getHeight()/2 TOP
        // canvas.getWidth()/2- joysitck.getWidth()/2 LEFT




        // joysticken i sina positoner
        if (x == 0 && y == 0){

            // joystickens mitpunkt på skärmen när du inte trycker på skärmen
            joysitckx = canvas.getWidth()/2- joysitck.getWidth()/2;
            joysticky = (canvas.getHeight()/100)*90- joysitck.getHeight()/2;

            // om ingen rör skärmen hamnar joysticken
            canvas.drawBitmap(joysitck, joysitckx, joysticky, null);
        }
        else {

            //  när man rör kontrolen (så att personen ska kunna se joysticken så sa jag att den skulle sticke ut med kullans höjd jämte med fingeret)
            canvas.drawBitmap(joysitck, x-joysitck.getWidth()/2, y-joysitck.getHeight(), null);
        }

        //y-joysitck.getHeight() TOP   x-joysitck.getWidth()/2 LEFT
        Log.i(tag,"onDrow");

    }



    public class MySurficeThread extends AsyncTask<Void,Void,Void> {

        SurfaceHolder mSurfaceHolder;

        cSurfaceView cSurfaceView;

        public MySurficeThread(SurfaceHolder sh, cSurfaceView csv) {


            mSurfaceHolder = sh;

            cSurfaceView = csv;

            x = y = 0;


            cSurfaceView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // användarens truck positon blir toutchX och toutchY
                    x = event.getX();
                    y = event.getY();

                    calculateValues(x, y);

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:

                            break;

                        case MotionEvent.ACTION_UP:

                            x = y = 0;
                            dx = dy = 0;

                            break;

                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }

                    return true;
                }

                private void calculateValues(float xx, float yy) {


                    // joystickens center positon för rörelsen av joysticken när skärmen blir tryckt
                    dx = xx - joysitckx;
                    dy = yy - joysticky;

                    // hur man tar fram vinkeln av med hjälp

                    angle = (float) Math.atan(Math.abs(dy / dx));

                    // Hypotenusan av rörelseområdet för joysticken

                    hypotenusan = (float) Math.sqrt(dx * dx + dy * dy);


                    // hur man kan se vart joystickens position är på Rörelse området
                    if (hypotenusan > radius) {


                    // alla positoner som joysticken kan hamnapå i rörelse området

                        if (dx > 0 && dy > 0) { // bot höger

                            xx = (float) (joysitckx + (radius * Math.cos(angle)));
                            yy = (float) (joysticky + (radius * Math.sin(angle)));

                            Log.d("marbW ", "print1: :" + String.valueOf(marbW));
                            Log.d("marbH ", "print1: :" + String.valueOf(marbH));

                            Log.d("radius ", "printR: :" + String.valueOf(radius));

                            Log.d("dx ", "printDX: :" + String.valueOf(dx));
                            Log.d("dy ", "printDY: :" + String.valueOf(dy));

                            Log.d("marbW2 ", "print2: :" + String.valueOf(marbW));
                            Log.d("marbH2 ", "print2: :" + String.valueOf(marbH));


                    } else if (dx > 0 && dy < 0) { // top höger

                        xx = (float) (joysitckx + (radius * Math.cos(angle)));
                        yy = (float) (joysticky - (radius * Math.sin(angle)));


                    } else if (dx < 0 && dy > 0) { // bot vänster

                        xx = (float) (joysitckx - (radius * Math.cos(angle)));
                        yy = (float) (joysticky + (radius * Math.sin(angle)));


                    } else if (dx < 0 && dy < 0) { // top vänster

                        xx = (float) (joysitckx - (radius * Math.cos(angle)));
                        yy = (float) (joysticky - (radius * Math.sin(angle)));


                    } else { // annars ?

                        xx = joysitckx + dx;
                        yy = joysticky + dy;

                    }

                    // sätter så att positionen av fingret kommer att vissas av xx och yy

                    x = xx;
                    y = yy;
                }

            }

        });


    }

        // Spelning av app så att allt går

        @Override
        protected Void doInBackground(Void... voids) {

            while (run) {

                Canvas canvas = null;

                try {

                    canvas = mSurfaceHolder.lockCanvas(null);

                    // joysticky = getWidth()/2;
                    // zerox = getHeight()/20*19;

                    // joysticky = (canvas.getHeight()/20)*19;


                    synchronized (mSurfaceHolder) {

                        cSurfaceView.draw(canvas, x, y);

                        //  BitmapFactory.decodeResource(marb, joysitckx, joysticky,(int) marbW,(int) marbH);
                        // cSurfaceView.onDraw(marb, marbW,marbH, null);
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                } finally {

                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
            return null;
        }
    }
}
