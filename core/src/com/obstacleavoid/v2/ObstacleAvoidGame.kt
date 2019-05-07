package com.obstacleavoid.v2

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Logger
import com.obstacleavoid.v2.screen.loading.LoadingScreen

class ObstacleAvoidGame : Game() {

    val assetsManager = AssetManager()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        assetsManager.logger.level = Logger.DEBUG

        setScreen(LoadingScreen(this))
    }

    override fun dispose() {
        super.dispose()
        assetsManager.dispose()
    }
}