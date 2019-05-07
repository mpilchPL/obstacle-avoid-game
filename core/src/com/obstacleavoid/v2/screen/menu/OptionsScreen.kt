package com.obstacleavoid.v2.screen.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.assets.AssetDescriptors
import com.obstacleavoid.v2.assets.AssetPaths
import com.obstacleavoid.v2.assets.PrefsFields
import com.obstacleavoid.v2.assets.RegionNames
import com.obstacleavoid.v2.config.ButtonsConfig
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.utils.*

class OptionsScreen(private val game: ObstacleAvoidGame) : ScreenAdapter() {

    private val batch = SpriteBatch()
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val stage = Stage(uiViewport)
    private val skin = Skin(AssetPaths.SKIN.toInternalFile())
    private val prefs = Gdx.app.getPreferences(PrefsFields.PREF_FILE_NAME)
    private val style = CheckBox.CheckBoxStyle(skin.get("default", CheckBox.CheckBoxStyle::class.java))
    private val styleBt = ImageTextButton.ImageTextButtonStyle(skin.get("default", ImageTextButton.ImageTextButtonStyle::class.java))
    private val assetsManager = game.assetsManager
    private val atlas = assetsManager[AssetDescriptors.GAMEPLAY_ATLAS]
    private val bgTexture = atlas[RegionNames.MENU]

    private lateinit var backBt: ImageTextButton
    private lateinit var easyCb: CheckBox
    private lateinit var mediumCb: CheckBox
    private lateinit var hardCb: CheckBox
    private lateinit var buttonGroup: ButtonGroup<CheckBox>


    override fun show() {
        Gdx.input.inputProcessor = stage


        easyCb = CheckBox("EASY", style).apply {
            align(Align.center)
            setDefaultButtonSize()
            setPosition(ButtonsConfig.BUTTONS_PADDING_X,
                    GameConfig.HUD_HEIGHT - ButtonsConfig.BUTTONS_PADDING_Y - ButtonsConfig.BUTTON_HEIGHT)
            if (prefs.getString(PrefsFields.DIFFICULTY_FIELD) == PrefsFields.EASY)
                isChecked = true
        }

        mediumCb = CheckBox("MEDIUM", style).apply {
            align(Align.center)
            setDefaultButtonSize()
            setPosition(ButtonsConfig.BUTTONS_PADDING_X,
                    easyCb.y - ButtonsConfig.BUTTONS_PADDING_Y - ButtonsConfig.BUTTON_HEIGHT)
            if (prefs.getString(PrefsFields.DIFFICULTY_FIELD) == PrefsFields.MEDIUM)
                isChecked = true
        }

        hardCb = CheckBox("HARD", style).apply {
            align(Align.center)
            setDefaultButtonSize()
            setPosition(ButtonsConfig.BUTTONS_PADDING_X,
                    mediumCb.y - ButtonsConfig.BUTTONS_PADDING_Y - ButtonsConfig.BUTTON_HEIGHT)
            if (prefs.getString(PrefsFields.DIFFICULTY_FIELD) == PrefsFields.HARD)
                isChecked = true
        }

        buttonGroup = ButtonGroup(easyCb, mediumCb, hardCb)
        buttonGroup.setMaxCheckCount(1)
        buttonGroup.setMinCheckCount(1)
        buttonGroup.setUncheckLast(true)


        backBt = ImageTextButton("Back", styleBt).apply {
            setDefaultButtonSize()
            setPosition(ButtonsConfig.BUTTONS_PADDING_X, ButtonsConfig.BUTTONS_PADDING_Y + ButtonsConfig.BUTTON_HEIGHT)
            addListener(object : ChangeListener(){
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    val diff = getCheckedBox()
                    prefs.putString(PrefsFields.DIFFICULTY_FIELD, diff)
                    prefs.flush()
                    game.screen = MenuScreen(game)
                }
            })
        }

        stage.addActor(easyCb)
        stage.addActor(mediumCb)
        stage.addActor(hardCb)
        stage.addActor(backBt)


    }

    private fun getCheckedBox(): String {
        if (hardCb.isChecked())
            return PrefsFields.HARD
        if (mediumCb.isChecked())
            return PrefsFields.MEDIUM

        return PrefsFields.EASY
    }

    override fun render(delta: Float) {
        clearScreen()

        drawBackground()
        drawStage()


    }

    override fun resize(width: Int, height: Int) {
        uiViewport.update(width, height, true)
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        batch.dispose()
        stage.dispose()
    }

    private fun drawStage() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined
        stage.act()
        stage.draw()
    }

    private fun drawBackground() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.use {
            batch.draw(bgTexture, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)
        }
    }
}