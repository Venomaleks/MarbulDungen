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

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // Backgrunds atribut

    Display display;//~~Gråmarkerade variabler som det står "is never used" kan tas bort.
        // Du ser detta meddelande om du lägger markören över variabeln.

    int Swhith;
    int Shight;

    // Bitmap Bilder med start atribut{

    // joysitck = Ratt för spelaren{
    Bitmap joysitck;

    // Rattens positon på skärmen
    float joystickx, joysticky;
    //}

    // marb = Kulan som joysitck styr{
    Bitmap marb;

    // Kulans positon på skärmen
    float marbW, marbH;

    // Kulans radie
    float marbRH,marbRW;

    // kulans rörelse riktning

    float marbMovementA, marbMovementB;

    //}

    //Bitmap}

    // Paint pänan som ritar som frågas{

    // text som kan användas
    Paint paint1;

    // Laburintväggar användas{
    Paint dungenWall;

    float wallH;

    // Laburintensväg max positoner (gämtemed X och Y axeln av hella skärmen)
    float MinWidth, MaxWidth, MinHight, MaxHight;//~~ variabel namn ska börja med små bokstäver
    //}

    // Animerad cirkel, balls ritade rörelse område{
    Paint animeradCirkel;

    // radien på cirkeln
    int radius;
    // Cirkelns positioner
    float cirkelx, cirkely;
    //}

    // svarta hålen{
    Paint hole;

    // hole positoner
    float holex, holey;

    float holeR;
    //}

    //Paint}

    // Rörelseområdet för Joystick (NÄR MAN RÖR SKÄRMEN INTE ANNARS!!!!!<--- Viktigt att veta){

    // mitpositon av Rörelse området (där joystick kommer vissas när man rör på skärmen)
    float dx, dy;
    // vinkel av Rörelse områdets cirkeln ( vilken vilken vinkel kommer personen ha gämförelse av des mitpositon)
    float angle;

    // längd av ståndet av mitten av cirkeln och användarens finger
    float Jhypotenusan;
    //Rörelseområdet}

    boolean role;

    // användarens Touch positon på skärmen
    float x, y;

    float Vy,Vx;

    float G;

    int Time;

    // hur länge man väntar tills man ser än ändring
    int Dt;
