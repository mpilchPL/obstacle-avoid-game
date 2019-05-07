package com.obstacleavoid.v2.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle

@JvmOverloads
fun clearScreen(color: Color = Color.BLACK) = clearScreen(color.r, color.g, color.b, color.a)


fun clearScreen(red: Float, green: Float, blue: Float, alpha: Float) {
    Gdx.gl.glClearColor(red, green, blue, alpha)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
}

inline fun Batch.use (function: () -> Unit) {
    begin()
    function()
    end()
}

inline fun ShapeRenderer.use (function: () -> Unit) {
    begin(ShapeRenderer.ShapeType.Line)
    function()
    end()
}

@JvmOverloads
fun ShapeRenderer.circles(c: Circle, segments: Int = 30) {
    circle(c.x, c.y, c.radius, segments)
}