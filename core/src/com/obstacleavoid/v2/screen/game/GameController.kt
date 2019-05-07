package com.obstacleavoid.v2.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import com.obstacleavoid.v2.assets.PrefsFields
import com.obstacleavoid.v2.config.DifficultyLevel
import com.obstacleavoid.v2.config.GameConfig
import com.obstacleavoid.v2.entity.Obstacle
import com.obstacleavoid.v2.entity.Player
import com.obstacleavoid.v2.utils.GdxArray
import com.obstacleavoid.v2.utils.isKeyPressed
import com.obstacleavoid.v2.utils.logger


class GameController {

    companion object {
        @JvmStatic
        private val log = logger<GameController>()
    }

    private val startPlayerX = GameConfig.WORLD_WIDTH / 2f
    private val startPlayerY = 1f
    private var obstaclesTimer = 0f
    private val prefs: Preferences = Gdx.app.getPreferences(PrefsFields.PREF_FILE_NAME)
    private var difficultyLevel = DifficultyLevel.MEDIUM

    var lives = GameConfig.LIVES_START
        private set
    var player = Player()
        private set
    var score = 0
        private set

    val obstacles = GdxArray<Obstacle>()
    val gameOver
        get() = lives <= 0

    private val obstaclePool = Pools.get(Obstacle::class.java, 20)

    init {
        player.setPosition(startPlayerX, startPlayerY)
    }

    fun update(delta: Float) {
        if (gameOver){
            return
        }

        //   ===========update player==================
        var xSpeed = 0f
        when {
            Input.Keys.RIGHT.isKeyPressed() -> xSpeed = Player.MAX_X_SPEED
            Input.Keys.LEFT.isKeyPressed() -> xSpeed = -Player.MAX_X_SPEED
        }
        player.x = MathUtils.clamp((player.x + xSpeed), Player.MIN_X, Player.MAX_X)
        //   ==========================================

        updateObstacles()
        createNewObstacle(delta)
        if (playerCollidesWithObstacle()) {
            lives--
            when {
                gameOver -> {
                    log.debug("game over")
                    if (score > prefs.getInteger(PrefsFields.BEST_SCORE_FIELD, 0)){
                        prefs.putInteger(PrefsFields.BEST_SCORE_FIELD, score)
                        prefs.flush()
                    }
                }
                else -> restart()
            }
        }
    }

    private fun playerCollidesWithObstacle(): Boolean {
        obstacles.forEach {
            if (it.isPlayerColliding(player) && !(it.hit)) {
                it.hit = true
                return true
            }
        }
        return false
    }

    private fun updateObstacles() {
        obstacles.forEach {
            if (!it.update()) {
                obstaclePool.free(it)
                obstacles.removeValue(it, true)
                score++
            }
        }
    }

    private fun createNewObstacle(delta: Float) {
        obstaclesTimer += delta
        if (obstaclesTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            obstaclesTimer = 0f
            val obstacleX = MathUtils.random(Obstacle.BOUNDS_RADIUS, GameConfig.WORLD_WIDTH - Obstacle.BOUNDS_RADIUS)
            val obstacleY = GameConfig.WORLD_HEIGHT + Obstacle.BOUNDS_RADIUS
            val obstacle = obstaclePool.obtain()
            obstacle.setPosition(obstacleX, obstacleY)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }
    }

    private fun restart() {
        obstaclePool.freeAll(obstacles)
        obstacles.clear()
        player.setPosition(startPlayerX, startPlayerY)
    }
}