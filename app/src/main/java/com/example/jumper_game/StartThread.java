package com.example.jumper_game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class StartThread extends Thread{
        public static final int MAX_FPS = 60;
        private double avarageFPS;
        private SurfaceHolder surfaceHolder;
        private Game game;
        private boolean running;
        private static Canvas canvas;

        public StartThread(SurfaceHolder surfaceHolder, Game game){
            super();
            this.surfaceHolder = surfaceHolder;
            this.game = game;
        }

        public void setRunning(Boolean running){
            this.running = running;
        }

        @Override
        public void run(){
            long startime;
            long timeMillis = 100/MAX_FPS;
            long waitTime;
            int frameCount = 0;
            long totalTime = 0;
            long targetTime = 1000/MAX_FPS;

            while(running){
                startime = System.nanoTime();
                canvas = null;

                try{
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder){
                        this.game.update();
                        this.game.draw(canvas);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    if(canvas != null){
                        try{
                            surfaceHolder.unlockCanvasAndPost(canvas );
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                timeMillis = (System.nanoTime() - startime)/1000000;
                waitTime = targetTime - timeMillis;
                try{
                    if(waitTime > 0)
                        this.sleep(waitTime);
                } catch (Exception e){
                    e.printStackTrace();
                }

                totalTime +=System.nanoTime() - startime;
                frameCount++;
                if(frameCount == MAX_FPS){
                    avarageFPS = 1000/(totalTime/frameCount/1000000);
                    frameCount = 0;
                    totalTime = 0;
                    System.out.println(avarageFPS);
                }
            }
        }
    }