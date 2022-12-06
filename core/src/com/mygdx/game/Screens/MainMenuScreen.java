package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.MyGdxGame;

public class MainMenuScreen implements Screen {
    MyGdxGame game;
    TiledMap map;

    Texture sprites;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        sprites = new Texture("ready.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map = new TmxMapLoader().load("map.tmx");


        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game,(TiledMapTileLayer)map.getLayers().get(0)));
        }

        game.batch.begin();
        game.batch.draw(sprites, Gdx.graphics.getWidth() / 2 - sprites.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprites.getHeight() / 2);
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
