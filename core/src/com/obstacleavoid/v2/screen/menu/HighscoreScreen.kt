package com.obstacleavoid.v2.screen.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.assets.AssetDescriptors
import com.obstacleavoid.v2.assets.AssetPaths
import com.obstacleavoid.v2.assets.PrefsFields
import com.obstacleavoid.v2.config.ButtonsConfig
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.utils.clearScreen
import com.obstacleavoid.v2.utils.toInternalFile
import com.obstacleavoid.v2.utils.use

class HighscoreScreen (private val game: ObstacleAvoidGame) : ScreenAdapter(){

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
    private val assets = game.assetsManager
    private var layout = GlyphLayout()
    private val font = assets[AssetDescriptors.FONT]
    private val prefs: Preferences = Gdx.app.getPreferences(PrefsFields.PREF_FILE_NAME)
    private val skin = Skin(AssetPaths.SKIN.toInternalFile())


    private lateinit var backBt: ImageTextButton
    private lateinit var style: ImageTextButton.ImageTextButtonStyle
    private lateinit var stage: Stage


    override fun show() {
        style = ImageTextButton.ImageTextButtonStyle(skin.get("default", ImageTextButton.ImageTextButtonStyle::class.java))
        stage = Stage(viewport)
        Gdx.input.inputProcessor = stage
        backBt = ImageTextButton("BACK", style).apply {
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, ButtonsConfig.BUTTON_HEIGHT + ButtonsConfig.BUTTONS_PADDING_Y)
            setSize(ButtonsConfig.BUTTON_WIDTH, ButtonsConfig.BUTTON_HEIGHT)
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.screen = MenuScreen(game)
                }
            })
        }

        stage.addActor(backBt)
    }


    override fun render(delta: Float) {
        clearScreen()
        viewport.apply()
        batch.projectionMatrix = camera.combined

        batch.use {
            layout.setText(font, prefs.getInteger(PrefsFields.BEST_SCORE_FIELD, 0).toString())
            font.draw(batch, layout, GameConfig.HUD_WIDTH / 2 - layout.width /2, GameConfig.HUD_HEIGHT /2 )
        }

        drawStage()
    }

    fun drawStage() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        stage.dispose()
    }


}