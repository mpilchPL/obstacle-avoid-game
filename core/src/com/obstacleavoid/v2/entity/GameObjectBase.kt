package com.obstacleavoid.v2.entity

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector

abstract class GameObjectBase {
    var x: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }
    var y: Float = 0f
        set(value) {
            field = value
            updateBounds()
        }
    abstract val bounds: Circle
    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    private fun updateBounds() {
        bounds.setPosition(x, y)
    }

    fun isPlayerColliding(gameObject: GameObjectBase) = Intersector.overlaps(gameObject.bounds, bounds)
}