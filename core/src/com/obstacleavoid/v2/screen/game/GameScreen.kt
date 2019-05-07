package com.obstacleavoid.v2.screen.game

import com.badlogic.gdx.Screen
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.screen.menu.MenuScreen

class GameScreen (val game: ObstacleAvoidGame): Screen {

    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer
    private val assetManager = game.assetsManager
    var gameIsOver = false


    override fun show() {
        controller = GameController()
        renderer = GameRenderer(assetManager, controller)
    }

    override fun render(delta: Float) {
        gameIsOver = controller.gameOver
        controller.update(delta)
        renderer.render()
        if (gameIsOver)
            game.screen = MenuScreen(game)
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        renderer.dispose()
    }
}