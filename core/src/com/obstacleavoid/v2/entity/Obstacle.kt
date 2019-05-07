package com.obstacleavoid.v2.entity

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.utils.Pool

class Obstacle : GameObjectBase(), Pool.Poolable {


    companion object {
        const val BOUNDS_RADIUS = 0.3f
        const val SIZE = 2 * BOUNDS_RADIUS
    }


    var ySpeed = 0.1f
    var hit = false
    override val bounds: Circle = Circle(x, y, BOUNDS_RADIUS)


    //  public functions
    fun update() : Boolean {
        y -= ySpeed
        if (y < 0)
            return false
        else if (hit)
            return false
        return true
    }

    override fun reset() {
        x = 0f
        y = 0f
        ySpeed = 0.1f
        hit = false
    }
}