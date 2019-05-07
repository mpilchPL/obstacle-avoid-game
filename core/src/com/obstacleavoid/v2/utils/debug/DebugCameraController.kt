package com.obstacleavoid.v2.utils.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.obstacleavoid.v2.utils.logger

class DebugCameraController {

    companion object {
        @JvmStatic
        private val log = logger<DebugCameraController>()
    }


    private val cameraConfig = DebugCameraConfig()
    private val position = Vector2()
    private val startPosition = Vector2()
    private var cameraZoom = 1f
        set(value) { field = MathUtils.clamp(value, cameraConfig.maxZoomIn, cameraConfig.maxZoomOut) }


    init {
        log.debug("$cameraConfig")
    }

    fun setStartPosition(x: Float, y: Float) {
        startPosition.set(x, y)
        position.set(x, y)
    }

    fun applyTo(camera: OrthographicCamera) {
        camera.position.set(position, 0f)
        camera.zoom = cameraZoom
        camera.update()
    }

    fun handleDebugInput() {
        val delta = Gdx.graphics.deltaTime
        val moveSpeed = cameraConfig.moveSpeed * delta
        val zoomSpeed = cameraConfig.zoomSpeed * delta

        when {
            // camera movement keys
            cameraConfig.isLeftPressed() -> moveLeft(moveSpeed)
            cameraConfig.isRightPressed() -> moveRight(moveSpeed)
            cameraConfig.isUptPressed() -> moveUp(moveSpeed)
            cameraConfig.isDownPressed() -> moveDown(moveSpeed)
            //camera zoom keys
            cameraConfig.isZoomInPressed() -> cameraZoom -= zoomSpeed
            cameraConfig.isZoomOutPressed() -> cameraZoom += zoomSpeed
            // reset / log
            cameraConfig.isResetPressed() -> setDefault()
            cameraConfig.isLogPressed() -> printLog()
        }

    }

    private fun moveLeft(speed: Float) = moveCamera(-speed, 0f)
    private fun moveRight(speed: Float) = moveCamera(speed, 0f)
    private fun moveUp(speed: Float) = moveCamera(0f, speed)
    private fun moveDown(speed: Float) = moveCamera(0f, -speed)

    private fun moveCamera(xSpeed: Float, ySpeed: Float) = setPosition(position.x + xSpeed, position.y + ySpeed)
    private fun setPosition(x: Float, y: Float) = position.set(x, y)
    private fun setDefault() {
        position.set(startPosition)
        cameraZoom = 1f
        printLog()
    }
    private fun printLog() = println("camera pos: \n x: ${position.x} y: ${position.y}\n" +
            "camera zoom: $cameraZoom")



}