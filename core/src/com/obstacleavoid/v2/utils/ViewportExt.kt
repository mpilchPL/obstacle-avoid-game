package com.obstacleavoid.v2.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport


@JvmOverloads
fun Viewport.drawGrid(renderer: ShapeRenderer, cellSize: Int = 1) {
    val oldColor = Color(renderer.color.cpy())
    val doubleWorldWidth = worldWidth * 2
    val doubleWorldHeight = worldHeight * 2

    apply()
    renderer.use {
        renderer.color = Color.WHITE
        var x = -doubleWorldWidth
        var y = -doubleWorldHeight

        //draw grid
        while (x < doubleWorldWidth) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight)
            x += cellSize
        }
        while (y < doubleWorldHeight) {
            renderer.line(-doubleWorldWidth, y, doubleWorldWidth, y)
            y += cellSize
        }

        //draw 0,0 lines
        renderer.color = Color.RED
        renderer.line(-doubleWorldWidth, 0f, doubleWorldWidth, 0f)
        renderer.line(0f, -doubleWorldHeight, 0f, doubleWorldHeight)

        //draw world bounds
        renderer.color = Color.GREEN
        renderer.line(0f, worldHeight, worldWidth, worldHeight)
        renderer.line(worldWidth, 0f, worldWidth, worldHeight)
    }

    renderer.color = oldColor
}