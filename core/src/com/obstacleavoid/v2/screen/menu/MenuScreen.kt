package com.obstacleavoid.v2.screen.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.assets.AssetDescriptors
import com.obstacleavoid.v2.assets.AssetPaths
import com.obstacleavoid.v2.assets.RegionNames
import com.obstacleavoid.v2.config.ButtonsConfig
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.screen.game.GameScreen
import com.obstacleavoid.v2.utils.*

class MenuScreen(private val game: ObstacleAvoidGame) : ScreenAdapter() {

    companion object {
        @JvmStatic
        private val log = logger<MenuScreen>()
    }

    private lateinit var style: ImageTextButton.ImageTextButtonStyle
    private lateinit var stage: Stage
    private lateinit var startGameBt: ImageTextButton
    private lateinit var highscoreBt: ImageTextButton
    private lateinit var optionsBt: ImageTextButton
    private lateinit var exitBt: ImageTextButton

    private var camera: OrthographicCamera = OrthographicCamera()
    private val viewport: FitViewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val batch: SpriteBatch = SpriteBatch()

    private val assetManager = game.assetsManager
    private val atlas = assetManager[AssetDescriptors.GAMEPLAY_ATLAS]
    private val font: BitmapFont = assetManager[AssetDescriptors.FONT]
    private val menuBackground = atlas[RegionNames.MENU]

    private val skin = Skin(AssetPaths.SKIN.toInternalFile())

    override fun show() {
        style = ImageTextButton.ImageTextButtonStyle(skin.get("default", TextButton.TextButtonStyle::class.java))
        stage = Stage(uiViewport)
        Gdx.input.inputProcessor = stage

        startGameBt = ImageTextButton("Start Game", style).apply {
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, GameConfig.HUD_HEIGHT - ButtonsConfig.BUTTON_HEIGHT - ButtonsConfig.BUTTONS_PADDING_Y)
            setDefaultButtonSize()
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.screen = GameScreen(game)
                }
            })
        }

        optionsBt = ImageTextButton("Options", style).apply {
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, startGameBt.y - ButtonsConfig.BUTTON_HEIGHT - ButtonsConfig.BUTTONS_PADDING_Y)
            setDefaultButtonSize()
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.screen = OptionsScreen(game)
                }
            })
        }


        highscoreBt = ImageTextButton("Highscore", style).apply {
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, optionsBt.y - ButtonsConfig.BUTTON_HEIGHT - ButtonsConfig.BUTTONS_PADDING_Y)
            setDefaultButtonSize()
            addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.screen = HighscoreScreen(game)
                }
            })
        }

        exitBt = ImageTextButton("Exit Game", style).apply {
            setDefaultButtonSize()
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, ButtonsConfig.BUTTONS_PADDING_Y + ButtonsConfig.BUTTON_HEIGHT)
            addListener(object : ChangeListener(){
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    game.dispose()
                }
            })
        }


        stage.addActor(startGameBt)
        stage.addActor(highscoreBt)
        stage.addActor(optionsBt)
        stage.addActor(exitBt)

    }

    override fun render(delta: Float) {
        clearScreen()


        drawBackground()
        //drawStrings()
        drawStage()
    }

    override fun pause() {}

    override fun resume() {}

    private fun drawBackground() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.use {
            batch.draw(menuBackground, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

        }
    }

    fun drawStrings() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined
        batch.use {
            font.draw(batch, "aaas", GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT / 2)
        }
    }

    private fun drawStage() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        stage.dispose()
    }

    override fun hide() {
        dispose()
    }
}