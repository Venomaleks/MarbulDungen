package e.aleks.marbuldungen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
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

    // Kulans radie
    float marbR;

    // kulans rörelse riktning
    float SinA, SinB;

    // kulans hastigets ändring i x axlen och y axeln
    float Vy,Vx;
    //}

    //Bitmap}

    // Paint pänan som ritar som frågas{

    // text som kan användas
    Paint paint1;

    // Laburintväggar användas{
    Paint dungenWall;

    // Hur flexibel/hård vägen är
    float wallH;

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

    // svarta hål{
    Paint hole;

    // hålets positoner
    float holex, holey;

    //hålets radie
    int holeR;
    //}
    //Paint}

    // Joystick {

    // mittposition av Rörelseområdet (NÄR MAN RÖR SKÄRMEN INTE ANNARS!!!!!<--- Viktigt att veta)
    float dx, dy;

    // vinkel av Rörelseområdets cirkeln ( vilken vinkel kommer personen ha i jämförelse av rörelse områdets mittposition)
    float angle;

    // längdavståndet av mitten av cirkeln och användarens finger i rörelse området
    float Jhypotenusan;

    //Joystick}

    // Backgrunds variabler {

    // skärm bräden
    int Swhith;
    // skärm hövden
    int Shight;

    // användarens Touch position på skärmen
    float x, y;

    // En gravitations konstant
    float G;

    // räknar alla tids steg
    int Time;

    // hur länge man väntar tills man ser än ändring
    int Timestep;

    // startar en trråd
    MySurficeThread thread;

    // sätter en run boolean till thread som automatiskt är sant
    boolean run = true;

    // debug text man som man kna få ut
    String tag = "debugging";

    // Backgrunds variabler}

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

        // alla backgrunds atribut{

        // skapar en tråd samt en holder
        thread = new MySurficeThread(getHolder(), this);
        getHolder().addCallback(this);

        // Hur många tids steg det har tagit när man startar
        Time = 0;

        // 20 millisecunder innan den vissar ändringen
        Timestep = 20;

        // tar fram särm stroleken på användarens särm
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Swhith = size.x;
        Shight = size.y;

        // mitten av joystiken
        dx = dy = 0;

        // Hastigheten som kulan kommer röra sig med
        Vx = Vy = 0;


        // ange en typ av tyngdkrafts acction
        G = (float) 0.001;

        //}

        // alla Ritade Bilder{

        // dungenWalls atribut{
        dungenWall = new Paint();

        //tjocklek
        dungenWall.setStrokeWidth(20);

        //Färg
        dungenWall.setColor(Color.rgb(0,0,255));

        // hur "hård vägen är"
        wallH =(float) 1;
        //}

        // hole atribut{
        hole = new Paint();

        // färg på hole
        hole.setColor(Color.rgb(0,0,0));

        // hole radius
        holeR = 60;

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

        // alla Ritade Bilder}

        // Bilderna i PNG som ska användas (samma bild men olika namn){

        // Kulan
        marb = BitmapFactory.decodeResource(getResources(),R.drawable.marb);

        // kulans radie defenerat
        marbR = marb.getWidth()/2;

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
    protected void draw(Canvas canvas, float x, float y, float marbW, float marbH) {
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

        // Draw hole{
        holex = (getWidth()/100)*20;
        holey = (getHeight()/100)*60;

        canvas.drawCircle(holex, holey, holeR, hole);
        // hole ritad}

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
        // vart kulan ska starta
        if (Time == 0){

            marbW = (canvas.getWidth() / 100) * 5;
            marbH = (canvas.getHeight() / 100) * 5;

            canvas.drawBitmap(marb, marbW, marbH, null);

        }else{ // vart kulan ska vara efter första tids hopet

            canvas.drawBitmap(marb, marbW, marbH, null);
        }

        // Kulan ritad}

        // Längden drån holet till kulan i höjd
        float Hdy = (holey - marbH);

        // Längdenområet från holet till kulan i bred
        float Hdx = (holex - marbW);

        // Hypotenusan ^2 av andänder sig av Längden drån holet till kulan fulla position

        float HypHD = (Hdy*Hdy) + (Hdx*Hdx);



        // tar fram användarens touch position i x och y axeln på skärmen som text
        canvas.drawText(Float.toString(x),20,40,paint1);
        canvas.drawText(Float.toString(y),20,140,paint1);

        canvas.drawText(Float.toString(x),20,40,paint1);
        canvas.drawText(Float.toString(y),20,140,paint1);
        canvas.drawText(Float.toString(marbW),20,340,paint1);
        canvas.drawText(Float.toString(marbH),20,440,paint1);
        canvas.drawText(Float.toString(Time),20,740,paint1);

        canvas.drawText(Float.toString(HypHD),20,840,paint1);

        canvas.drawText(Float.toString(Vx),20,940,paint1);
        canvas.drawText(Float.toString(Vy),20,1040,paint1);


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

            Vx = Vy = 0;

            // @Override
            cSurfaceView.setOnTouchListener(new OnTouchListener() {
                @Override

                // ALLT HÄR INNE KOMMER BARA FUNGERA NÄR PERSONEN RÖR SKÄRMEN{

                public boolean onTouch(View v, MotionEvent event) {

                    // användarens truck positon blir toutchX och toutchY
                    x = event.getX();
                    y = event.getY();

                    // Värden man beräknar för nya positoner och hastigheter för kulan marbW, marbH,Vx,Vy
                    // Värden man beräknar för rörelsen av joysticken x, y
                    calculateValues(x,y,marbW,marbH,Vx,Vy);

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

                private void calculateValues(float xx, float yy, float mx,float my, float VX, float VY ) {


                    // joystickens center positon för rörelsen av joysticken när skärmen blir tryckt
                    dx = (xx - joysitckx) - (getWidth() / 100) * 5;
                    dy = (yy - joysticky) - (getHeight() / 100) * 5;

                    // hur man tar fram vinkeln av med hjälp

                    angle = (float) Math.atan(Math.abs(dy / dx));

                    // Hypotenusan av rörelseområdet för joysticken

                    Jhypotenusan = (float) Math.sqrt(dx * dx + dy * dy);


                    // Hur stor lutningen är i X-axeln (räknas i radianer)
                    SinA =(float) ((dx/Jhypotenusan) * 0.15); // 0.15 gör så lutningen blir ≈ 15 graders lutning

                    // Hur stor lutningen är i Y-axeln (räknas i radianer)
                    SinB =(float) ((dy/Jhypotenusan) * 0.15); // 0.15 gör så lutningen blir ≈ 15 graders lutning


                    // Kulans rörelse ändring när personen rör särmen i y axeln
                    my = (float) (marbH + Vy * Timestep + G * SinA * (Timestep*Timestep) * 0.5);

                    // ger den nya positonen till den nuvarande positonen
                    marbH = my;

                    // Kulans rörelse ändring när personen rör särmen i x axeln

                    mx =(float) (marbW + Vx * Timestep + G * SinB * (Timestep*Timestep) * 0.5);

                    // ger den nya positonen till den nuvarande positonen
                    marbW = mx;


                    // Hur stor ska rörelsen vara i X axeln för kulan
                    Vx = VX + Timestep * G * SinA;

                    // Hur stor ska rörelsen vara i Y axeln för kulan
                    Vy = VY + Timestep * G * SinB;


                    // hur man kan se vart joystickens position är på Rörelse området

                    if (Jhypotenusan > radius) {


                    // alla positoner som joysticken kan hamnapå i rörelse området
                        //~~ Jag förstår inte uträkningen här , men det verkar som att det fungerar?
                        //~~Nu rör sig väl joysticken inom cirkeln, som den ska?

                        if (dx > 0 && dy > 0) { // bot höger

                            xx = (float) (joysitckx + (radius * Math.cos(angle)));
                            yy = (float) (joysticky + (radius * Math.sin(angle)));


                    } else if (dx > 0 && dy < 0) { // top höger

                        xx = (float) (joysitckx + (radius * Math.cos(angle)));
                        yy = (float) (joysticky - (radius * Math.sin(angle)));


                    } else if (dx < 0 && dy > 0) { // bot vänster

                        xx = (float) (joysitckx - (radius * Math.cos(angle)));
                        yy = (float) (joysticky + (radius * Math.sin(angle)));


                    } else if (dx < 0 && dy < 0) { // top vänster

                            xx = (float) (joysitckx - (radius * Math.cos(angle)));
                            yy = (float) (joysticky - (radius * Math.sin(angle)));
                    }

                    // vad kullan får för rörelse yta om man har en touch utanför den animerade cirkeln

                        x = xx + ((getWidth() / 100) * 9) / 2;
                        y = yy + ((getHeight() / 100) * 13) / 2;
                }

            }

        });

            // ALLT HÄR INNE KOMMER BARA FUNGERA NÄR PERSONEN RÖR SKÄRMEN}

    }

        // Spelning av app så att allt går

        @Override
        protected Void doInBackground(Void... voids) {

            // vad som händer under run metoden skapas här
            // samt det som vissas på skärmen hanteras här
            while (run) {

                // Att kullan inte ska åka utanför Dungen ramens alla vägar{


                // att hastigheten ska byta riktning i kontakt is deta område
                if (marbH < MinHight + 15) {

                    marbH = MinHight +(marbR / 3);

                    Vy = Math.abs(Vy) * wallH;


                }

                // att hastigheten ska byta riktning i kontakt is deta område
                if ( marbW < (MinWidth + 15)) {

                    marbW = MinWidth + marbR / 3;

                    Vx = Math.abs(Vx) * wallH;


                }

                // att hastigheten ska byta riktning i kontakt is deta område
                if (marbH > MaxHight - marb.getHeight() - 15){

                    marbH = MaxHight - (float) (marbR * 2.3);

                    Vy =  - Vy * wallH;

                }


                // att hastigheten ska byta riktning i kontakt is deta område
                if (marbW > (MaxWidth - marb.getWidth() - 15)){

                    marbW = MaxWidth - (float) (marbR * 2.3);

                    Vx =  - Vx * wallH;

                }

                // Att kullan inte ska åka utanför Dungen ramens alla vägar{

                // Att kulan ska röra sig görs här {
                if (x != 0 && y != 0){// NÄR DU RÖR KONTROLEN HÄNDER ÄNDRING AV VELOSETY / HASTIGHET

                    Vx = Vx + Timestep * G * SinA;
                    Vy = Vy + Timestep * G * SinB;

                }

                // Vissas ändringen på skärmen
                marbH = (float) (marbH + Vy * Timestep + G * SinA * (Timestep*Timestep) * 0.5);

                marbW =(float) (marbW + Vx * Timestep + G * SinB * (Timestep*Timestep) * 0.5);

                // Att kulan rör sig}


                float Hdy = (holey - marbH) - (getHeight() / 100) * 3;

                float Hdx = (holex - marbW) - (getHeight() / 100) * 3;


                // holets distans tills den ska ramla i   - marbW  - marbH (marb.getWidth()/2)*marb.getWidth()/2)
                float holedis =((Hdy*Hdy) + (Hdx*Hdx));


                // vad som händer när spelaren rular i hålet
                if (holedis < marbR*marbR){

                    Time = 0;

                    Vx = Vy = 0;

                    SinA = SinB = 0;

                }

                Canvas canvas = null;

                try {

                    canvas = mSurfaceHolder.lockCanvas(null);

                    synchronized (mSurfaceHolder) {

                        cSurfaceView.draw(canvas, x, y, marbW, marbH);

                        //  BitmapFactory.decodeResource(marb, joysitckx, joysticky,(int) marbW,(int) marbH);
                        // cSurfaceView.onDraw(marb, marbW,marbH, null);
                        Time += 1;
                    }
                    Thread.sleep(Timestep);
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