/*
    private static final long COUNTDOWN_MILLIS = 6000; //180000milli 180sec 3 min
    long Time = COUNTDOWN_MILLIS;
    Random rand;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 1;
    Thread start;
    Intent END;
*/

    // SurficeViewens tråd
    MySurficeThread thread;

    boolean run = true;

    // debug text man som man kna få ut
    String tag = "debugging";



    // A, speedLV;
    // float tempX;

    public CustomSurfaceView(Context context) {
        super(context);

        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {

        // skappar en tråd samt en holder
        thread = new MySurficeThread(getHolder(), this);
        getHolder().addCallback(this);

        // mspeed.;

        Time = 0;

        Dt = 20;

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Swhith = size.x;
        Shight = size.y;
        role = false;

        // dungenWalls atribut{
        dungenWall = new Paint();

        //tjocklek (kan vara Swhith*0.025f)

        float WallThcc = 20;

        dungenWall.setStrokeWidth(WallThcc);

        //Färg
        dungenWall.setColor(Color.rgb(0,0,255));

        // hur "hård vägen är"
        wallH = 0.75f;
        //}

        // hole atribut{
        hole = new Paint();

        // färg på hole
        hole.setColor(Color.rgb(0,0,0));

        //}

        // paint1s atribut {
        paint1 = new Paint();

        //textstrolek
        paint1.setTextSize(40);//~~behöver vara relativt till skärmens bredd eller höjd
        //Färg
        paint1.setColor(Color.rgb(255,0,0));
        //}

        // animeradCirkels atribut{
        animeradCirkel = new Paint();

        //Färg
        animeradCirkel.setColor(Color.rgb(0,0,0));
        // cirkelns radie
        radius = 125;//~~behöver vara relativt till skärmens bredd eller höjd
        // cirkelns positon på skärmen

        //}
        // mitten av joystiken
        dx = dy = 0;

        // Hastigheten som kulan kommer röra sig med
        Vx = Vy = 0;


        // ange en typ av tyngdkrafts acction
        G = (float) 0.001;

        // Ritar Bilderna som ska användas (samma bild men olika namn){

        // Kulan
        marb = BitmapFactory.decodeResource(getResources(),R.drawable.marb);

        marbRW = marb.getWidth()/2;

        marbRH = marb.getHeight()/2;

        // joystickens
        joysitck = BitmapFactory.decodeResource(getResources(), R.drawable.joystick);

        //

        // Hur man kan ändra storlek på Bilder

        /*


        float scale = Math.min(((float)maxHeight / max.getWidth()), ((float)maxWidth / max.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

      max = Bitmap.createBitmap(max, 0, 0, max.getWidth(), max.getHeight(), matrix, true);
*/
    }



    // VIKTIGT HÄR KONTROLERAR DU HUR HELLA PROGRAMET SKA GÅ I TID
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
    protected void draw(Canvas canvas, float x, float y, float mx, float my) {
        super.draw(canvas);

        //Backgrundsbilden, färg (använder ingen bild)

        canvas.drawRGB(0, 255, 0);


        // Draw Frame of Dungun

        //canvas.getWidth()/100*54 canvas.getHeight()/100)*85

        // cirkelns position på skärmen {
        cirkelx = 0.5f * canvas.getWidth();
        cirkely = 0.87f * canvas.getHeight();

        canvas.drawCircle(cirkelx ,cirkely, radius, animeradCirkel);
        //cirkeln ritad }



        // Draw hole{
        holex = getWidth()/12;
        holey = getHeight()/32;

        // hole radius
        holeR = getWidth()*0.06f;

        // se antäknings booken för att se vad siffrorna står för

        canvas.drawCircle(holex*5, holey*6.4f, holeR, hole); // H1


        canvas.drawCircle(holex*5, holey*14.8f, holeR, hole); // H2

        canvas.drawCircle(holex*9.1f, holey*14.8f, holeR, hole); // H3

        canvas.drawCircle(holex*11, holey*2f, holeR, hole); // H4

        canvas.drawCircle(holex*1.1f, holey*22.4f, holeR, hole); // H5

        canvas.drawCircle(holex*11, holey*22.4f, holeR, hole); // H6
        // hole ritad}


        // canvas.drawRect(MinHight, MaxWidth, MaxHight, MinWidth,dungenWall);

        // Labyrintens MaxArea yta på skärmen
        MinWidth = 0.01f * getWidth();
        MaxWidth = 0.99f * getWidth();
        MinHight = 0.01f * getHeight();
        MaxHight = 0.75f * getHeight();

        // Laburint ram
        canvas.drawLine(MinWidth,MinHight, MinWidth, MaxHight, dungenWall);
        canvas.drawLine(MaxWidth,MinHight, MaxWidth, MaxHight, dungenWall);
        canvas.drawLine(MinWidth,MinHight, MaxWidth, MinHight, dungenWall);
        canvas.drawLine(MinWidth,MaxHight, MaxWidth, MaxHight, dungenWall);

        // dela in bannans storlekar i delar för att konstruera laburinten lättare

        float baseH = getHeight()/16;//~~ variabel deklarationer för lokala variabler ska vara i början av en metod

        float baseW = getWidth()/6;//~~ variabel deklarationer för lokala variabler ska vara i början av en metod

        float WallR = (dungenWall.getStrokeWidth()/2);//~~ variabel deklarationer för lokala variabler ska vara i början av en metod

        // The Dungen :,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,(,:,( {

        // se antäknings booken för att se vad siffrorna står för

        // första vägen
        canvas.drawLine(MinWidth,baseH*2 + MinHight, (MinWidth + baseW) + WallR, baseH*2 + MinHight, dungenWall); // 1

        canvas.drawLine(MinWidth + baseW, (baseH*2 + MinHight), MinWidth + baseW, baseH*4 + MinHight, dungenWall); // 2



        // andra vägen
        canvas.drawLine(MinWidth + baseW*2, MinHight, MinWidth + baseW*2, baseH*2 + MinHight, dungenWall); // 3

        canvas.drawLine(MinWidth + baseW*2, baseH*2 + MinHight, MinWidth + baseW*3, baseH*2 + MinHight, dungenWall); // 4

        canvas.drawLine(MinWidth + baseW*3, baseH*2 + MinHight, MinWidth + baseW*3, baseH*4 + MinHight, dungenWall); // 5

        canvas.drawLine(MinWidth + baseW*3, baseH*4 + MinHight, MinWidth + baseW*2, baseH*4 + MinHight, dungenWall); // 6

        canvas.drawLine(MinWidth + baseW*2, baseH*4 + MinHight, MinWidth + baseW*2, baseH*8 + MinHight, dungenWall); // 7

        canvas.drawLine(MinWidth + baseW*2, baseH*6 + MinHight, MinWidth + baseW, baseH*6 + MinHight, dungenWall); // 8

        canvas.drawLine(MinWidth + baseW, baseH*8 + MinHight, MinWidth + baseW*3, baseH*8 + MinHight, dungenWall); // 9

        canvas.drawLine(MinWidth + baseW, baseH*8 + MinHight, MinWidth + baseW, baseH*10 + MinHight, dungenWall); // 10


        // tredje vägen
        canvas.drawLine(MinWidth + baseW*2 ,MaxHight, MinWidth + baseW*2 , (baseH*10 + MinHight) - 10, dungenWall); // 11

        canvas.drawLine(MinWidth + baseW*2 , baseH*10 + MinHight, MinWidth + baseW*3 , baseH*10 + MinHight, dungenWall); // 12



        // fjärde vägen
        canvas.drawLine(MinWidth + baseW*4,MaxHight, MinWidth + baseW*4, baseH*2 + MinHight - 10, dungenWall); // 13

        canvas.drawLine(MinWidth + baseW*4,baseH*8 + MinHight, MinWidth + baseW*5, baseH*8 + MinHight, dungenWall); // 14

        canvas.drawLine(MinWidth + baseW*5, baseH*8 + MinHight + 10, MinWidth + baseW*5, baseH*6 + MinHight, dungenWall); // 15

        canvas.drawLine(MinWidth + baseW*4, baseH*2 + MinHight, MinWidth + baseW*5, baseH*2 + MinHight, dungenWall); // 16

        canvas.drawLine(MinWidth + baseW*4, baseH*6 + MinHight, MinWidth + baseW*3, baseH*6 + MinHight, dungenWall); // 17


        // femte vägen
        canvas.drawLine(MaxWidth,baseH*4 + MinHight, MinWidth + baseW*5, baseH*4 + MinHight, dungenWall); // 18

        // sjätte vägen
        canvas.drawLine(MinWidth + baseW*5,MaxHight, MinWidth + baseW*5,MinHight + baseH*10, dungenWall); // 19

        // alla Dungun vägarna}


        // Kullans position på skärmen{
        //  marbW = (Swhith / 2);
        //  marbH = (Shight / 2);

        // vart kulan ska starta
        if (Time == 0){

            marbW = 0.05f * canvas.getWidth();
            marbH = 0.05f * canvas.getHeight();

            canvas.drawBitmap(marb, marbW, marbH, null);

            Time += 1;
        }else{ // vart kulan ska åka efter en touch på kontrolen

            canvas.drawBitmap(marb, marbW, marbH, null);

        }

        // Kullan ritad}


        float Hdy = (holey - marbH);
        float Hdx = (holex - marbW);

        float holedis = (Hdy*Hdy) + (Hdx*Hdx);



        // tar fram användarens tutch positon i x och y axeln på skärmen som text
        canvas.drawText(Float.toString(x),20,40,paint1);
        canvas.drawText(Float.toString(y),20,140,paint1);
        canvas.drawText(Float.toString(marbW),20,340,paint1);
        canvas.drawText(Float.toString(marbH),20,440,paint1);
        canvas.drawText(Float.toString(Time),20,740,paint1);

        canvas.drawText(Float.toString(holedis),20,840,paint1);

        canvas.drawText(Float.toString(Vx),20,940,paint1);
        canvas.drawText(Float.toString(Vy),20,1040,paint1);

        // (canvas.getHeight()/100)*85- joysitck.getHeight()/2 TOP
        // canvas.getWidth()/2- joysitck.getWidth()/2 LEFT




        // joysticken i sina positoner
        if (x == 0 && y == 0){

            // joystickens mittpunkt på skärmen när du inte trycker på skärmen
            joystickx = 0.5f * canvas.getWidth()- joysitck.getWidth()/2;
            joysticky = 0.87f * canvas.getHeight()- joysitck.getHeight()/2;

            // om ingen rör skärmen hamnar joysticken
            canvas.drawBitmap(joysitck, joystickx, joysticky, null);
        }
        else {

            //  när man rör kontrollen (så att personen ska kunna se joysticken så sa jag att den skulle sticke ut med kullans höjd jämte med fingeret)
            canvas.drawBitmap(joysitck, x-joysitck.getWidth()/2, y-joysitck.getHeight(), null);
        }

        //y-joysitck.getHeight() TOP   x-joysitck.getWidth()/2 LEFT
        Log.i(tag,"onDrow");

    }



    public class MySurficeThread extends AsyncTask<Void,Void,Void>{

        SurfaceHolder mSurfaceHolder;

        CustomSurfaceView cSurfaceView;

        public MySurficeThread(SurfaceHolder sh, CustomSurfaceView csv) {


            mSurfaceHolder = sh;

            cSurfaceView = csv;

            x = y = 0;

            Vx = Vy = 0;

            // när personen startar att röra skärmen händer det under

            csv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // användarens truck positon blir toutchX och toutchY

                    x = event.getX();
                    y = event.getY();


                    // Värden man beräknar för nya positoner och hastigheter för kulan marbW, marbH,Vx,Vy
                    // Värden man beräknar för rörelsen av joysticken x, y
                    calculateValues(x,y,marbW,marbH,Vx,Vy);

                    switch (event.getAction() & MotionEvent.ACTION_MASK){

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

                //~~ float mx,float behöver inte användas som parameterar.
                //värdena som skickas in används aldrig. det räcker med att sätta dessa som värden på marbH och marbW
                //du skickar in marbW,marbH till dessa parametrar, men marbW,marbH används aldrig

                //Det räcker med att t.ex. sätta
                //marbW =(float) (marbW + Vx * Dt + G * marbMovementB * (Dt*Dt) * 0.5);

                //istället för

                //mx =(float) (marbW + Vx * Dt + G * marbMovementB * (Dt*Dt) * 0.5);
                //marbW = mx;


                // på samma sätt är my onödig

                //float Vy,Vx; är onödiga som parametrar, eftersom de är instansvariabler, vilket betyder att du
                //har tillgång till dem överallt i din klass

                //Jag är ej helt säker på om de första variablerna ska heta float userTouchX, float userTouchY
                //men du skickar in x och y, så jag antog det.
                private void calculateValues(float userTouchX, float userTouchY, float mx,float my, float VX, float VY) {


                    //  float finJstickx = (joystickx + (getWidth()/100)*10);
                    //  float finJsticky = (joysticky + (getHeight()/100)*10);

                    // joystickens center positon för rörelsen av joysticken när skärmen blir tryckt

                    dx = (userTouchX - joystickx) - (getWidth() / 100) * 5;
                    dy = (userTouchY - joysticky) - (getHeight() / 100) * 5;


                    // Hypotenusan av rörelseområdet för joysticken

                    Jhypotenusan = (float) Math.sqrt(dx * dx + dy * dy);

                    // hur man tar fram vinkeln av med hjälp

                    angle = (float) Math.atan(Math.abs(dy / dx));

                    // Olles formel

                    marbMovementA =(float) ((dx/Jhypotenusan) * 0.15);

                    marbMovementB =(float) ((dy/Jhypotenusan) * 0.15);


                    // float marbAngle = Math.atan(Math.abs((getHeight()/2)/(getWidth()/2)));

                    // A = Math.sqrt((radius * Math.cos(angle))* (radius * Math.cos(angle))+ (radius * Math.sin(angle))*(radius * Math.sin(angle)));

                   /* int cX = cSurfaceView.getWidth();
                    int  cY = cSurfaceView.getHeight();
                    int areaC = cX * cY;
*/
                    //  MinWidth = getWidth()/100 MaxWidth = (getWidth()/100)*106) MinHight = getHeight()/100 MaxHight = (getHeight()/100)*75

                    my = (float) (marbH + Vy * Dt + G * marbMovementA * (Dt*Dt) * 0.5);

                    marbH = my;

                    mx =(float) (marbW + Vx * Dt + G * marbMovementB * (Dt*Dt) * 0.5);

                    marbW = mx;



                    Vx = VX + Dt * G * marbMovementA;
                    Vy = VY + Dt * G * marbMovementB;




                    if (Jhypotenusan > radius) {


                        if (dx > 0 && dy > 0) { // botten höger

                            // marbW > MaxWidth || marbW < MinWidth && marbH > MaxHight || marbH < MinHight


                            userTouchX = (float) (joystickx + (radius * Math.cos(angle)));
                            userTouchY = (float) (joysticky + (radius * Math.sin(angle)));



                            //    mx =(float) (marbW + (MinWidth * Math.sin(angle)));
                            //    my =(float) (marbH + (MinHight * Math.cos(angle)));
                            //  for (marbW = mx ; ; mx =(float) (radius/10 * Math.cos(angle)));
                            //  for (marbH = my ; ; my =(float) (radius/10 * Math.sin(angle)));


                            // mx =(float) (marbW + (radius * Math.cos(angle)));
                            // my =(float) (marbW + ( * Math.cos(angle)));



                        } else if (dx > 0 && dy < 0) { //toppen höger

                            userTouchX = (float) (joystickx + (radius * Math.cos(angle)));
                            userTouchY = (float) (joysticky - (radius * Math.sin(angle)));

                            //   mx = (float) (marbW + (MinWidth * Math.sin(angle)));

                            //   my = (float) (marbH - (MaxHight * Math.cos(angle)));

                        } else if (dx < 0 && dy > 0) { //botten vänster

                            userTouchX = (float) (joystickx - (radius * Math.cos(angle)));
                            userTouchY = (float) (joysticky + (radius * Math.sin(angle)));

                            //    mx = (float) (marbW - (MinWidth * Math.sin(angle)));

                            //    my = (float) (marbH + (MaxHight * Math.cos(angle)));

                        } else if (dx < 0 && dy < 0) { //toppen vänster

                            userTouchX = (float) (joystickx - (radius * Math.cos(angle)));
                            userTouchY = (float) (joysticky - (radius * Math.sin(angle)));

                            //   mx = (float) (marbW - (MaxWidth * Math.sin(angle)));

                            //   my = (float) (marbH - (MaxHight * Math.cos(angle)));

                            //    for (marbW = marb.getWidth(), marbH = marb.getHeight();role = true; marbW -= (radius/Jhypotenusan), marbH -= (radius/Jhypotenusan));

                            //             marbW = (float) (marbW - (radius * Math.cos(angle)));
                            //             marbH = (float) (marbH - (radius * Math.sin(angle)));
                        }

                        // vad kulan får för rörelse yta om man har en touch utanför den animerade cirkeln

                        x = userTouchX + ((getWidth() / 100) * 9) / 2;
                        y = userTouchY + ((getHeight() / 100) * 13) / 2;

                    }
                }
            });


        }
/*

  Log.d("marbW ", "print1: :" + String.valueOf(marbW));
                            Log.d("marbH ", "print1: :" + String.valueOf(marbH));

                            Log.d("radius ", "printR: :" + String.valueOf(radius));

                            Log.d("dx ", "printDX: :" + String.valueOf(dx));
                            Log.d("dy ", "printDY: :" + String.valueOf(dy));

                            Log.d("marbW2 ", "print2: :" + String.valueOf(marbW));
                            Log.d("marbH2 ", "print2: :" + String.valueOf(marbH));

 */
        // Spelning av app så att allt går

        @Override
        protected Void doInBackground(Void... voids) {

            while (run) {


                // Att kulan ska röra sig görs här {
                if (x != 0 && y != 0){// NÄR DU RÖR KONTROLEN HÄNDER ÄNDRING AV VELOSETY / HASTIGHET

                    Vx = Vx + Dt * G * marbMovementA;
                    Vy = Vy + Dt * G * marbMovementB;


                    marbH = (float) (marbH + Vy * Dt + G * marbMovementA * (Dt*Dt) * 0.5);

                    marbW =(float) (marbW + Vx * Dt + G * marbMovementB * (Dt*Dt) * 0.5);


                } else {

                    marbH = (float) (marbH + Vy * Dt + G * marbMovementA * (Dt * Dt) * 0.5);

                    marbW = (float) (marbW + Vx * Dt + G * marbMovementB * (Dt * Dt) * 0.5);

                }
                // Att kulan rör sig}

                //    MaxWidth, MaxHight, MinHight, MinWidth, marbW, marbH, Vx, Vy, wallH, marbRW
                collisionDetection(Vx, Vy);

                //    marbW, marbH, holey, holex, Vx, Vy, G, marbMovementA, marbMovementB, Time, marbRW
                fallDetection(Vx, Vy, marbMovementA, marbMovementB, Time);

                Canvas canvas = null;

                try {

                    canvas = mSurfaceHolder.lockCanvas(null);

                    // joysticky = getWidth()/2;
                    // zerox = getHeight()/20*19;

                    // joysticky = (canvas.getHeight()/20)*19;


                    synchronized (mSurfaceHolder) {

                        // ritar fram draw functionen
                        cSurfaceView.draw(canvas, x, y, marbW, marbH);

                        //  BitmapFactory.decodeResource(marb, joystickx, joysticky,(int) marbW,(int) marbH);
                        // cSurfaceView.onDraw(marb, marbW,marbH, null);


                        Time += 1;
                    }
                    // vissar en ändring var 10 milisecund
                    Thread.sleep(Dt);
                } catch (InterruptedException e) {

                } finally {

                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

            }
            return null;
        }

        // (alla typer av object som används)
        //  float MaxWidth, float MaxHight, float MinHight, float MinWidth, float marbW, float marbH, float Vx, float Vy, float wallH, float marbRW

        //~~ float vx,float vy behöver inte användas som parameterar.
        //variablerna är onödiga. Det räcker med att t.ex. sätta
        //Vx = - Vx * wallH;
        //istället för

        //vx = - Vx * wallH;
        //Vx = vx;

        // på samma sätt är float vy onödig
         // förstår du vad jag menar?
        private void collisionDetection(float vx, float vy) {



            // så den stutsar när den har åkt på vägarna

            float baseH = getHeight()/16;

            float baseW = getWidth()/6;

            float WallThcc = 20;

            float WallR = WallThcc/2;

            // Dungen Ramen{

            if (marbH < MinHight + (WallR)) {

                vy = - Vy * wallH;

                Vy = vy;
            }

            if ( marbW < (MinWidth + (WallR))) {
//~~ det är alltid en onödig rad i koden innanför dessa if-satser.
                //det räcker med  Vx = - Vx * wallH;
                //vx är en onödig variabel.
                vx = - Vx * wallH;//~~varför blir hastigheten i X = - Vx * wallH
                //alltså varför multiplicerar du med väggens höjd? Jag behöver veta för att förstå hur du tänker här.
                Vx = vx;
            }

            // MAX FOR
            if (marbH > MaxHight - marb.getHeight() - (WallR)){

                vy =  - Vy * wallH;

                Vy = vy;
            }

            if (marbW > (MaxWidth - marb.getWidth() - (WallR))){

                vx =  - Vx * wallH;

                Vx = vx;
            }


            // Dungen Ramen}


            // Väg 1{

            // 1 klar
            float W1minw = MinWidth;//~~variabeln W1minw används aldrig
            float W1maxw = MinWidth + baseW;

            float W1h = MinHight + baseH * 2;


            // laburint vägarnas stuts :/ osäker om det är rätt!

//~~ vad står dessa if-satser för? Jag behöver veta för att förstå hur du har tänkt här.
            if ( ((marbH < (W1h) + (WallR)) && (marbH > (W1h - marbRW*2) - (WallR))) && marbW < W1maxw - 10){

                // marbH = MinHight +(baseH) - 15;
//~~ det är alltid en onödig rad i koden innanför dessa if-satser.
                //det räcker med  Vy = -Vy * wallH;
                //vy är en onödig variabel.
                vy = -Vy * wallH;

                Vy = vy;
            }

            // ytre hörn

            if ( (marbH > ((W1h - marbRW*2) - (WallR)) && marbH < (W1h + (WallR))) && (marbW > W1maxw - (WallR)) && (marbW < W1maxw + 10)){

                vx = -Vx * wallH;

                Vx = vx;
            }


            // väggar vågrätt   MinWidth + baseW, baseH*2 + MinHight, MinWidth + baseW, baseH*4 + MinHight

            // 2 klar
            float W2w = MinWidth + baseW;

            float W2minh = MinHight + baseH*2;
            float W2maxh = MinHight + baseH*4;

//~~ varför -10 i dessa formler? borde det inte vara relativt till höjd eller bredd?
            if ((marbW > (W2w - marbRW*2) - (WallR)) && (marbW < (W2w) + (WallR)) && marbH < W2maxh -10 && marbH > W2minh){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            // Väg kant
            if ( (marbW > ((W2w - marbRW*2) - (WallR)) && marbW < (W2w + (WallR))) && (marbH < W2maxh) && (marbH > W2maxh - 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg 1} Klar

            // Väg 2{

            // 3 klar
            float W3w = MinWidth + baseW*2;

            float W3maxh = MinHight + baseH*2;


            if ((marbW > (W3w - marbRW*2) - (WallR)) && (marbW < (W3w) + (WallR)) && marbH < W3maxh){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            /* ytre hörn
            if ( (marbW > ((W3w - marbRW*2) - (WallR)) && marbW < (W3w + (WallR))) && (marbH < W3maxh) && (marbH > W3maxh - 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }
*/
            // 4

            float W4h = MinHight + baseH*2;

            float W4minw = baseW*2 + MinWidth;
            float W4maxw = baseW*3 + MinWidth;

            if ( (marbH > (W4h - marbRW*2 - (WallR)) && marbH < (W4h + WallR)) && marbW > W4minw && marbW < W4maxw){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;

            }

            // Väg kant

            if ( (marbH > (W4h -  marbRW*2 - (WallR)) && marbH < (W4h + WallR)) && (marbW > W4maxw - 10) && (marbW < W4maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }

            // 5

            float W5w = MinWidth + baseW*3;

            float W5minh = baseH*2 + MinHight;
            float W5maxh = baseH*4 + MinHight;


            if ((marbW > (W5w - marbRW*2) - (WallR)) && (marbW < (W5w) + (WallR)) && marbH < W5maxh -10 && marbH > W5minh){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            // Väg kant
            if ( (marbW > ((W5w - marbRW*2) - (WallR)) && marbW < (W5w + (WallR))) && (marbH < W5maxh) && (marbH > W5maxh - 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // 6

            float W6h = MinHight + baseH*4;

            float W6minw = baseW*2 + MinWidth;
            float W6maxw = baseW*3 + MinWidth;

            if ( (marbH > (W6h - marbRW*2 - (WallR)) && marbH < (W6h + WallR)) && marbW > W6minw && marbW < W6maxw){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;


            }

            // Väg kant

            if ( (marbH > (W6h -  marbRW*2 - (WallR)) && marbH < (W6h + WallR)) && (marbW > W6maxw - 10 ) && (marbW < W6maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }

            // 7

            float W7w = MinWidth + baseW*2;

            float W7minh = baseH*4 + MinHight;

            float W7maxh = baseH*7 + MinHight;


            if ((marbW > (W7w - marbRW*2) - (WallR)) && (marbW < (W7w) + (WallR)) && (marbH < W7maxh) && (marbH > W7minh) ){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;

            }

            // ytre hörn
            if ( (marbW > ((W7w - marbRW*2) - (WallR)) && marbW < (W7w + (WallR))) && (marbH < W7maxh) && (marbH > W7maxh - 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // 8

            float W8h = MinHight + baseH*6;

            float W8minw = baseW + MinWidth;
            float W8maxw = baseW*2 + MinWidth;

            if ( (marbH > (W8h - marbRW*2 - (WallR)) && marbH < (W8h + WallR)) && marbW > W8minw && marbW < W8maxw - 10){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if ( (marbH > (W8h -  marbRW*2 - (WallR)) && marbH < (W8h + WallR)) && (marbW > W8maxw - 10 ) && (marbW < W8maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }


            // 9

            float W9h = MinHight + baseH*7;

            float W9minw = baseW + MinWidth;
            float W9maxw = baseW*3 + MinWidth;

            if ( (marbH > (W9h - marbRW*2 - (WallR)) && marbH < (W9h + WallR)) && marbW > W9minw && marbW < W9maxw - 10){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if ( (marbH > (W9h -  marbRW*2 - (WallR)) && marbH < (W9h + WallR)) && (marbW > W9maxw - 10 ) && (marbW < W9maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }


            // 10

            float W10w = MinWidth + baseW;

            float W10minh = baseH*2 + MinHight;
            float W10maxh = baseH*4 + MinHight;


            if ((marbW > (W10w - marbRW*2) - (WallR)) && (marbW < (W10w) + (WallR)) && marbH < W10maxh -10 && marbH > W10minh){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            // Väg kant
            if ( (marbW > ((W10w - marbRW*2) - (WallR)) && marbW < (W10w + (WallR))) && (marbH < W10maxh) && (marbH > W10maxh - 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }


            // Väg 2}

            // Väg 3 {

            // 11 klar
            float W11w = MinWidth + baseW*2;

            float W11minh = baseH*9 + MinHight;


            if ((marbW > (W11w - marbRW*2) - (WallR)) && (marbW < (W11w) + (WallR)) && marbH > W11minh + (10)){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;

            }

            // ytre hörn
            if ( (marbW > ((W11w - marbRW*2) - (WallR)) && marbW < (W11w + (WallR))) && (marbH > W11minh) && (marbH < W11minh + 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // 12 klar

            float W12h = MinHight + baseH*9;

            float W12minw = baseW*2 + MinWidth;
            float W12maxw = baseW*3 + MinWidth;

            if ( (marbH > (W12h - (WallR)) && marbH < (W12h + marbRW*2) + (WallR)) && marbW > W12minw && marbW < W12maxw - 10){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;

            }

            // Väg kant

            if ( (marbH > (W12h - (WallR)) && marbH < (W12h + marbRW*2) + (WallR)) && (marbW > W12maxw - 10 ) && (marbW < W12maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }

            // Väg 3} Klar

            // Väg 4 {

            // 13
            float W13w = MinWidth + baseW*4;

            float W13minh = baseH*1 + MinHight;


            if ((marbW > (W13w - marbRW*2) - (WallR)) && (marbW < (W13w) + (WallR)) && marbH > W13minh + 10){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            // ytre hörn   (((marbW > W13minh - 10) && (marbW < W13minh)) ||
            if ( (marbW > ((W13w - marbRW*2) - (WallR)) && marbW < (W13w + (WallR))) &&  (marbH > W13minh ) && (marbH < W13minh  + 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // 14

            float W14h = MinHight + baseH*7;

            float W14minw = baseW*4 + MinWidth;
            float W14maxw = baseW*5 + MinWidth;

            if ( (marbH > (W14h - (WallR)) && marbH < (W14h + marbRW*2) + (WallR)) && marbW > W14minw && marbW < W14maxw - (10)){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if (marbH > (W14h - (WallR)) && marbH < (W14h + WallR) && (marbW > W14maxw - (10)) && (marbW < W14maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }

            // 15
            float W15w = baseW*5 + MinWidth;

            float W15minh = MinHight + baseH*5;
            float W15maxh = MinHight + baseH*7;


            if ((marbW > (W15w - marbRW*2) - (WallR)) && (marbW < (W15w) + (WallR)) && marbH < W15maxh && marbH > W15minh + 10){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;

                Vx = vx;
            }

            // Väg kant
            if ( (marbW > ((W15w - marbRW*2) - WallR) && marbW < (W15w + (WallR))) && (marbH > W15minh && (marbH < W15minh + 10))){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // 16

            float W16h = MinHight + baseH*2;

            float W16minw = baseW*4 + MinWidth;
            float W16maxw = baseW*5 + MinWidth;

            if ( (marbH > (W16h - marbRW*2 - (WallR)) && marbH < (W16h) + (WallR)) && marbW > W16minw && marbW < W16maxw - (10)){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if (marbH > (W16h - marbRW*2 - (WallR)) && marbH < (W16h + WallR) && (marbW > W16maxw - (10)) && (marbW < W16maxw)){

                vx = -Vx * wallH;

                Vx = vx;
            }

            // 17

            float W17h = MinHight + baseH*6;

            float W17minw = baseW*3 + MinWidth;
            float W17maxw = baseW*4 + MinWidth;

            if ( (marbH > (W17h - marbRW*2 - (WallR)) && marbH < (W17h) + (WallR)) && marbW > W17minw -(10) && marbW < W17maxw){

                // marbH = MinHight +(baseH) - 15;

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if (marbH > (W17h - (WallR)) && marbH < (W17h + WallR) && (marbW > W17maxw) && (marbW < W17maxw - (10))){

                vx = -Vx * wallH;

                Vx = vx;
            }

                 /*

        canvas.drawLine(MinWidth + baseW*4, baseH*6 + MinHight, MinWidth + baseW*3, baseH*6 + MinHight, dungenWall); // 17

*/

            // Väg 4 }

            // Väg 5 & 6{

            // 18 = 5 klar

            float W18h = MinHight + baseH*4.5f;

            float W18minw = baseW*5 + MinWidth;
            float W18maxw = MaxWidth;//~~variabeln används inte och borde tas bort.

            if ( (marbH < (W18h + (WallR)) && marbH > (WallR) - (WallR)) && marbW > W18minw + 10){

                // marbH = MinHight +(baseH) - 15;  + (Swhith*0.025f/2)

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg kant

            if  (marbH > ((W18h - marbRW*2) - (WallR)) && marbH < W18h + (WallR) && (marbW > W18minw) && (marbW < W18minw + 10)) {

                vx = -Vx * wallH;

                Vx = vx;
            }
            // 19 = 6 klar

            float W19w = MinWidth + baseW*5;

            float W19minh = MinHight + baseH*9;
            float W19maxh = MaxHight;//~~variabeln W19maxh används inte och borde tas bort.

            if ((marbW > (W19w - marbRW*2) - WallR) && (marbW < (W19w) + (WallR)) && marbH > W19minh + (10)){

                // marbH = MinHight +(baseH) - 15;

                vx = -Vx * wallH;
                Vx = vx;
            }

            // Väg kant
            if ( (marbW > ((W19w - marbRW*2) - (WallR)) && marbW < (W19w + (WallR))) && (marbH > W19minh ) && (marbH < W19minh + 10)){

                vy = -Vy * wallH;

                Vy = vy;
            }

            // Väg 5 & 6} klar

        }
    }

    // (alla typer av object som används)
    // float marbW, float marbH, float holey, float holex, float Vx, float Vy, float G, float marbMovementA, float marbMovementB, int Time ,float marbRW

    //~~ float vy, float sa, float sb, int time behöver inte användas som parameterar.
    //du skickar in Vx, Vy, marbMovementA, marbMovementB, Time till dessa parametrar,
    // men Vx, Vy, marbMovementA, marbMovementB, Time  används aldrig

    //variablerna är onödiga. Det räcker med att t.ex. sätta

    //Time=0;

    //istället för

    //  time = 0;
    //  Time = time;

    // på samma sätt är float vx och vy onödiga
    //det borde varit

    // Vx =0; Vy=0;

    //istället för

    //vx = vy = 0;
    //Vx = vx;

    // förstår du vad jag menar?
    private void fallDetection(float vx, float vy, float sa, float sb, int time) {





        // Hålle 1
        float Hdy1 = (holey*6.4f - marbH) - (getHeight() / 100) * 3;//~~borde vara *0.03f istället

        float Hdx1 = (holex*5 - marbW) - (getHeight() / 100) * 3;

        // Hålle 2

        float Hdy2 = (holey*14.8f - marbH) - (getHeight() / 100) * 3;

        float Hdx2 = (holex*5 - marbW) - (getHeight() / 100) * 3;

        // hålle 3
        float Hdy3 = (holey*14.8f - marbH) - (getHeight() / 100) * 3;

        float Hdx3 = (holex*9.1f - marbW) - (getHeight() / 100) * 3;


        // Hålle 4
        float Hdy4 = (holey*2f - marbH) - (getHeight() / 100) * 3;

        float Hdx4 = (holex*11 - marbW) - (getHeight() / 100) * 3;



        // Hålle 5
        float Hdy5 = (holey*22.4f - marbH) - (getHeight() / 100) * 3;

        float Hdx5 = (holex*1.1f - marbW) - (getHeight() / 100) * 3;


        // Hålle 6
        float Hdy6 = (holey*22.4f - marbH) - (getHeight() / 100) * 3;

        float Hdx6 = (holex*11 - marbW) - (getHeight() / 100) * 3;


        // holets distans tills den ska ramla i   - marbW  - marbH (marb.getWidth()/2)*marb.getWidth()/2)
//~~är du helt säker på att det inte ska vara roten ur detta?
        //~~stämmer dessa formler, alltså ramlar kulan alltid in i hålet på rätt sätt?
        //~~ i så fall kan du bortse från min kommentar här.
        float holedis1 =((Hdy1*Hdy1) + (Hdx1*Hdx1));

        float holedis2 =((Hdy2*Hdy2) + (Hdx2*Hdx2));

        float holedis3 =((Hdy3*Hdy3) + (Hdx3*Hdx3));

        float holedis4 =((Hdy4*Hdy4) + (Hdx4*Hdx4));

        float holedis5 =((Hdy5*Hdy5) + (Hdx5*Hdx5));

        float holedis6 =((Hdy6*Hdy6) + (Hdx6*Hdx6));


        // Håll 1
        if (holedis1 < marbRW*marbRW){


            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;
        }

        // Håll 2
        if (holedis2 < marbRW*marbRW){

            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;
        }

        // Håll 3
        if (holedis3 < marbRW*marbRW){

            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;

        }


        // Håll 4
        if (holedis4 < marbRW*marbRW){

            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;

        }


        // Håll 5
        if (holedis5 < marbRW*marbRW){

            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;

        }

        // Håll 6
        if (holedis6 < marbRW*marbRW){

            time = 0;
            Time = time;

            vx = vy = 0;
            Vx = vx;
            Vy = vy;

            sa = sb = 0;
            marbMovementA = sa;
            marbMovementB = sb;

        }
    }
}
