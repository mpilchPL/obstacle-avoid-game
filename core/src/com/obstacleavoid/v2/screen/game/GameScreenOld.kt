package com.obstacleavoid.v2.screen.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.obstacleavoid.v2.assets.AssetPaths
import com.obstacleavoid.v2.config.DifficultyLevel
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.entity.Obstacle
import com.obstacleavoid.v2.entity.Player
import com.obstacleavoid.v2.utils.*
import com.obstacleavoid.v2.utils.debug.DebugCameraController

@Deprecated("use GameScreen", ReplaceWith("GameScreen"), DeprecationLevel.WARNING)
class GameScreenOld : Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var uiCamera: OrthographicCamera
    private lateinit var uiViewport: Viewport
    private lateinit var uiFont: BitmapFont
    private lateinit var renderer: ShapeRenderer
    private lateinit var player: Player
    private lateinit var debugCameraController: DebugCameraController
    private lateinit var batch: SpriteBatch

    private var obstaclesTimer = 0f
    private var difficultyLevel = DifficultyLevel.MEDIUM
    private var scoreTimer = 0f
    private var score = 0
    private val obstacles = GdxArray<Obstacle>()
    private var lives = GameConfig.LIVES_START
    private val layout = GlyphLayout()
    private val padding = 20f
    private val gameOver
        get() = lives <= 0


    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
        uiCamera = OrthographicCamera()
        uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
        batch = SpriteBatch()
        uiFont = BitmapFont(AssetPaths.SITKA_FONT.toInternalFile())
        renderer = ShapeRenderer()

        debugCameraController = DebugCameraController()
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
        player = Player()

        val startPlayerX = GameConfig.WORLD_WIDTH / 2f
        val startPlayerY = 1f

        player.setPosition(startPlayerX, startPlayerY)

    }

    override fun render(delta: Float) {
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        if (!gameOver) {
            update(delta)
        }


        clearScreen()
        renderer.projectionMatrix = camera.combined

        renderer.use {
//            player.drawDebug(renderer)
//            obstacles.forEach { it.drawDebug(renderer) }
        }

        renderUi()
        viewport.drawGrid(renderer)
    }

    private fun renderUi() {
        batch.projectionMatrix = uiCamera.combined
        batch.use {
            val livesText = "LIVES: $lives"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)

            val scoreText = "SCORE: $score"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - padding, GameConfig.HUD_HEIGHT - layout.height)
        }
    }

    private fun update(delta: Float) {
        //player.update()
        updateObstacles(delta)
        createNewObstacle(delta)
        if (playerCollidesWithObstacle()) {
            lives--
        }
    }

    private fun playerCollidesWithObstacle(): Boolean {
        obstacles.forEach {
            if (it.isPlayerColliding(player) && !(it.hit)){
                it.hit = true
                return true
            }
        }
        return false
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        dispose()
    }

    private fun updateObstacles(delta: Float) {
        obstacles.forEach {
            if (!it.update()) {
                obstacles.removeValue(it, true)
                score++
            }
        }
    }


    private fun createNewObstacle(delta: Float) {
        obstaclesTimer += delta
        if (obstaclesTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            obstaclesTimer = 0f
            val obstacleX = MathUtils.random(0f, GameConfig.WORLD_WIDTH)
            val obstacleY = GameConfig.WORLD_HEIGHT + Obstacle.BOUNDS_RADIUS
            val obstacle = Obstacle()
            obstacle.setPosition(obstacleX, obstacleY)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }
    }

}