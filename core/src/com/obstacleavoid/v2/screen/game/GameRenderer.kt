package com.obstacleavoid.v2.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.obstacleavoid.v2.assets.AssetDescriptors
import com.obstacleavoid.v2.assets.RegionNames
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.entity.Obstacle
import com.obstacleavoid.v2.entity.Player
import com.obstacleavoid.v2.utils.*
import com.obstacleavoid.v2.utils.debug.DebugCameraController


class GameRenderer(private val assetManager: AssetManager,
                   private val controller: GameController) : Disposable {


    companion object {
        @JvmStatic
        private val log = logger<GameRenderer>()
    }

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)

    private val renderer: ShapeRenderer = ShapeRenderer()
    private val debugCameraController: DebugCameraController = DebugCameraController()
    private val batch: SpriteBatch = SpriteBatch()

    private val layout = GlyphLayout()
    private val padding = 20f

    private val uiFont = assetManager[AssetDescriptors.FONT]
    private val gameplayAtlas = assetManager[AssetDescriptors.GAMEPLAY_ATLAS]

    private val playerTexture = gameplayAtlas[RegionNames.PLAYER]
    private val backgroundTexture = gameplayAtlas[RegionNames.BACKGROUND]
    private val obstacleTexture = gameplayAtlas[RegionNames.OBSTACLE]

    init {
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }


    fun render() {
        batch.totalRenderCalls = 0
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        clearScreen()

        if (Gdx.input.isTouched && !controller.gameOver) {
            val screenTouchPos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            val worldTouchPos = viewport.unproject(Vector2(screenTouchPos) )

            log.debug("screenTouch: $screenTouchPos, worldTouch: $worldTouchPos")

            val player = controller.player
            var xSpeed = 0f
            when {
                worldTouchPos.x > GameConfig.WORLD_WIDTH / 2 -> xSpeed = Player.MAX_X_SPEED
                else -> xSpeed = -Player.MAX_X_SPEED
            }
            player.x = MathUtils.clamp((player.x + xSpeed), Player.MIN_X, Player.MAX_X)
        }

        renderGameplay()
        renderDebug()

        renderUi()
        viewport.drawGrid(renderer)
//        log.debug("total render calls: ${batch.totalRenderCalls}")
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.use {
            renderer.circles(controller.player.bounds)

            controller.obstacles.forEach {
                renderer.circles(it.bounds)
            }
        }
    }

    private fun renderGameplay() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.use {
            batch.draw(backgroundTexture, 0f,0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)
            val player = controller.player
            batch.draw(playerTexture,
                    player.x - Player.BOUNDS_RADIUS, player.y - Player.BOUNDS_RADIUS,
                    Player.SIZE, Player.SIZE)
            controller.obstacles.forEach {
                batch.draw(obstacleTexture,
                        it.x - Obstacle.BOUNDS_RADIUS, it.y - Obstacle.BOUNDS_RADIUS,
                        Obstacle.SIZE, Obstacle.SIZE)
            }
        }
    }

    private fun renderUi() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined
        batch.use {
            val livesText = "LIVES: ${controller.lives}"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, padding, GameConfig.HUD_HEIGHT - layout.height)

            val scoreText = "SCORE: ${controller.score}"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - padding, GameConfig.HUD_HEIGHT - layout.height)
        }
    }
}