package com.obstacleavoid.v2.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.obstacleavoid.v2.ObstacleAvoidGame
import com.obstacleavoid.v2.config.GameConfig


fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.width = GameConfig.WIDTH
    config.height = GameConfig.HEIGHT
    LwjglApplication(ObstacleAvoidGame(), config)
}