package com.obstacleavoid.v2.entity

import com.badlogic.gdx.math.Circle
import com.obstacleavoid.v2.config.GameConfig

class Player : GameObjectBase() {

    companion object {
        // constants
        const val BOUNDS_RADIUS = 0.4f
        const val SIZE = BOUNDS_RADIUS * 2f
        const val MAX_X_SPEED = 0.1f
        const val MIN_X = 0f + BOUNDS_RADIUS
        const val MAX_X = GameConfig.WORLD_WIDTH - BOUNDS_RADIUS
    }

    override val bounds: Circle = Circle(x, y, BOUNDS_RADIUS)


}