package com.obstacleavoid.v2.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.utils.clearScreen
import com.obstacleavoid.v2.utils.drawGrid

class GameScreen : Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: ShapeRenderer

    override fun show() {
        camera = OrthographicCamera()
        camera.zoom = 1.5f
        viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
        renderer = ShapeRenderer()
    }

    override fun render(delta: Float) {
        clearScreen()
        renderer.projectionMatrix = camera.combined

        viewport.drawGrid(renderer)

    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width,height, true)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }
}