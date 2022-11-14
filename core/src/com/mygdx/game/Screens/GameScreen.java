package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Enums.Direction;
import com.mygdx.game.MyGdxGame;

public class GameScreen implements Screen {
    public static final int FRAME_COLS = 14, FRAME_ROWS = 13;
    public static final float SPEED = 400;
    public static final float ANIMATION_SPEED = 0.06f;
    public static final int SIZE_PIXEL = 16;
    public static final int SIZE = SIZE_PIXEL * 3;

    Direction DIRECTION;
    Animation<TextureRegion>[] moveAnimation;
    Texture texture;
    int frame;
    boolean animLoop;
    float stateTime;
    float x;
    float y;

    MyGdxGame game;

    public GameScreen(MyGdxGame game) {
        this.game = game;
        y = 50;
        x = MyGdxGame.WIDTH / 2 - SIZE / 2;

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
        texture = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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


        // Movement code
        x += SPEED * Gdx.graphics.getDeltaTime() * DIRECTION.getxDirection();
        y += SPEED * Gdx.graphics.getDeltaTime() * DIRECTION.getyDirection();



        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            DIRECTION = Direction.UP;
            frame = 2;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            DIRECTION = Direction.DOWN;
            frame = 3;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            DIRECTION = Direction.RIGHT;
            frame = 0;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            DIRECTION = Direction.LEFT;
            frame = 1;
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

    }

    @Override
    public void dispose() {

    }
}
