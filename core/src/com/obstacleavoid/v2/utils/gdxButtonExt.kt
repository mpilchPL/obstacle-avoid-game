package com.obstacleavoid.v2.utils

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.obstacleavoid.v2.config.ButtonsConfig

fun Button.setDefaultButtonSize() {
    return setSize(ButtonsConfig.BUTTON_WIDTH, ButtonsConfig.BUTTON_HEIGHT)
}