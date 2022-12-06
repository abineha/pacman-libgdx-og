package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.mygdx.game.Enums.Direction;
import com.mygdx.game.MyGdxGame;
import java.math.RoundingMode;

public class GameScreen implements Screen {
    public static final int FRAME_COLS = 14, FRAME_ROWS = 13;
    public static final float SPEED = 50;
    public static final float ANIMATION_SPEED = 0.06f;
    public static final int SIZE_PIXEL = 20;
    public static final int SIZE = SIZE_PIXEL * 1;

    TiledMap map;

    TiledMapRenderer mapRenderer;
    OrthographicCamera camera;

    Direction DIRECTION;
    Animation<TextureRegion>[] moveAnimation;
    Texture texture;
    int frame;
    boolean animLoop;
    float stateTime;
    float x;
    float y;
    private TiledMapTileLayer collisionLayer;
    float vx=-1,vy=1;

    MyGdxGame game;

    public GameScreen(MyGdxGame game,TiledMapTileLayer collisionLayer) {
        this.game = game;
        this.collisionLayer=collisionLayer;
        y = 140;
        x = 208;
        texture = new Texture("sprites.png");





        System.out.println(texture.getWidth() + " " + texture.getHeight());

        // create 2d array of textures
        TextureRegion[][] frameSpriteSheet = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        // create array of textures for animation loop(selected from above 2d array)
        TextureRegion[] moveFramesUP = new TextureRegion[]{
                frameSpriteSheet[0][2],
                frameSpriteSheet[2][1],
                frameSpriteSheet[2][0],
                frameSpriteSheet[2][1]
        };

        TextureRegion[] moveFramesDOWN = new TextureRegion[]{
                frameSpriteSheet[0][2],
                frameSpriteSheet[3][1],
                frameSpriteSheet[3][0],
                frameSpriteSheet[3][1]
        };

        TextureRegion[] moveFramesRIGHT = new TextureRegion[]{
                frameSpriteSheet[0][2],
                frameSpriteSheet[0][1],
                frameSpriteSheet[0][0],
                frameSpriteSheet[0][1]
        };

        TextureRegion[] moveFramesLEFT = new TextureRegion[]{
                frameSpriteSheet[0][2],
                frameSpriteSheet[1][1],
                frameSpriteSheet[1][0],
                frameSpriteSheet[1][1]
        };

        frame = 1;
        moveAnimation = new Animation[4];

        moveAnimation[0] = new Animation<TextureRegion>(ANIMATION_SPEED, moveFramesRIGHT);
        moveAnimation[1] = new Animation<TextureRegion>(ANIMATION_SPEED, moveFramesLEFT);
        moveAnimation[2] = new Animation<TextureRegion>(ANIMATION_SPEED, moveFramesUP);
        moveAnimation[3] = new Animation<TextureRegion>(ANIMATION_SPEED, moveFramesDOWN);

        DIRECTION = Direction.LEFT;




    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("map.tmx");

        mapRenderer = new OrthoCachedTiledMapRenderer(map, 1/16f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 28, 36);
        camera.update();

        texture = new Texture("sprites.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // rendering map
        mapRenderer.setView(camera);
        mapRenderer.render();


        animLoop = true;

        // Border checking
        if(x >= Gdx.graphics.getWidth() - SIZE) {
            x = Gdx.graphics.getWidth() - SIZE;
            animLoop = false;
        }
        if(x <= 0) {
            x = 0;
            animLoop = false;
        }
        if(y >= Gdx.graphics.getHeight() - SIZE) {
            y = Gdx.graphics.getHeight() - SIZE;
            animLoop = false;
        }
        if(y <= 0) {
            y = 0;
            animLoop = false;
        }

        //save old position
        float oldX,oldY,tileWidth=collisionLayer.getTileWidth(),tileHeight=collisionLayer.getTileHeight() ;
        boolean collisionX=false,collisionY=false;



        //move on x
        int s=16;

        oldX=x;
        x += SPEED * Gdx.graphics.getDeltaTime() * DIRECTION.getxDirection();
        if((vx)<0){
            //middle left

            collisionX =collisionLayer.getCell((int) (Math.round(x / tileWidth)), (int) (Math.round((y + s / 2) / tileHeight))).getTile().getProperties().containsKey("blocked");



        }else {
            // middle right

            collisionX = collisionLayer.getCell((int) (Math.round((x + s) / tileWidth)), (int) (Math.round((y+ s / 2) / tileHeight))).getTile().getProperties().containsKey("blocked");

        }

        //react to x collision
        if(collisionX){
            x=oldX;
        }

        //move on y
        oldY=y;
        y += SPEED * Gdx.graphics.getDeltaTime() * DIRECTION.getyDirection();
        if(vy<0){
            // bottom middle
            collisionY = collisionLayer.getCell((int) (Math.round((x+ s / 2) / tileWidth)), (int) (Math.round(y / tileHeight))).getTile().getProperties().containsKey("blocked");



        }else {
            // top middle
            collisionY = collisionLayer.getCell((int) (Math.round((x + s/ 2) / tileWidth)), (int) (Math.round((y + s) / tileHeight))).getTile().getProperties().containsKey("blocked");


        }

        //react to y collision
        if(collisionY){
            y=oldY;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            DIRECTION = Direction.UP;
            frame = 2;
            vy=1;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            DIRECTION = Direction.DOWN;
            frame = 3;
            vy=-1;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            DIRECTION = Direction.RIGHT;
            frame = 0;
            vx=1;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            DIRECTION = Direction.LEFT;
            frame = 1;
            vx=-1;
        }

        stateTime += delta;

        // render code
        game.batch.begin();

        game.batch.draw(moveAnimation[frame].getKeyFrame(stateTime, animLoop), x, y, SIZE, SIZE);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        map.dispose();

    }
}
