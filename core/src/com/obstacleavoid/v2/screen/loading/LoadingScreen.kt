package com.obstacleavoid.v2.screen.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.assets.AssetDescriptors
import com.obstacleavoid.v2.assets.PrefsFields
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.screen.menu.MenuScreen
import com.obstacleavoid.v2.utils.clearScreen
import com.obstacleavoid.v2.utils.logger

class LoadingScreen(private val game: ObstacleAvoidGame) : ScreenAdapter() {

    companion object {
        @JvmStatic
        private val log = logger<LoadingScreen>()

        private const val PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f
        private const val PROGRESS_BAR_HEIGHT = 60f
    }

    private val assetManager = game.assetsManager
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: ShapeRenderer
    private lateinit var batch: SpriteBatch

    private var progress = 0f
    private var waitTime = 0.75f
    private var changeScreen = false
    private val prefs = Gdx.app.getPreferences(PrefsFields.PREF_FILE_NAME)


    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
        renderer = ShapeRenderer()
        batch = SpriteBatch()

        updatePreferences()

        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.GAMEPLAY_ATLAS)
        assetManager.load(AssetDescriptors.SKIN_ATLAS)


    }

    override fun render(delta: Float) {
        update(delta)
        clearScreen()

        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.begin(ShapeRenderer.ShapeType.Filled)
            draw()
        renderer.end()

        if (changeScreen) {
//            game.screen = MenuScreen(game)
            game.screen = MenuScreen(game)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        log.debug("dispose")
        renderer.dispose()
        batch.dispose()
    }

    private fun update(delta: Float) {
        progress = assetManager.progress

        if (assetManager.update()){
            waitTime -= delta
            if (waitTime <= 0){
                log.debug("Asset Manager diagnostics: ${assetManager.diagnostics}")
                changeScreen = true
            }
        }
    }

    private fun draw() {
        val progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2
        val progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2

        renderer.rect(progressBarX, progressBarY, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)

    }

    private fun updatePreferences() {
        if (!prefs.contains(PrefsFields.BEST_SCORE_FIELD))
            prefs.putInteger(PrefsFields.BEST_SCORE_FIELD, 0)
        if(!prefs.contains(PrefsFields.DIFFICULTY_FIELD))
            prefs.putString(PrefsFields.DIFFICULTY_FIELD, PrefsFields.EASY)

        prefs.flush()
    }


}