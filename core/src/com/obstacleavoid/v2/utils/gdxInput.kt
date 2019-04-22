package com.obstacleavoid.v2.utils

import com.badlogic.gdx.Gdx

fun Int.isKeyPressed() : Boolean = Gdx.input.isKeyPressed(this)
